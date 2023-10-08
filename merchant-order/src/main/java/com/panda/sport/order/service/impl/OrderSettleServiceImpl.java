package com.panda.sport.order.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.panda.sport.backup.mapper.BackupOrderMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.utils.FtpUtil;
import com.panda.sport.merchant.common.utils.ZipFilesUtils;
import com.panda.sport.merchant.common.vo.api.BetApiVo;
import com.panda.sport.order.service.OrderSettleService;
import com.panda.sport.order.service.expot.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.panda.sport.merchant.common.constant.Constant.orderStatusMap;

@Slf4j
@RefreshScope
@Service
public class OrderSettleServiceImpl implements OrderSettleService {
    @Autowired
    private BackupOrderMapper backupOrderMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private FtpProperties ftpProperties;

    @Value("${ftp_switch:on}")
    private String ftpSwitch;

    @Value("${json_ftp_switch:off}")
    private String jsonFtpSwitch;

    @Value("${csv_ftp_switch:on}")
    private String csvFtpSwitch;

    @Value("${ftp.upload.thread:5}")
    private Integer uploadThread;

    @Value("${ftp.upload.pageSize:1000}")
    private Integer ftpPageSize;

    private final static String FOLDER = "/panda_order_";
    private final static String SLASH = "/";
    private final static String VIP = "vip_";

    @Async
    @Override
    public void uploadSettleOrder(long startTime, long endTime, String codeParam) throws IOException {
        uploadSettleOrder(startTime, endTime, codeParam, null);
    }

    /**
     * 获取结算订单
     * 1000*60*60-1
     *
     * @return
     */
    @Override
    public void uploadSettleOrder(long startTime, long endTime, String codeParam, Integer vipLevel) throws IOException {
        log.info("UPLOAD_FTP上传开关=" + ftpSwitch);
        if ("off".equals(ftpSwitch)) {
            return;
        }
        Date executeDate = new Date(startTime);
        String executeStr = DateFormatUtils.format(executeDate, "yyyyMMdd");
        // 获取所有商户信息
        List<MerchantPO> merchantList;
        if (StringUtils.isEmpty(codeParam)) {
            merchantList = merchantMapper.queryMerchantTree();
        } else {
            merchantList = merchantMapper.queryMerchantTreeByMerchantCode(codeParam);
        }
        if (merchantList.size() <= 0) {
            log.info(executeStr + "UPLOAD_结算时间:{},没有商户:{},{}", DateUtil.lastWeek(), merchantList.size(), codeParam);
            return;
        }
        // 分组map
        Map<String, MerchantPO> merchantMap = merchantList.stream().collect(Collectors.toMap(MerchantPO::getMerchantCode, merchantPO -> merchantPO));
        // 父类组装
        Map<String, MerchantPO> parentMerchantMap = new HashMap<>();
        // 获取上级id
        List<String> parentIdList = merchantList.stream().map(MerchantPO::getParentId).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        // 如果有上级
        if (parentIdList.size() > 0) {
            List<MerchantPO> parentMerchantList = merchantMapper.getMerchantInMerchantId(new HashSet<>(parentIdList));
            if (parentMerchantList.size() > 0) {
                parentMerchantMap = parentMerchantList.stream().collect(Collectors.toMap(MerchantPO::getId, merchantPO -> merchantPO));
            }
        }
        List<String> merchantCodeList = merchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
        long startALlWorkL = System.currentTimeMillis();
        for (String merchantCode : merchantCodeList) {
            MerchantPO merchantPO = merchantMap.getOrDefault(merchantCode, null);
            Set<BetApiVo> orderSettleList = new HashSet<>();
            try {
                log.info(executeStr + "UPLOAD_START:merchantCode:" + merchantCode + ",merchantPO:" + merchantPO.getId() + ",parentId:" + merchantPO.getParentId() + ",agentLevel:" + merchantPO.getAgentLevel());

                startUploadMerchantOrder(executeDate, executeStr, merchantPO, merchantCode, startTime, endTime, orderSettleList, parentMerchantMap, vipLevel);

            } catch (Exception e) {
                log.error(executeStr + "UPLOAD_ERRROR_merchantCode:" + merchantCode + ",parentMerchantMap:" + parentMerchantMap.get(merchantPO.getParentId()) + "异常!", e);
            } finally {
                orderSettleList = null;
            }
        }
        log.info(executeStr + ",UPLOAD_END!所有商户任务上传结束,共:" + (System.currentTimeMillis() - startALlWorkL));
    }

