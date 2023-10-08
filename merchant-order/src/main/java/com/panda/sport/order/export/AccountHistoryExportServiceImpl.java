package com.panda.sport.order.export;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.sport.backup.mapper.BackupTAccountChangeHistoryMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.BizTypeEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  账变导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("accountHistoryExportServiceImpl")
@Slf4j
public class AccountHistoryExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private BackupTAccountChangeHistoryMapper accountChangeHistoryMapper;


    private static final int threadSize = 40000;

    private static final int pageSize = 1000;


    @Override
    @Async("asyncServiceExecutor")
    public void export(MerchantFile merchantFile) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("开始账变记录导出 param = {}", merchantFile.getExportParam());
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            super.updateFileStart(merchantFile.getId());
            UserAccountFindVO findVO = JSON.parseObject(merchantFile.getExportParam(), UserAccountFindVO.class);
            JSONObject jsonObject = JSON.parseObject(merchantFile.getExportParam());
            String language = jsonObject.getString("language");
            List<Map<String, Object>> mapList = new ArrayList<>();
            int total = 0;
            if (merchantFile.getDataSize() == null) {
                total = queryCount(findVO);
            }else {
                total = merchantFile.getDataSize();
            }
            log.info(language + ", id = {} 组装开始账变记录导出: 总数 = {}",merchantFile.getId() , total);

            if (total < MerchantFileService.SPLIT_FILE_DATE_SIZE_2) {
                InputStream inputStream = null;
                int size = 1000;
                int i = 0;
                int star = 0;
                super.updateFileStart(merchantFile.getId());
                super.updateRate(merchantFile.getId(), 40L);
                try {
                    do {
                        findVO.setStart(star);
                        findVO.setPageSize(size);
                        List<Map<String, Object>> list = queryList(findVO);
                        if (list != null) {
                            mapList.addAll(list);
                        }
                        i++;
                        star = i * size;
                    } while (star < total);
                    super.updateRate(merchantFile.getId(), 80L);
                    log.info("id = {} 查询结束,花费时间:" + (System.currentTimeMillis() - startTime),merchantFile.getId());
                    if (CollectionUtils.isEmpty(mapList)) {
                        throw new Exception("未查询到数据");
                    }
                    inputStream = new ByteArrayInputStream(exportAccountChangeHistoryCsv(mapList, findVO.getLanguage()));
                    super.uploadFile(merchantFile, inputStream);
                    log.info("id = {}导出结束,花费时间: {}", merchantFile.getId(),(System.currentTimeMillis() - startTime));
                }finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            log.error("流关闭异常！", e);
                        }
                    }
                }
            }else {
                String[] names = merchantFile.getFtpFileName().split(",");
                int k = 0;
                for (int j = names.length; j > 0; j--) {
                    InputStream inputStream = null;
                    try {
                        mapList = new ArrayList<>();
                        super.updateFileStart(merchantFile.getId());
                        int thrednum = 5;
                        ExecutorService service = Executors.newFixedThreadPool(thrednum);
                        log.info(" id = {} export  thrednum=" + thrednum,merchantFile.getId());
                        try {
                            BlockingQueue<Future<List<Map<String, Object>>>> queue = new LinkedBlockingQueue<>();
                            for (int i = 0; i < thrednum; i++) {
                                Future<List<Map<String, Object>>> future = service.submit(queryList(k, i, findVO));
                                queue.add(future);
                            }
                            int queueSize = queue.size();
                            log.info(" id = {} export  queueSize=" + queueSize,merchantFile.getId());
                            for (int i = 0; i < queueSize; i++) {
                                List<Map<String, Object>> list = queue.take().get();
                                log.info("export  list=" + (list == null ? 0 : list.size()));
                                mapList.addAll(list);
                            }
                            log.info("id = {} export  mapList=" + mapList.size(), merchantFile.getId());
                        } finally {
                            service.shutdown();
                        }
                        super.updateRate(merchantFile.getId(), 80L / j);
                        log.info("id = {} 查询结束,花费时间:" + (System.currentTimeMillis() - startTime),merchantFile.getId());
                        if (CollectionUtils.isEmpty(mapList)) {
                            throw new Exception("未查询到数据");
                        }
                        inputStream = new ByteArrayInputStream(exportAccountChangeHistoryCsv(mapList, findVO.getLanguage()));
                        super.uploadFileMax(merchantFile, inputStream, names[k]);
                        log.info("id = {} 大文件导出结束,花费时间: {}", (System.currentTimeMillis() - startTime), merchantFile.getId());
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                log.error("流关闭异常3！", e);
                            }
                        }
                    }
                    k++;
                }
                super.updateFileStatusEnd(merchantFile.getId());
            }
            log.info(" id = {} 导出结束,花费时间: {}", (System.currentTimeMillis() - startTime),merchantFile.getId());
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("开始账变记录导出异常！", e);
        }
    }

    public Callable<List<Map<String, Object>>>  queryList(final int j, final int i,final UserAccountFindVO findVO) {
        long starTime = System.currentTimeMillis();
        Callable<List<Map<String, Object>>> callable = new Callable<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call() throws Exception {
                List<Map<String, Object>> list = new ArrayList<>();
                int startIndex = i * threadSize + (j * MerchantFileService.SPLIT_FILE_DATE_SIZE_2);
                for (int k = 0; k < 40; k++) {
                    findVO.setStart(startIndex + (k * pageSize));
                    findVO.setPageSize(pageSize);
                    try {
                        List<Map<String, Object>> listResult = accountChangeHistoryMapper.queryChangeHistoryListNewExportList(findVO);
                        if (CollectionUtil.isEmpty(listResult)){
                            break;
                        }
                        list.addAll(listResult);
                    } catch (Exception e) {
                        log.error("注单查询导出异常！开始补偿", e);
                    }
                }
                return list;
            }
        };
        long time = System.currentTimeMillis() - starTime;
        if (time > 300000) {
            log.info("数据库查询订单耗时 {}", time / 1000);
        }
        return callable;
    }

    public List<Map<String, Object>> queryList(UserAccountFindVO findVO) {
        return accountChangeHistoryMapper.queryChangeHistoryListNewExportList(findVO);
    }

    public int queryCount(UserAccountFindVO findVO) {
        return accountChangeHistoryMapper.queryChangeHistoryListNewExportListCount(findVO);
    }

    /**
     * 导出用户到csv文件
     *
     * @param mapList  导出的数据
     * @param language
     */
    private byte[] exportAccountChangeHistoryCsv(List<Map<String, Object>> mapList, String language) {
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(mapList.size());
        //序号
        int index = 0;
        for (Map<String, Object> vo : mapList) {
            index++;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", "\t" + index + "\t");
            rowData.put("2", "\t" + DateUtil.format(new Date(Long.parseLong(String.valueOf(vo.get("createTime")))), "yyyy-MM-dd HH:mm:ss") + "\t");
            rowData.put("3", "\t" + vo.get("username") + "\t");
            rowData.put("4", "\t" + vo.get("uid") + "\t");
            rowData.put("5", "\t" + vo.get("merchantName") + "\t");
            rowData.put("6", "\t" + (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ?
                    BizTypeEnum.getDescByCode(Integer.valueOf(String.valueOf(vo.get("bizType")))) :
                    BizTypeEnum.getEnByCode(Integer.valueOf(String.valueOf(vo.get("bizType"))))) + "\t");
            rowData.put("7", "\t" + vo.get("remark") + "\t");
            rowData.put("8", ("0".equals(String.valueOf(vo.get("changeType"))) ? "+" : "-") + vo.get("changeAmount"));
            rowData.put("9", vo.get("beforeTransfer"));
            rowData.put("10", vo.get("afterTransfer"));
            rowData.put("11", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "成功" : "Done");
            rowData.put("12", "\t" + ("null".equals(String.valueOf(vo.get("orderNo"))) ? "" : String.valueOf(vo.get("orderNo"))) + "\t");
            rowData.put("13", "\t" +  DateUtil.format(new Date(Long.parseLong(String.valueOf(vo.get("modifyTime")))),"yyyy-MM-dd HH:mm:ss") + "\t");
            rowData.put("14", "\t" + ("null".equals(String.valueOf(vo.get("modifyUser"))) ? "" : String.valueOf(vo.get("modifyUser"))) + "\t");
            //处理手动加款ip
            if(ObjectUtils.isNotEmpty(vo.get("remark")) && vo.get("remark").toString().contains("手动加扣款操作人IP:")){
                    String remark =vo.get("remark").toString();
                    String ip = remark.substring(remark.indexOf("IP:")+3).trim();
                rowData.put("15", "\t" + ("null".equals(ip) ? "" : ip) + "\t");
            }else{
                rowData.put("15", "\t" + ("null".equals(String.valueOf(vo.get("ipAddress"))) ? "" : String.valueOf(vo.get("ipAddress"))) + "\t");
            }
            exportData.add(rowData);
        }

        return CsvUtil.exportCSV(getAccountheader(language), exportData);
    }

    private LinkedHashMap<String, String> getAccountheader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "账变时间");
            header.put("3", "用户名");
            header.put("4", "用户ID");
            header.put("5", "所属商户");
            header.put("6", "账变类型");
            header.put("7", "账变来源");
            header.put("8", "账变金额");
            header.put("9", "账变前余额");
            header.put("10", "账变后余额");
            header.put("11", "账变结果");
            header.put("12", "注单号");
            header.put("13", "操作时间");
            header.put("14", "操作人");
            header.put("15", "IP");
        } else {
            header.put("1", "NO");
            header.put("2", "Time");
            header.put("3", "Player");
            header.put("4", "PlayerId");
            header.put("5", "Merchant");
            header.put("6", "BizType");
            header.put("7", "Source");
            header.put("8", "Amount");
            header.put("9", "BeforeAmount");
            header.put("10", "AfterAmount");
            header.put("11", "Result");
            header.put("12", "TicketsNo");
            header.put("13", "modifyTime");
            header.put("14", "modifyUser");
            header.put("15", "ipAddress");
        }

        return header;
    }
}
