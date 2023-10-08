package com.panda.sport.admin.export;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  javier
 * --------  ---------  --------------------------
 */
@Service("groupByUserReportExportServiceImpl")
@Slf4j
public class GroupByUserReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient userReportClient;

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

            log.info("开始用户投注报表导出 param = {}", merchantFile.getExportParam());
            UserOrderVO vo = JSON.parseObject(merchantFile.getExportParam(), UserOrderVO.class);

            final Map<String, Object> result = (Map<String, Object>) userReportClient.listUserBetGroupByUser(vo).getData();

            if (CollectionUtils.isEmpty((List<?>) result.get("list"))) {
                log.info("exportMerchantReport returnDate:is null");
                throw new Exception("未查询到数据");
            }
            inputStream = new ByteArrayInputStream(groupByExportUsersToCsv((List<?>) result.get("list"), vo));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("用户投注统计报表异常!", e);
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
     * 导出用户到csv文件
     *
     * @param resultList 导出的数据（用户）
     */
    protected static byte[] groupByExportUsersToCsv(List<?> resultList, UserOrderVO vo) {
        String filter = vo.getFilter();
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        List<UserOrderDayPO> filterList = mapper.convertValue(resultList, new TypeReference<List<UserOrderDayPO>>() {
        });
        int i = 0;
        for (UserOrderDayPO orderPO : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", "\t" + orderPO.getUserId());
            rowData.put("3", orderPO.getUserName());
            rowData.put("4", vo.getStartTime() + "to" + vo.getEndTime());
            rowData.put("5", orderPO.getMerchantCode());
            BigDecimal validMoney;
            if ("1".equals(filter)) {
                rowData.put("6", orderPO.getBetNum());
                rowData.put("7", orderPO.getBetAmount());
                rowData.put("8", orderPO.getProfit());
                rowData.put("9", orderPO.getProfitRate() + "%");
                validMoney = orderPO.getOrderValidBetMoney();
            } else if ("2".equals(filter)) { // 开赛时
                rowData.put("6", orderPO.getLiveOrderNum());
                rowData.put("7", orderPO.getLiveOrderAmount());
                rowData.put("8", orderPO.getLiveProfit());
                rowData.put("9", orderPO.getLiveProfitRate() + "%");
                validMoney = orderPO.getOrderValidBetMoney();
            } else {
                rowData.put("6", orderPO.getSettleOrderNum());
                rowData.put("7", orderPO.getSettleOrderAmount());
                rowData.put("8", orderPO.getSettleProfit());
                rowData.put("9", orderPO.getSettleProfitRate() + "%");
                validMoney = orderPO.getSettleValidBetMoney();
            }
            rowData.put("10", orderPO.getActiveDays());

            rowData.put("11", validMoney);
            rowData.put("12", orderPO.getOrderValidBetCount());
            //修改用户投注统计有效投注笔数取值
           // rowData.put("12", "1".equals(filter) ? orderPO.getValidTickets() : orderPO.getTicketSettled());
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getUserHeader(vo.getLanguage()), exportData);
    }

}
