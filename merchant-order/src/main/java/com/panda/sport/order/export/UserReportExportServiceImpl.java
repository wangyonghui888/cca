package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.order.feign.MerchantReportClient;
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
 * @Description :  用户报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("userReportExportServiceImpl")
@Slf4j
public class UserReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient userReportClient;

    @Override
    @Async()
    public void export(MerchantFile merchantFile){

        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            if (super.checkTask(merchantFile.getId())){
                log.info("当前任务被删除！");
                return;
            }
            super.updateFileStart(merchantFile.getId());

            log.info("开始用户投注报表导出 param = {}",merchantFile.getExportParam());
            UserOrderVO vo  = JSON.parseObject(merchantFile.getExportParam(), UserOrderVO.class);

            List<?> list = userReportClient.queryUserReport(vo);

            if (CollectionUtils.isEmpty(list)) {
                log.info("exportMerchantReport returnDate:is null");
                throw new Exception("未查询到数据");
            }
            inputStream =  new ByteArrayInputStream(exportUsersToCsv(list, vo.getFilter()));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}" ,(System.currentTimeMillis() - startTime));
        }catch (Exception e){
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("用户投注统计报表异常!", e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    log.error("流关闭异常！");
                }
            }
        }
    }

    /**
     * 导出用户到csv文件
     *
     * @param resultList 导出的数据（用户）
     * @return
     */
    protected byte[] exportUsersToCsv(List<?> resultList, String filter) {
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
            rowData.put("3", "********");
            rowData.put("4", orderPO.getTime());
            rowData.put("5", orderPO.getMerchantCode());
            if ("1".equals(filter)) {
                rowData.put("6", orderPO.getBetNum());
                rowData.put("7", orderPO.getBetAmount());
                rowData.put("8", orderPO.getProfit());
                rowData.put("9", orderPO.getProfitRate() + "%");
            } else if ("2".equals(filter)) {
                rowData.put("6", orderPO.getLiveOrderNum());
                rowData.put("7", orderPO.getLiveOrderAmount());
                rowData.put("8", orderPO.getLiveProfit());
                rowData.put("9", orderPO.getLiveProfitRate() + "%");
            } else {
                rowData.put("6", orderPO.getSettleOrderNum());
                rowData.put("7", orderPO.getSettleOrderAmount());
                rowData.put("8", orderPO.getSettleProfit());
                rowData.put("9", orderPO.getSettleProfitRate() + "%");
            }
            rowData.put("10", orderPO.getActiveDays());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "序号");
        header.put("2", "用户ID");
        header.put("3", "用户名");
        header.put("4", "日期");
        header.put("5", "所属商户");
        header.put("6", "投注笔数");
        header.put("7", "投注金额");
        header.put("8", "盈亏金额");
        header.put("9", "盈亏比例");
        header.put("10", "活跃天数");
        return CsvUtil.exportCSV(header, exportData);
    }
}
