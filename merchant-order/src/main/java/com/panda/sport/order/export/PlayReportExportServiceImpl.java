package com.panda.sport.order.export;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.merchant.MatchBetInfoVO;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import com.panda.sport.order.service.impl.MatchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  玩法报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service("playReportExportServiceImpl")
public class PlayReportExportServiceImpl extends AbstractOrderFileExportService {

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

            log.info("开始玩法投注报表导出 param = {}", merchantFile.getExportParam());

            Map params = JSON.parseObject(merchantFile.getExportParam(), Map.class);
            List<?> returnData = null;
            if (params.get("merchantCodeList") != null) {
                returnData = rpcClient.exportMerchantPlayStatisticList(params);
            } else {
                returnData = rpcClient.exportMatchPlayStatisticList(params);
            }

            if (CollectionUtil.isEmpty(returnData)) {
                throw new Exception("未查询到数据");
            }
            String language = (String) params.get("language");

            inputStream = new ByteArrayInputStream(exportPlayToCsv(returnData, language));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("玩法投注统计报表异常!", e);
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

    /**
     * 设置赛事导出报表格式
     *
     * @param resultList
     * @param language
     * @return
     */
    private byte[] exportPlayToCsv(List<?> resultList, String language) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        List<MatchBetInfoVO> filterList = mapper.convertValue(resultList, new TypeReference<List<MatchBetInfoVO>>() {
        });
        int i = 0;
        for (MatchBetInfoVO order : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", order.getMerchantCode());
            rowData.put("3", order.getMatchInfo());
            rowData.put("4", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? MatchServiceImpl.matchMap.get(order.getMatchStatus()) :
                    MatchServiceImpl.matchENMap.get(order.getMatchStatus()));
            rowData.put("5", DateFormatUtils.format(new Date(order.getBeginTime()), "yyyy-MM-dd HH:mm:ss"));
            rowData.put("6", order.getPlayName());
            rowData.put("7", order.getMarketValue() + " \t");
            rowData.put("8", order.getBetAmount());
            rowData.put("9", order.getOrderAmount());
            rowData.put("10", order.getUserAmount());
            rowData.put("11", order.getUnSettleOrder());
            rowData.put("12", order.getUnSettleAmount());
            rowData.put("13", order.getSettleAmount());
            rowData.put("14", order.getProfit());
            rowData.put("15", order.getProfitRate().multiply(new BigDecimal(100)) + "%");
            rowData.put("16", order.getMatchId());
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getPlayHeader(language), exportData);
    }


}
