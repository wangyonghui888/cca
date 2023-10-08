package com.panda.sport.admin.export;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  商户报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("merchantReportExportServiceImpl")
@Slf4j
public class MerchantReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient rpcClient;

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            super.updateFileStart(merchantFile.getId());
            log.info("开始商户报表导出 param = {}", merchantFile.getExportParam());
            JSONObject jsonObject = JSON.parseObject(merchantFile.getExportParam());
            String language = jsonObject.getString("language");
            MerchantOrderVO requestVO = JSON.parseObject(merchantFile.getExportParam(), MerchantOrderVO.class);
            List<?> returnData = rpcClient.merchantDataQuery(requestVO);
            if (CollectionUtils.isEmpty(returnData)) {
                log.info("exportMerchantReport returnDate:is null");
                throw new Exception("未查询到数据");
            }
            inputStream = new ByteArrayInputStream(exportMerchantReportToCsv(returnData, requestVO.getFilter(), language));
            super.uploadFile(merchantFile, inputStream);
            log.info(language + ",导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("exportMerchantReport导出商户报表异常!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("流关闭异常！");
                }
            }
        }
    }

    private byte[] exportMerchantReportToCsv(List<?> resultList, String filter, String language) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        int i = 0;
        ObjectMapper mapper = new ObjectMapper();
        List<MerchantOrderDayPO> filterList = mapper.convertValue(resultList, new TypeReference<List<MerchantOrderDayPO>>() {
        });
        for (MerchantOrderDayPO order : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", order.getTime() + " ");
            rowData.put("3", " " + order.getMerchantName() + " ");
            rowData.put("4", order.getMerchantLevel() == null ? "" : order.getMerchantLevel() + " ");
            if ("1".equals(filter)) {
                rowData.put("5", order.getValidBetAmount());
                rowData.put("6", order.getValidTickets());
                rowData.put("7", order.getReturnAmount());
                rowData.put("8", order.getProfit());
                rowData.put("9", order.getProfitRate() + "%");
                rowData.put("10", order.getBetSettledUsers() == null ? 0 : order.getBetSettledUsers());
                rowData.put("11", order.getAddUser());
                rowData.put("12", order.getRegisterTotalUserSum());
                rowData.put("13", order.getBetUserRate() + "%");
            } else if ("2".equals(filter)) {
                rowData.put("5", order.getLiveBetAmount());
                rowData.put("6", order.getLiveOrderNum());
                rowData.put("7", order.getLiveReturn());
                rowData.put("8", order.getLiveProfit());
                rowData.put("9", order.getLiveProfitRate() + "%");
                rowData.put("10", order.getLiveUsers() == null ? 0 : order.getLiveUsers());
                rowData.put("11", order.getAddUser());
                rowData.put("12", order.getRegisterTotalUserSum());
                rowData.put("13", order.getLiveUserRate() + "%");
            } else {
                rowData.put("5", order.getSettleBetAmount());
                rowData.put("6", order.getSettleOrderNum());
                rowData.put("7", order.getSettleReturn());
                rowData.put("8", order.getSettleProfit());
                rowData.put("9", order.getSettleProfitRate() + "%");
                rowData.put("10", order.getSettleUsers() == null ? 0 : order.getSettleUsers());
                rowData.put("11", order.getAddUser());
                rowData.put("12", order.getRegisterTotalUserSum());
                rowData.put("13", order.getSettileUserRate() + "%");
            }
            exportData.add(rowData);
        }

        return CsvUtil.exportCSV(getMerchantHeader(language), exportData);
    }

    private LinkedHashMap<String, String> getMerchantHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if(language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)){
            header.put("1", "序号");
            header.put("2", "日期");
            header.put("3", "商户名称");
            header.put("4", "商户等级");
            header.put("5", "投注金额");
            header.put("6", "投注笔数");
            header.put("7", "派彩金额");
            header.put("8", "平台盈利金额");
            header.put("9", "盈利率");
            header.put("10", "投注用户数");
            header.put("11", "新增用户数");
            header.put("12", "注册用户数");
            header.put("13", "投注率");
        }else {
            header.put("1", "NO");
            header.put("2", "Date");
            header.put("3", "Merchant");
            header.put("4", "Level");
            header.put("5", "BetAmount");
            header.put("6", "Tickets");
            header.put("7", "Return");
            header.put("8", "Profit");
            header.put("9", "ProfitRate");
            header.put("10", "BetUsers");
            header.put("11", "NewUsers");
            header.put("12", "AllUsers");
            header.put("13", "BetRate");
        }
        return header;
    }

}
