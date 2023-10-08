package com.panda.sport.order.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.panda.sport.backup.mapper.BackupTAccountChangeHistoryMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.OrderMapper;

import com.panda.sport.merchant.common.enums.BizTypeEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.FtpUtil;
import com.panda.sport.merchant.mapper.OrderSettleDetailMapper;
import com.panda.sport.merchant.mapper.OrderSettleMapper;
import com.panda.sport.order.service.AccountRecordService;
import com.panda.sport.order.service.expot.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountRecordServiceImpl implements AccountRecordService {

    @Autowired
    OrderSettleMapper orderSettleMapper;

    @Autowired
    OrderSettleDetailMapper orderSettleDetailMapper;


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private FtpProperties ftpProperties;


    @Autowired
    private BackupTAccountChangeHistoryMapper accountChangeHistoryMapper;


    /**
     * 获取账变 交易记录
     *
     * @return
     */
    @Override
    
    public void accountRecordUpload(long startTime, long endTime) throws IOException {
        Date executeDate = new Date(startTime);
        // 获取所有商户信息
        List<MerchantPO> merchantList = merchantMapper.queryMerchantTree();
        if (merchantList.size() <= 0) {
            log.info("结算时间:{},没有商户:{}", DateUtil.lastWeek(), merchantList.size());
            return;
        }
        // 分组map
        Map<String, MerchantPO> merchantMap = merchantList.stream().collect(Collectors.toMap(MerchantPO::getMerchantCode, merchantPO -> merchantPO));
        // 父类组装
        Map<String, MerchantPO> parentMerchantMap = new HashMap<>();
        List<String> parentIdList = merchantList.stream().map(MerchantPO::getParentId).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        if (parentIdList.size() > 0) {
            List<MerchantPO> parentMerchantList = merchantMapper.getMerchantInMerchantId(new HashSet<>(parentIdList));
            if (parentMerchantList.size() > 0) {
                parentMerchantMap = parentMerchantList.stream().collect(Collectors.toMap(MerchantPO::getId, merchantPO -> merchantPO));
            }
        }
        int pageSize = 1000;
        List<String> merchantCodeList = merchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
        for (String merchantCode : merchantCodeList) {
            int pageNo = 1;
            MerchantPO merchantPO = merchantMap.getOrDefault(merchantCode, null);
            log.info("merchantCode:" + merchantCode + ",merchantPO:" + merchantPO.getId() + ",parentId:" + merchantPO.getParentId() + ",agentLevel:" + merchantPO.getAgentLevel());
            int total = accountChangeHistoryMapper.countChangeHistory(merchantCode, startTime, endTime);
            if (total > 0) {
                List<Map<String, Object>> orderSettleList = new ArrayList<>();
                int n = (total % pageSize) == 0 ? (total / pageSize) : (total / pageSize + 1);
                for (int i = 0; i < n; i++) {
                    int start = (pageNo - 1) * pageSize;
                    log.info("queryAccountChangeList:" + merchantCode + "," + startTime + "," + endTime + "," + start + "," + pageSize);
                    List<Map<String, Object>> changeVoList = accountChangeHistoryMapper.queryAccountChangeList(merchantCode, startTime, endTime, start, pageSize);
                    orderSettleList.addAll(changeVoList);
                    pageNo += 1;
                }
                String parentCode = parentMerchantMap.get(merchantPO.getParentId()).getMerchantCode();
                String dir = "";
                if (merchantPO.getAgentLevel() == 0 || merchantPO.getAgentLevel() == 1) {
                    //直营商户panda_order_694262
                    dir = "panda_order_" + merchantCode + "/" + DateFormatUtils.format(executeDate, "yyyy-MM");
                } else if (merchantPO.getAgentLevel() == 2) {
                    // 二级商户
                    dir = "panda_order_" + parentCode + "/panda_order_" + merchantCode + "/" + DateFormatUtils.format(executeDate, "yyyy-MM");
                }
                log.info("开始上传商户CODE:{},商户名称:{},执行时间:{},记录数:{},total:{},parentCode:{},dir:{}",
                        merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate, orderSettleList.size(), total, parentCode, dir);
                writeIntoJsonFile(executeDate, merchantPO, dir, orderSettleList);
                writeIntoCSVFile(executeDate, merchantPO, dir, orderSettleList);
            }
        }
    }

    /**
     * 写入文件
     *
     * @param merchantPO
     * @param betApiVoList
     * @paramjava.io.IOException: 用户名或密码不正确。
     */
    private void writeIntoJsonFile(Date executeDate, MerchantPO merchantPO, String dir, List<Map<String, Object>> betApiVoList) throws IOException {
        String fileName = DateFormatUtils.format(executeDate, "yyyyMMdd HH") + "_account_change.json";
        InputStream inputStream = null;
        try {
            File file = new File(fileName);
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(JSONObject.toJSONString(betApiVoList));
            write.flush();
            write.close();
            inputStream = new FileInputStream(file);
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", dir, fileName, inputStream);
            log.info("json商户CODE:{},商户名称:{},结算时间:{},ftp写入成功", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate);
        } catch (Exception e) {
            log.info("json商户CODE:{},商户名称:{},结算时间:{},ftp写入失败", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), executeDate);
            log.error("写入文件失败!", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private void writeIntoCSVFile(Date executeDate, MerchantPO merchantPO, String dir, List<Map<String, Object>> resultList) throws IOException {
        String fileName = DateFormatUtils.format(executeDate, "yyyyMMdd HH") + "_account_change.csv";
        InputStream inputStream = null;
        try {
            byte[] bytes = exportOrderAccountHistoryToCsv(resultList);
            //String decoded = new String(bytes, "UTF-8");
            File tempFile = File.createTempFile(DateFormatUtils.format(executeDate, "yyyyMMdd"), ".csv");
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
                    log.error("关闭流异常!", var8);
                }
            }
            //File file = new File(CSV_PATH + fileName);
            inputStream = new FileInputStream(tempFile);
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", dir, fileName, inputStream);
            log.info("csv:商户CODE:{},商户名称:{},结算时间:{},ftp写入成功", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), DateUtil.lastWeek());
        } catch (Exception e) {
            log.info("csv:商户CODE:{},商户名称:{},结算时间:{},ftp写入失败", merchantPO.getMerchantCode(), merchantPO.getMerchantName(), DateUtil.lastWeek());
            log.error("写入文件失败!", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private byte[] exportOrderAccountHistoryToCsv(List<Map<String, Object>> mapList) {
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
            rowData.put("5", "\t" + vo.get("merchantCode") + "\t");
            rowData.put("6", "\t" + BizTypeEnum.getDescByCode(Integer.valueOf(String.valueOf(vo.get("bizType")))) + "\t");
            rowData.put("7", "\t" + vo.get("remark") + "\t");
            rowData.put("8", ("0".equals(String.valueOf(vo.get("changeType"))) ? "+" : "-") + vo.get("changeAmount"));
            rowData.put("9", vo.get("beforeTransfer"));
            rowData.put("10", vo.get("afterTransfer"));
            rowData.put("11", "\t" + "成功" + "\t");
            rowData.put("12", "\t" + ("null".equals(String.valueOf(vo.get("orderNo"))) ? "" : String.valueOf(vo.get("orderNo"))) + "\t");
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
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
        return CsvUtil.exportCSV(header, exportData);
    }
}