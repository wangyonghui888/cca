package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.merchant.MatchBetInfoVO;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
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
import java.util.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  赛事报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("matchReportExportServiceImpl")
@Slf4j
public class MatchReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient rpcClient;

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

            log.info("开始赛事投注报表导出 param = {}",merchantFile.getExportParam());


            SportVO sportVO = JSON.parseObject(merchantFile.getExportParam(), SportVO.class);
            List<?> resultList = null;
            Map<String,Object> result = null;
            if (CollectionUtils.isEmpty(sportVO.getMerchantCodeList())) {
                result = rpcClient.queryMatchStatisticList(sportVO);
            } else {
                result = rpcClient.queryMerchantMatchStatisticList(sportVO);
            }

            if (result != null) {
                resultList = (List<?>) result.get("list");
            }

            if (CollectionUtils.isEmpty(resultList)) {
                throw new Exception("未查询到数据");
            }

            inputStream =  new ByteArrayInputStream(exportMatchToCsv(resultList));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}" ,(System.currentTimeMillis() - startTime));
        }catch (Exception e){
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("赛事投注统计报表异常!", e);
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
     * 设置赛事导出报表格式
     * @param resultList
     * @return
     */
    private byte[] exportMatchToCsv(List<?> resultList) {
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
            rowData.put("4", order.getSportName());
            rowData.put("5", MatchServiceImpl.tournamentLevelMap.get(order.getTournamentLevel()));
            rowData.put("6", MatchServiceImpl.matchMap.get(order.getMatchStatus()));
            rowData.put("7", DateFormatUtils.format(new Date(order.getBeginTime()), "yyyy-MM-dd HH:mm:ss"));
            rowData.put("8", order.getBetAmount());
            rowData.put("9", order.getOrderAmount());
            rowData.put("10", order.getUserAmount());
            rowData.put("11", order.getUnSettleOrder());
            rowData.put("12", order.getUnSettleAmount());
            rowData.put("13", order.getSettleAmount());
            rowData.put("14", order.getProfit());
            rowData.put("15", order.getProfitRate() + "%");
            rowData.put("16", order.getPlayAmount());
            rowData.put("17", order.getParlayVaildBetAmount());
            rowData.put("18", order.getParlayValidTickets());
            rowData.put("19", order.getParlayProfit());
            rowData.put("20", order.getParlayProfitRate());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "序号");
        header.put("2", "商户名称");
        header.put("3", "赛事对阵");
        header.put("4", "赛种");
        header.put("5", "赛事等级");
        header.put("6", "赛事状态");
        header.put("7", "开赛时间");
        header.put("8", "投注金额");
        header.put("9", "投注笔数");
        header.put("10", "投注用户数");
        header.put("11", "未结算笔数");
        header.put("12", "未结算金额");
        header.put("13", "派彩金额");
        header.put("14", "盈利金额");
        header.put("15", "盈利率");
        header.put("16", "玩法数量");
        header.put("17", "串关投注额");
        header.put("18", "串关注单数");
        header.put("19", "串关盈利额");
        header.put("20", "串关盈利率");
        return com.panda.sport.merchant.common.utils.CsvUtil.exportCSV(header, exportData);
    }

}