    /**
     * vip商户上传FTP
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param codeParam 商户编码
     * @throws IOException
     */
    @Async
    @Override
    public void uploadSettleOrderVip(long startTime, long endTime, String codeParam) throws IOException {

        log.info("VIP_UPLOAD_FTP上传开关=" + ftpSwitch);
        if ("off".equals(ftpSwitch) ) {
            return;
        }
        Date executeDate = new Date(startTime);
        String executeStr = null;
        try {
            // 格式化
            executeStr = DateFormatUtils.format(executeDate, "yyyyMMdd");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        // 获取所有商户信息
        List<MerchantPO> merchantList = null;

        if (StringUtils.isEmpty(codeParam)) {
            // 获取商户树
            merchantList = merchantMapper.queryMerchantTree();
        } else {
            // 根据商户编码获取商户树
            merchantList = merchantMapper.queryMerchantTreeByMerchantCode(codeParam);
        }
        if (merchantList.size() <= 0) {
            log.info(executeStr + "VIP_UPLOAD_结算时间:{},没有商户:{},{}", DateUtil.lastWeek(), merchantList.size(), codeParam);
            return;
        }
        // 商户编码分组
        Map<String, MerchantPO> merchantMap = merchantList.stream().collect(Collectors.toMap(MerchantPO::getMerchantCode, merchantPO -> merchantPO));
        // 父类组装
        Map<String, MerchantPO> parentMerchantMap = new HashMap<>();
        // 获取上级id
        List<String> parentIdList = merchantList.stream().map(MerchantPO::getParentId).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        // 如果有上级
        if (parentIdList.size() > 0) {
            List<MerchantPO> parentMerchantList = merchantMapper.getMerchantInMerchantId(new HashSet<>(parentIdList));
            if (parentMerchantList.size() > 0) {
                parentMerchantMap = parentMerchantList.stream().collect(Collectors.toMap(MerchantPO::getId, merchantPO -> merchantPO));
            }
        }
        List<String> merchantCodeList = merchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
        long startALlWorkL = System.currentTimeMillis();
        for (String merchantCode : merchantCodeList) {
            MerchantPO merchantPO = merchantMap.getOrDefault(merchantCode, null);
            Set<BetApiVo> orderSettleList = new HashSet<>();
            try {
                log.info(executeStr + "VIP_UPLOAD_START:merchantCode:" + merchantCode + ",merchantPO:" + merchantPO.getId() + ",parentId:" + merchantPO.getParentId() + ",agentLevel:" + merchantPO.getAgentLevel());

                startUploadMerchantOrderVip(executeDate, executeStr, merchantPO, merchantCode, startTime, endTime, orderSettleList, parentMerchantMap);

            } catch (Exception e) {
                log.error(executeStr + "VIP_UPLOAD_ERRROR_merchantCode:" + merchantCode + ",parentMerchantMap:" + parentMerchantMap.get(merchantPO.getParentId()) + "异常!", e);
            } finally {
                orderSettleList = null;
            }
        }
        log.info(executeStr + ",VIP_UPLOAD_END!所有商户任务上传结束,共:" + (System.currentTimeMillis() - startALlWorkL));
    }

    /**
     * 上传所有的结算列表csv和json到ftp
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param codeParam  商户编码
     * @throws IOException
     */
    @Async
    @Override
    public void uploadSettleOrderAllVip(long startTime, long endTime, String codeParam) throws IOException {
        log.info("VIP_UPLOAD_FTP上传开关=" + ftpSwitch);
        if ("off".equals(ftpSwitch)) {
            return;
        }
        Date executeDate = new Date(startTime);
        String executeStr = null;
        try {
            executeStr = DateFormatUtils.format(executeDate, "yyyyMM");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        Set<BetApiVo> orderSettleList = new HashSet<>();
        try {
            startUploadMerchantOrderAllVip(executeDate, executeStr, startTime, endTime, orderSettleList);
        } catch (Exception e) {
            log.error(executeStr + "VIP_UPLOAD_ERRROR_merchantCode:" + "异常!", e);
        }
    }

    private void startUploadMerchantOrder(Date executeDate, String executeStr, MerchantPO merchantPO, String merchantCode, long startTime, long endTime,
                                          Set<BetApiVo> orderSettleList, Map<String, MerchantPO> parentMerchantMap, Integer vipLevel) throws IOException {
        long startWorkL = System.currentTimeMillis();

        int total =0  ;
        // 大于7天查大表
        Date date = DateUtils.transferLongToDate(endTime);

        String  patternString = "yyyy-MM";
        String csvString ;
        if((endTime-startTime)/(60*60*24*1000) <=1){
            csvString = "yyyyMMdd";
        }else{
            csvString = "yyyyMM";
        }

        Boolean largerThanSevenDay = date.before(DateUtils.addNDay(new Date(),-3));
        log.info("当前时间大于三天?,{}",largerThanSevenDay);

        if (largerThanSevenDay){
            total = backupOrderMapper.countTicketListBackUp(merchantCode, startTime, endTime, vipLevel);

        }else {
            total = backupOrderMapper.countSettleOrderListBackUp(merchantCode, startTime, endTime, vipLevel);
        }

        if (total == 0) {
            return;
        }
        log.info(executeStr + "UPLOAD_Statistic:" + merchantCode + ",cost:" + (System.currentTimeMillis() - startWorkL) + ",total=" + total);
        String dir = "";
        String parentCode;
        if (merchantPO.getAgentLevel() == 0 || merchantPO.getAgentLevel() == 1) {
            //直营商户panda_order_694262
            parentCode = merchantCode;
            dir = FOLDER + merchantCode + SLASH + DateFormatUtils.format(executeDate, patternString);


        } else if (merchantPO.getAgentLevel() == 2) {
            parentCode = parentMerchantMap.get(merchantPO.getParentId()).getMerchantCode();
                // 二级商户
            dir = FOLDER + parentCode + FOLDER + merchantCode + SLASH + DateFormatUtils.format(executeDate, patternString);

        } else {
            return;
        }
        if (total > 0) {
            if (total > ftpPageSize * 3) {
                log.info(executeStr + "UPLOAD_total=" + total + ",线程数=" + uploadThread + "," + merchantCode);
                CountDownLatch countDownLatch = new CountDownLatch(uploadThread);
                ExecutorService threadPool = Executors.newFixedThreadPool(uploadThread);
                ConcurrentHashMap<Integer, Object> resultMap = new ConcurrentHashMap<>();
                try {
                    //int start, end;
                    int eachThread = total / uploadThread;
                    log.info(executeStr + "UPLOAD_eachThread:" + eachThread);
                    for (int i = 0; i < uploadThread; i++) {
                        int start = i * eachThread;
                        int end = (i + 1 == uploadThread) ? total : (i + 1) * eachThread - 1;
                        log.info(executeStr + "UPLOAD_start=" + start + ",end=" + end + ",pageSize=" + ftpPageSize + "," + merchantCode);
                        threadPool.execute(new TaskExecutor(countDownLatch, i + 1, resultMap, backupOrderMapper, merchantCode, startTime, endTime, start, end, ftpPageSize, vipLevel,largerThanSevenDay));
                    }
                    countDownLatch.await();
                    log.info(executeStr + "UPLOAD_所有线程都执行查询完毕，开始合并数据!" + merchantCode);
                    for (Map.Entry entry : resultMap.entrySet()) {
                        List<BetApiVo> tempList = (List<BetApiVo>) entry.getValue();
                        orderSettleList.addAll(tempList);
                    }
                } catch (InterruptedException e) {
                    log.error(executeStr + "UPLOAD_ERROR_线程导出异常!" + merchantCode, e);
                } finally {
                    threadPool.shutdown();
                    resultMap = null;
                }
            } else {
                if (largerThanSevenDay) {
                    List<BetApiVo> tempSettleList = backupOrderMapper.queryTicketListBackUp(merchantCode, startTime, endTime, 0, ftpPageSize * 3, vipLevel);
                    orderSettleList.addAll(tempSettleList);
                }else{
                    List<BetApiVo> tempSettleList = backupOrderMapper.querySettleOrderListBackUp(merchantCode, startTime, endTime, 0, ftpPageSize * 3, vipLevel);
                    orderSettleList.addAll(tempSettleList);
                }
            }
            // 处理数据
            hanleOrderSettleList(orderSettleList);
            log.info(executeStr + "UPLOAD_Search_END:merchantCode:{},商户名称:{},结算时间:{},结算订单数:{},total:{},parentCode:{},dir:{},cost:{}",
                    merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate, orderSettleList.size(), total, parentCode, dir, (System.currentTimeMillis() - startWorkL));
            writeIntoCSVFile(csvString,executeDate, merchantPO, dir, orderSettleList, vipLevel);
            log.info(executeStr + "UPLOAD_CSV_END:merchantCode:{},商户名称:{},parentCode:{},dir:{},cost:{}",
                    merchantPO.getMerchantCode(), merchantPO.getMerchantName(), parentCode, dir, (System.currentTimeMillis() - startWorkL));
            int size = orderSettleList.size();
            int pageSize = 100000;
            if (size > pageSize) {
                List<BetApiVo> objList = Lists.newArrayList(orderSettleList);
                int pages = (int) Math.ceil((float) size / pageSize);
                for (int i = 0; i < pages; i++) {
                    int start = i * pageSize;
                    int end = (i + 1 == pages) ? size : (i + 1) * pageSize;
                    Set<BetApiVo> subList = Sets.newLinkedHashSet(objList.subList(start, end));
                    writeIntoJsonFile(vipLevel, executeDate, merchantPO, dir, subList, "-" + i);
                    subList = null;
                }
                objList = null;
            } else {
                writeIntoJsonFile(vipLevel, executeDate, merchantPO, dir, orderSettleList, "");
            }
            log.info(executeStr + "UPLOAD_JSON_END:merchantCode:{},商户名称:{},parentCode:{},dir:{},cost:{}",
                    merchantPO.getMerchantCode(), merchantPO.getMerchantName(), parentCode, dir, (System.currentTimeMillis() - startWorkL));
        }
    }

    private void hanleOrderSettleList(Set<BetApiVo> orderSettleList) {
        for (BetApiVo betApiVo : orderSettleList) {
            if(Constant.INT_3.equals(betApiVo.getOutcome())  || Constant.INT_4.equals(betApiVo.getOutcome())
             || Constant.INT_5.equals(betApiVo.getOutcome())  || Constant.INT_6.equals(betApiVo.getOutcome())){
                betApiVo.setValidOrderAmount(betApiVo.getProfitAmount());
            }
        }
    }

    /**
     *  上传商户注单记录vip到ftp
     * @param executeDate  执行时间
     * @param executeStr  执行时间字符串
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @param orderSettleList  结算列表
     * @throws IOException
     */
    private void startUploadMerchantOrderAllVip(Date executeDate, String executeStr, long startTime, long endTime,
                                                Set<BetApiVo> orderSettleList) throws IOException {
        long startWorkL = System.currentTimeMillis();
        // 从备份结算表中获取总条数
        int total = backupOrderMapper.countTicketListBackUp(null, startTime, endTime, 1);
        if (total == 0) {
            return;
        }
        log.info(executeStr + "VIP_UPLOAD_Statistic:所有,cost:" + (System.currentTimeMillis() - startWorkL) + ",total=" + total);
        String dir = "panda_order_all_vip";
        if (total > 0) {
            // 如果获取记录太多，分多个线程上传记录
            if (total > ftpPageSize * 3) {
                log.info(executeStr + "VIP_UPLOAD_total=" + total + ",线程数=" + uploadThread);
                CountDownLatch countDownLatch = new CountDownLatch(uploadThread);
                ExecutorService threadPool = Executors.newFixedThreadPool(uploadThread);
                ConcurrentHashMap<Integer, Object> resultMap = new ConcurrentHashMap<>();
                try {
                    //int start, end;
                    int eachThread = total / uploadThread;
                    log.info(executeStr + "VIP_UPLOAD_eachThread:" + eachThread);
                    for (int i = 0; i < uploadThread; i++) {
                        int start = i * eachThread;
                        int end = (i + 1 == uploadThread) ? total : (i + 1) * eachThread - 1;
                        log.info(executeStr + "VIP_UPLOAD_start=" + start + ",end=" + end + ",pageSize=" + ftpPageSize);
                        // 数据量太大所以分批次获取列表
                        threadPool.execute(new VipTaskExecutor(countDownLatch, i + 1, resultMap, backupOrderMapper, null, startTime, endTime, start, end, ftpPageSize, 1));
                    }
                    countDownLatch.await();
                    log.info(executeStr + "VIP_UPLOAD_所有线程都执行查询完毕，开始合并数据!所有");
                    for (Map.Entry entry : resultMap.entrySet()) {
                        List<BetApiVo> tempList = (List<BetApiVo>) entry.getValue();
                        orderSettleList.addAll(tempList);
                    }
                } catch (InterruptedException e) {
                    log.error(executeStr + "VIP_UPLOAD_ERROR_线程导出异常!所有", e);
                } finally {
                    threadPool.shutdown();
                    resultMap = null;
                }
            } else {
                List<BetApiVo> tempSettleList = backupOrderMapper.queryTicketListBackUp(null, startTime, endTime, 0, ftpPageSize * 3, 1);
                orderSettleList.addAll(tempSettleList);
            }
            log.info(executeStr + "VIP_UPLOAD_Search_END:merchantCode:{},商户名称:{},结算时间:{},结算订单数:{},total:{},parentCode:{},dir:{},cost:{}",
                    executeDate, orderSettleList.size(), total, dir, (System.currentTimeMillis() - startWorkL));
            // 写文件到csv文件
            writeIntoCSVFile("yyyyMM", executeDate, null, dir, orderSettleList, 1);
            log.info(executeStr + "VIP_UPLOAD_CSV_END:merchantCode:{},商户名称:{},parentCode:{},dir:{},cost:{}",
                    dir, (System.currentTimeMillis() - startWorkL));
            int size = orderSettleList.size();
            int pageSize = 100000;
            // 如果大于10万条，就分页写入Json文件
            if (size > pageSize) {
                List<BetApiVo> objList = Lists.newArrayList(orderSettleList);
                int pages = (int) Math.ceil((float) size / pageSize);
                for (int i = 0; i < pages; i++) {
                    int start = i * pageSize;
                    int end = (i + 1 == pages) ? size : (i + 1) * pageSize;
                    Set<BetApiVo> subList = Sets.newLinkedHashSet(objList.subList(start, end));
                    writeIntoJsonFile(1, executeDate, null, dir, subList, "-" + i);
                    subList = null;
                }
                objList = null;
            } else {
                writeIntoJsonFile(1, executeDate, null, dir, orderSettleList, "");
            }
            log.info(executeStr + "VIP_UPLOAD_JSON_END:merchantCode:{},商户名称:{},parentCode:{},dir:{},cost:{}",
                    dir, (System.currentTimeMillis() - startWorkL));
        }
    }

    /**
     * 上传每个商户的vip csv和json记录到ftp
     * @param executeDate
     * @param executeStr
     * @param merchantPO
     * @param merchantCode
     * @param startTime
     * @param endTime
     * @param orderSettleList
     * @param parentMerchantMap
     * @throws IOException
     */
    private void startUploadMerchantOrderVip(Date executeDate, String executeStr, MerchantPO merchantPO, String merchantCode, long startTime, long endTime,
                                             Set<BetApiVo> orderSettleList, Map<String, MerchantPO> parentMerchantMap) throws IOException {
        long startWorkL = System.currentTimeMillis();
        int total = backupOrderMapper.countTicketListBackUp(merchantCode, startTime, endTime, 1);
        if (total == 0) {
            return;
        }
        log.info(executeStr + "VIP_UPLOAD_Statistic:" + merchantCode + ",cost:" + (System.currentTimeMillis() - startWorkL) + ",total=" + total);
        String dir = "";
        String parentCode;
        if (merchantPO == null) {

        } else if (merchantPO.getAgentLevel() == 0 || merchantPO.getAgentLevel() == 1) {
            //直营商户panda_order_694262
            parentCode = merchantCode;
            dir = FOLDER + merchantCode;
        } else if (merchantPO.getAgentLevel() == 2) {
            parentCode = parentMerchantMap.get(merchantPO.getParentId()).getMerchantCode();            // 二级商户
            dir = FOLDER + parentCode + FOLDER + merchantCode;
        }
        if (total > 0) {
            if (total > ftpPageSize * 3) {
                log.info(executeStr + "VIP_UPLOAD_total=" + total + ",线程数=" + uploadThread + "," + merchantCode);
                CountDownLatch countDownLatch = new CountDownLatch(uploadThread);
                ExecutorService threadPool = Executors.newFixedThreadPool(uploadThread);
                ConcurrentHashMap<Integer, Object> resultMap = new ConcurrentHashMap<>();
                try {
                    //int start, end;
                    int eachThread = total / uploadThread;
                    log.info(executeStr + "VIP_UPLOAD_eachThread:" + eachThread);
                    for (int i = 0; i < uploadThread; i++) {
                        int start = i * eachThread;
                        int end = (i + 1 == uploadThread) ? total : (i + 1) * eachThread - 1;
                        log.info(executeStr + "VIP_UPLOAD_start=" + start + ",end=" + end + ",pageSize=" + ftpPageSize + "," + merchantCode);
                        threadPool.execute(new VipTaskExecutor(countDownLatch, i + 1, resultMap, backupOrderMapper, merchantCode, startTime, endTime, start, end, ftpPageSize, 1));
                    }
                    countDownLatch.await();
                    log.info(executeStr + "VIP_UPLOAD_所有线程都执行查询完毕，开始合并数据!" + merchantCode);
                    for (Map.Entry entry : resultMap.entrySet()) {
                        List<BetApiVo> tempList = (List<BetApiVo>) entry.getValue();
                        orderSettleList.addAll(tempList);
                    }
                } catch (InterruptedException e) {
                    log.error(executeStr + "VIP_UPLOAD_ERROR_线程导出异常!" + merchantCode, e);
                } finally {
                    threadPool.shutdown();
                    resultMap = null;
                }
            } else {
                List<BetApiVo> tempSettleList = backupOrderMapper.queryTicketListBackUp(merchantCode, startTime, endTime, 0, ftpPageSize * 3, 1);
                orderSettleList.addAll(tempSettleList);
            }
            log.info(executeStr + "VIP_UPLOAD_Search_END:merchantCode:{},商户名称:{},结算时间:{},结算订单数:{},total:{},parentCode:{},dir:{},cost:{}",
                    merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate, orderSettleList.size(), total, dir, (System.currentTimeMillis() - startWorkL));
            writeIntoCSVFile("yyyyMM", executeDate, merchantPO, dir, orderSettleList, 1);
            log.info(executeStr + "VIP_UPLOAD_CSV_END:merchantCode:{},商户名称:{},parentCode:{},dir:{},cost:{}",
                    merchantPO.getMerchantCode(), merchantPO.getMerchantName(), dir, (System.currentTimeMillis() - startWorkL));
            int size = orderSettleList.size();
            int pageSize = 100000;
            if (size > pageSize) {
                List<BetApiVo> objList = Lists.newArrayList(orderSettleList);
                int pages = (int) Math.ceil((float) size / pageSize);
                for (int i = 0; i < pages; i++) {
                    int start = i * pageSize;
                    int end = (i + 1 == pages) ? size : (i + 1) * pageSize;
                    Set<BetApiVo> subList = Sets.newLinkedHashSet(objList.subList(start, end));
                    writeIntoJsonFile(1, executeDate, merchantPO, dir, subList, "-" + i);
                    subList = null;
                }
                objList = null;
            } else {
                writeIntoJsonFile(1, executeDate, merchantPO, dir, orderSettleList, "");
            }
            log.info(executeStr + "VIP_UPLOAD_JSON_END:merchantCode:{},商户名称:{},parentCode:{},dir:{},cost:{}",
                    merchantPO.getMerchantCode(), merchantPO.getMerchantName(), dir, (System.currentTimeMillis() - startWorkL));
        }
    }

    /**
     * 上传csv到ftp
     *
     * @param csvString
     * @param executeDate   执行时间
     * @param merchantPO    商户
     * @param dir           目的目录
     * @param resultList    返回结果
     * @param vipLevel      vip级别
     * @throws IOException
     */
    private void writeIntoCSVFile(String csvString, Date executeDate, MerchantPO merchantPO, String dir, Set<BetApiVo> resultList, Integer vipLevel) throws IOException {

        if ("off".equals(csvFtpSwitch)) {
            log.info("UPLOAD_CSV上传FTP已经关闭:" + csvFtpSwitch);
            return;
        }
        String  dateName  = (vipLevel != null && vipLevel == 1 ? VIP : "") + DateFormatUtils.format(executeDate, csvString) ;
        String zipName = dateName + ".zip";
        String csvName = dateName + ".csv";
        InputStream inputStream = null;
        try {
            byte[] bytes = exportTicketToFtp(resultList);
            //String decoded = new String(bytes, "UTF-8");
            File tempFile = File.createTempFile(DateFormatUtils.format(executeDate, csvString), ".csv");
            // CsvWriter writer = new CsvWriter(tempFile.getCanonicalPath(), ',', Charset.forName("GBK"));
            FileOutputStream fos = new FileOutputStream(tempFile);
            //OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            // CsvWriter writer = new CsvWriter(CSV_PATH+fileName, ',', Charset.forName("UTF-8"));
            //String[] row = {decoded};
            //writer.writeRecord(row);
            //writer.close();
            //osw.close();
            try {
                fos.write(bytes);
            } finally {
                try {
                    fos.close();
                } catch (IOException var8) {
                    log.error("UPLOAD_关闭流异常!", var8);
                }
            }

            File zipTemp = File.createTempFile(DateFormatUtils.format(executeDate, csvString), ".zip");
            ZipFilesUtils.zipCsv(Arrays.asList(tempFile),csvName,zipTemp.getPath());
            inputStream = new FileInputStream(zipTemp);
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", dir, zipName, inputStream);
            log.info("UPLOAD_csv:商户CODE:{},商户名称:{},结算时间:{},ftp写入成功", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate);
        } catch (Exception e) {
            log.error("UPLOAD_csv:商户CODE:{},商户名称:{},结算时间:{},ftp写入失败", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate);
            log.error("UPLOAD_写入文件失败!", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 导出数据为byte数组
     * 注意： 如果有长数据需要加\t ,否则数据显示不全
     *
     * @param resultList
     * @return
     */
    private byte[] exportTicketToFtp(Set<BetApiVo> resultList) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        int i = 0;
        for (BetApiVo order : resultList) {
            if (order.getOrderStatus() == null || order.getOrderStatus() != 1) {
                continue;
            }
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", order.getUserName());
            rowData.put("3", order.getMerchantCode());
            rowData.put("4", CurrencyTypeEnum.optGetDescription(order.getCurrency()));

            String sportName = "";
            String marKetType = "";
            String marketValue = "";
            BigDecimal oddsValue = null;
             Long beginTime = null;
            Long matchId = null;
            String matchName = "";
            String matchInfo = "";
            String  playOptionName = "";
            String playName = "";
            Integer matchType = null ;
            if(CollectionUtils.isNotEmpty(order.getDetailList())){

                sportName =  order.getDetailList().get(0).getSportName();
                marKetType = order.getDetailList().get(0).getMarketType();
                marketValue = order.getDetailList().get(0).getMarketValue();
                oddsValue = order.getDetailList().get(0).getOddsValue();
                beginTime =  order.getDetailList().get(0).getBeginTime();
                matchId = order.getDetailList().get(0).getMatchId();
                matchName = order.getDetailList().get(0).getMatchName();
                matchInfo = order.getDetailList().get(0).getMatchInfo();
                playOptionName = order.getDetailList().get(0).getPlayOptionName();
                playName = order.getDetailList().get(0).getPlayName();
                matchType = order.getDetailList().get(0).getMatchType();
            }

            rowData.put("5", sportName);
            rowData.put("6", order.getProfitAmount() == null ? "" : order.getProfitAmount());
            rowData.put("7", order.getOrderAmount());
            rowData.put("8", order.getSeriesType() == 1 ? marKetType : " ");
            rowData.put("09", order.getSeriesType() == 1 ? (marketValue == null ? "" : marketValue+"\t"): " ");
            rowData.put("10", order.getSeriesType() == 1 ? oddsValue: "");
            rowData.put("11", orderStatusMap.get(order.getOrderStatus()));
            // 投注时间
            rowData.put("12", DateUtils.transferLongToDateStrings(order.getCreateTime()));
            // 开赛时间
            rowData.put("13", beginTime == null ? "" :DateUtils.transferLongToDateStrings(beginTime));
            // 结算时间
            rowData.put("14", order.getSettleTime() == null ? "" : DateUtils.transferLongToDateStrings(order.getSettleTime()));
            rowData.put("15", order.getOrderNo() + "\t");
            rowData.put("16", order.getSeriesValue());
            rowData.put("17", Constant.settleStatusMap.get(order.getOutcome()) == null ? "未结算" : Constant.settleStatusMap.get(order.getOutcome()));
            rowData.put("18", order.getSeriesType() == 1 ?  matchId: " ");
            rowData.put("19", order.getSeriesType() == 1 ?  matchName: " ");
            rowData.put("20", order.getSeriesType() == 1 ? matchInfo : " ");
            if (matchType == null){
                rowData.put("21", "");
            }else{
                rowData.put("21", matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘"));
            }
            rowData.put("22", playOptionName);
            rowData.put("23",playName);
            rowData.put("24", order.getPreBetAmount());
            rowData.put("25", order.getValidOrderAmount() == null ? "0" : order.getValidOrderAmount());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "序号");
        header.put("2", "用户名称");
        header.put("3", "商户名称");
        header.put("4", "用户币种");
        header.put("5", "赛种");
        header.put("6", "盈亏");
        header.put("7", "下注金额");
        header.put("8", "盘口类型");
        header.put("9", "盘口值");
        header.put("10", "赔率");
        header.put("11", "注单状态");
        header.put("12", "投注时间");
        header.put("13", "开赛时间");
        header.put("14", "结算时间");
        header.put("15", "注单号");
        header.put("16", "串关值");
        header.put("17", "结算状态");
        header.put("18", "赛事ID");
        header.put("19", "联赛名称");
        header.put("20", "比赛对阵");
        header.put("21", "注单类型");
        header.put("22", "投注项名称");
        header.put("23", "玩法名称");
        header.put("24", "提前结算投注金额");
        header.put("25", "有效投注金额");
        return CsvUtil.exportCSV(header, exportData);
    }

    /**
     * 写入文件
     *
     * @param vipLevel
     * @param merchantPO
     * @param dir
     * @param betApiVoList
     * @paramjava.io.IOException: 用户名或密码不正确。
     */
    private void writeIntoJsonFile(Integer vipLevel, Date executeDate, MerchantPO merchantPO, String dir, Set<BetApiVo> betApiVoList, String subDir) throws IOException {
        if ("off".equals(jsonFtpSwitch)) {
            log.info("UPLOAD_Json上传FTP已经关闭:" + csvFtpSwitch);
            return;
        }
        String fileName = (vipLevel != null && vipLevel == 1 ? VIP : "") + DateFormatUtils.format(executeDate, "yyyyMMdd") + subDir + ".json";
        InputStream inputStream = null;
        try {
            File file = new File(fileName);
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            // 转换json
            String jsonOrder = JSONObject.toJSONString(betApiVoList);
            write.write(compress(jsonOrder));
            //write.write(jsonOrder);
            write.flush();
            write.close();
            inputStream = new FileInputStream(file);
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", dir, fileName, inputStream);
            log.info("UPLOAD_json商户CODE:{},商户名称:{},结算时间:{},json写入成功", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate);
        } catch (Exception e) {
            log.error("UPLOAD_json商户CODE:{},商户名称:{},结算时间:{},json写入失败", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate);
            log.error("UPLOAD_写入文件失败!", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

/*    public static void main(String[] args) throws IOException {
        String fileName = "D:\\work\\panda-merchant\\20211128.json";
        File file = new File(fileName);
        String jsonOrder = FileUtils.readFileToString(file, "UTF-8");

        String ad = compress(jsonOrder);

        String original = unCompress(jsonOrder);
        JSONArray jsonArray = JSONArray.parseArray(original);
        Object[] str =jsonArray.stream().toArray();
        for (Object vo : str) {
            log.info("vo=" + vo);
        }

    }*/

    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲 区数组
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n = 0;
        // 将未压缩数据读入字节数组
        while ((n = gzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("UTF-8");
    }


    public static String compress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输出流
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        // 将字节写入此输出流
        gzip.write(str.getBytes(StandardCharsets.UTF_8)); // 因为后台默认字符集有可能是GBK字符集，所以此处需指定一个字符集
        gzip.close();
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("ISO-8859-1");
    }

}
