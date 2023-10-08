package com.panda.sport.merchant.manage.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.backup.mapper.BackupOrderMixMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.OrderMapper;
import com.panda.sport.backup.mapper.OrderMixExportMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.DateTimeUtils;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.panda.sport.merchant.common.constant.Constant.UTC8;

@Slf4j
@Service("abstractFinanceService")
public abstract class AbstractFinanceService {

    @Autowired
    protected OrderMapper orderMapper;
    @Autowired
    protected BackupOrderMixMapper orderMixMapper;
    @Autowired
    protected OrderMixExportMapper orderMixExportMapper;
    @Autowired
    protected MerchantMapper merchantMapper;

    /**
     * 日对账单导出list
     */
    protected void exportFinanceDay(HttpServletResponse response, MerchantFinanceDayVo dayVo, String language) {
        List<?> resultList = this.financeDayExportListQuery(dayVo, language);
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }
        try {
            String fileName = "merchant" + dayVo.getFinanceDayId().split("-")[dayVo.getFinanceDayId().split("-").length - 1] + "dayBill.csv";
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            FileCopyUtils.copy(exportFinanceDayCsv(resultList, language), response.getOutputStream());
        } catch (Exception e) {
            log.error("导出商户报表异常!", e);
        }
    }

    /**
     * 日对账单导出list
     */
    private List<Map<String, String>> financeDayExportListQuery(MerchantFinanceDayVo dayVo, String language) {
        String time = dayVo.getFinanceDayId().substring(0, 10);
        String merchantCode = dayVo.getFinanceDayId().substring(11);
        Long merchantId = merchantMapper.getMerchantId(merchantCode);
        if (null == merchantId) {
            return null;
        }

        long oneDay = 24 * 60 * 60 * 1000;
        long half = oneDay / 2;
        long createTime = DateTimeUtils.convertString2Date("yyyy-MM-dd", time).getTime();
        long endTime = createTime;
        if (UTC8.equals(dayVo.getTimeZone())) {
            //自然日
            endTime = endTime + oneDay - 1;
        } else {
            //账务日
            createTime = createTime + half;
            endTime = endTime + oneDay + half - 1;
        }
        if ("3".equals(dayVo.getFilter())) {
            return orderMixExportMapper.queryOrderExportSettleList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency());
        } else {
            return orderMixExportMapper.queryOrderExportList(createTime, endTime, merchantId, dayVo.getManagerCode(), language, dayVo.getVipLevel(), dayVo.getCurrency());
        }
    }

    /**
     * 导出用户到csv文件
     * String.valueOf不能删除
     *
     * @param mapList  导出的数据
     * @param language
     */
    private byte[] exportFinanceDayCsv(List<?> mapList, String language) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> filterList = mapper.convertValue(mapList, new TypeReference<List<Map<String, String>>>() {
        });
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(mapList.size());
        for (Map<String, String> map : filterList) {
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", map.get("username"));
            rowData.put("2", map.get("platform_name"));
            rowData.put("3", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? map.get("country_zh") : map.get("country_en"));
            rowData.put("4", map.get("sport_name"));
            rowData.put("5", map.get("profit_amount"));
            rowData.put("6", map.get("order_amount_total"));
            rowData.put("7", map.get("market_type"));
            rowData.put("8", map.get("market_value"));
            rowData.put("9", map.get("odds_value"));
            rowData.put("10", map.get("order_status") + "\t");
            rowData.put("11", map.get("create_time") + "\t");
            rowData.put("12", map.get("begin_time") + "\t");
            rowData.put("13", map.get("settle_time") + "\t");
            rowData.put("14", map.get("order_no") + "\t");
            rowData.put("15", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? map.get("series_value") : getSeriesValue(map.get("series_type")));
            rowData.put("16", map.get("settle_status"));
            rowData.put("17", map.get("match_id"));
            rowData.put("18", map.get("match_name"));
            rowData.put("19", map.get("match_info"));
            rowData.put("20", map.get("match_type"));
            rowData.put("21", map.get("play_option_name") + "\t");
            rowData.put("22", map.get("play_name"));
            rowData.put("23", map.get("uid") + "\t");
//            rowData.put("1", new BigDecimal(dayVo.getOrderNo()).toPlainString() + "\t");
            exportData.add(rowData);
        }


        return CsvUtil.exportCSV(getHeader(language), exportData);
    }

    private String getSeriesValue(String seriesType) {
        if (seriesType.equals("1")) {
            return "single";
        } else {
            return "parlay";
        }
    }

    private LinkedHashMap<String, String> getHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "用户名称");
            header.put("2", "平台名称");
            header.put("3", "用户币种");
            header.put("4", "赛种");
            header.put("5", "盈亏");
            header.put("6", "下注金额");
            header.put("7", "盘口类型");
            header.put("8", "盘口值");
            header.put("9", "赔率");
            header.put("10", "注单状态");
            header.put("11", "下注时间");
            header.put("12", "开赛时间");
            header.put("13", "结算时间");
            header.put("14", "注单号");
            header.put("15", "串关值");
            header.put("16", "结算状态");
            header.put("17", "赛事ID");
            header.put("18", "联赛名称");
            header.put("19", "比赛对阵");
            header.put("20", "注单类型");
            header.put("21", "投注项名称");
            header.put("22", "玩法名称");
            header.put("23", "用户ID");
        } else {
            header.put("1", "Player");
            header.put("2", "Platform");
            header.put("3", "currency");
            header.put("4", "Sport");
            header.put("5", "Profit");
            header.put("6", "BetAmount");
            header.put("7", "MarketType");
            header.put("8", "Market");
            header.put("9", "Odds");
            header.put("10", "Status");
            header.put("11", "BetTime");
            header.put("12", "BeginTime");
            header.put("13", "SettleTime");
            header.put("14", "TicketNo");
            header.put("15", "Series");
            header.put("16", "SettleResult");
            header.put("17", "MatchId");
            header.put("18", "Tournament");
            header.put("19", "MatchInfo");
            header.put("20", "TicketType");
            header.put("21", "PlayOptions");
            header.put("22", "PlayName");
            header.put("23", "PlayerId");
        }

        return header;
    }
}
