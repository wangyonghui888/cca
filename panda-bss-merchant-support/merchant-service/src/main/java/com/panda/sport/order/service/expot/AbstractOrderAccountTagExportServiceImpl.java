package com.panda.sport.order.service.expot;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.panda.sport.backup.mapper.OrderMixExportMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.*;


/**
 * @author :  istio
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.export
 * @Description :  注单导出 二次结算单独处理
 * @Date: 2022-03-26 16:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service("abstractOrderAccountTagExportServiceImpl")
public class AbstractOrderAccountTagExportServiceImpl extends AbstractOrderExportServiceImpl {

    @Autowired
    public OrderMixExportMapper orderMixExportMapper;

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {
        super.execute(merchantFile);
    }

    @Override
    protected byte[] exportOrderToCsv(List<OrderSettle> resultList, String language, String merchantCode) throws Exception {

        List<Map<String, String>> orderSettleInfos = getAmount(resultList);

        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        Integer i = 0;

        for (OrderSettle order : resultList) {
            try {
                if (order.getOrderDetailList() == null || order.getOrderDetailList().size() == 0 || StringUtils.isEmpty(order.getOrderDetailList().get(0).getBetNo())) {
                    continue;
                }

                LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
                i = i + 1;

                Map<String, String> orderSettleMap =
                        Optional
                                .ofNullable(orderSettleInfos)
                                .orElse(Lists.newArrayList())
                                .stream()
                                .filter(orderSettleInfo -> orderSettleInfo.get("orderNo").equals(order.getOrderNo()))
                                .findFirst().orElse(null);

                rowData.put("1", i);
                rowData.put("2", order.getFakeName() + "\t");
                rowData.put("3", "123");
                rowData.put("4", order.getMerchantCode());
                rowData.put("5", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ?
                        CurrencyTypeEnum.optGetDescription(order.getCurrencyCode()) : CurrencyTypeEnum.optGetCurrency(order.getCurrencyCode()));
                rowData.put("6", getObj(order.getProfitAmount()));
                rowData.put("7", getObj(order.getOrderAmountTotal()));
                // 单独添加的字段
                rowData.put("8", orderSettleMap == null ? "" : getObj(orderSettleMap.get("negativeAmount")));
                rowData.put("9", orderSettleMap == null ? "" : getObj(orderSettleMap.get("amount")));

                rowData.put("10", getOrderStatus(order.getOrderStatus(), language));
                rowData.put("11", getObj(order.getCreateTimeStr()));
                rowData.put("12", getObj(order.getSettleTimeStr()));
                rowData.put("13", order.getOrderNo() + "\t");
                rowData.put("14", getSeriesValue(order.getSeriesType(), language));
                rowData.put("15", language.equalsIgnoreCase(LanguageEnum.ZS.getCode()) ?
                        (order.getOutcome() == null ? "未结算" : settleStatusMap.get(order.getOutcome())) :
                        (order.getOutcome() == null ? "unsettled" : settleStatusEnMap.get(order.getOutcome())));
                rowData.put("16", getObj(order.getUid()) + "\t");
                rowData.put("17", getObj(order.getLocalProfitAmount()));
                rowData.put("18", getObj(order.getLocalBetAmount()));
                rowData.put("19", getDeviceType(order.getDeviceType(),language));

                BigDecimal orderValidBetMoney = new BigDecimal(0);

                if (order.getOutcome() != null && (order.getOutcome() == 4 || order.getOutcome() == 5)) {
                    if (order.getProfitAmount() != null) {
                        if (order.getLocalBetAmount().compareTo(order.getProfitAmount()) < 0) {
                            orderValidBetMoney = order.getLocalBetAmount().abs();
                        } else {
                            orderValidBetMoney = order.getProfitAmount().abs();
                        }
                    }
                } else {
                    if (order.getProfitAmount() != null) {
                        orderValidBetMoney = order.getProfitAmount().abs();
                    }
                }

                // 填充有效投注笔数
                order.setSumValidBetNo(CommonDefaultValue.ZERO);
                if (order.getOutcome() != null && (order.getOutcome() == 3 || order.getOutcome() == 4 || order.getOutcome() == 5 || order.getOutcome() == 6)) {
                    order.setSumValidBetNo(CommonDefaultValue.ONE);
                }
                rowData.put("20", getObj(orderValidBetMoney));
                if (order.getOrderDetailList() != null) {
                    if (StringUtils.isEmpty(merchantCode)) {
                        for (int j = 0; j < order.getOrderDetailList().size(); j++) {
                            LinkedHashMap<String, Object> rowDataCopy = new LinkedHashMap<>(rowData);
                            Integer matchType = order.getOrderDetailList().get(j).getMatchType();
                            rowDataCopy.put("21", Objects.equals(order.getSeriesType(), 1) ?
                                    (language.equals(LanguageEnum.ZS.getCode()) ?
                                            (matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) :
                                            (matchType == 1 ? "Prematch" : (matchType == 2 ? "Live" : "Outright"))) :
                                    (language.equals(LanguageEnum.ZS.getCode()) ? "串关" :"Cross connection" ));
                            rowDataCopy.put("22", order.getOrderDetailList().get(j).getSportName());
                            rowDataCopy.put("23", getObj(order.getOrderDetailList().get(j).getTournamentName()));
                            rowDataCopy.put("24", order.getOrderDetailList().get(j).getMatchInfo());
                            rowDataCopy.put("25", getObj(order.getOrderDetailList().get(j).getMatchId()));
                            rowDataCopy.put("26", getObj(order.getOrderDetailList().get(j).getBeginTimeStr()));
                            rowDataCopy.put("27", order.getOrderDetailList().get(j).getPlayName());
                            rowDataCopy.put("28", order.getOrderDetailList().get(j).getPlayOptionName() + "\t");
                            rowDataCopy.put("29", getObj(order.getOrderDetailList().get(j).getMarketType()));
                            rowDataCopy.put("30", getObj(order.getOrderDetailList().get(j).getMarketValue()));
                            rowDataCopy.put("31", getObj(order.getOrderDetailList().get(j).getOddFinally()));
                            rowDataCopy.put("32", getObj(order.getIp()));
                            rowDataCopy.put("33", order.getDeviceImei());
                            int series = order.getSeriesType();
                            if (1 != order.getSeriesType()) {
                                String t = series_zs.get(order.getSeriesType() + "");
                                series = Integer.parseInt(t.split("串")[0]);
                            }
                            rowDataCopy.put("36", order.getMerchantCode());
                            rowDataCopy.put("37", order.getMerchantName());
                            rowDataCopy.put("38", ObjectUtil.equal(order.getUserLevel(), 1) ? (language.equals(LanguageEnum.ZS.getCode()) ? "Vip用户":"VIP user") : (language.equals(LanguageEnum.ZS.getCode()) ? "普通用户":"Ordinary users"));
                            rowDataCopy.put("39", order.getOrderAmountTotal().divide(BigDecimal.valueOf(series), 2, BigDecimal.ROUND_FLOOR));
                            rowDataCopy.put("40", order.getProfitAmount() == null ? "" : order.getProfitAmount().divide(BigDecimal.valueOf(series), 2, BigDecimal.ROUND_FLOOR));
                            rowDataCopy.put("41", getObj(order.getPreBetAmount()));
                            rowDataCopy.put("42", order.getVipUpdateTimeStr());
                            rowDataCopy.put("43", order.getSumValidBetNo());
                            exportData.add(rowDataCopy);
                        }
                    } else {
                        if ("trader".equals(merchantCode)) {
                            Integer matchType = order.getOrderDetailList().get(0).getMatchType();
                            rowData.put("21", Objects.equals(order.getSeriesType(), 1) ?
                                    (language.equals(LanguageEnum.ZS.getCode()) ?
                                            (matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) :
                                            (matchType == 1 ? "Prematch" : (matchType == 2 ? "Live" : "Outright"))) :
                                    (language.equals(LanguageEnum.ZS.getCode()) ? "串关" :"Cross connection" ));
                            rowData.put("22", order.getOrderDetailList().get(0).getSportName());
                            if ("1".equals(String.valueOf(order.getSeriesType()))) {
                                rowData.put("23", getObj(order.getOrderDetailList().get(0).getTournamentName()));
                                rowData.put("24", order.getOrderDetailList().get(0).getMatchInfo());
                                rowData.put("25", getObj(order.getOrderDetailList().get(0).getMatchId()));
                                rowData.put("26", getObj(order.getOrderDetailList().get(0).getBeginTimeStr()));
                                rowData.put("27", order.getOrderDetailList().get(0).getPlayName());
                                rowData.put("28", order.getOrderDetailList().get(0).getPlayOptionName() + "\t");
                                rowData.put("29", getObj(order.getOrderDetailList().get(0).getMarketType()));
                                rowData.put("30", getObj(order.getOrderDetailList().get(0).getMarketValue()));
                                rowData.put("31", getObj(order.getOrderDetailList().get(0).getOddFinally()));
                            } else {
                                rowData.put("23", " ");
                                rowData.put("24", " ");
                                rowData.put("25", " ");
                                rowData.put("26", " ");
                                rowData.put("27", " ");
                                rowData.put("28", " ");
                                rowData.put("29", " ");
                                rowData.put("30", " ");
                                rowData.put("31", " ");
                            }
                            rowData.put("32", getObj(order.getIp()));
                            rowData.put("33", order.getDeviceImei());
                            int series = order.getSeriesType();
                            if (1 != order.getSeriesType()) {
                                String t = series_zs.get(order.getSeriesType() + "");
                                series = Integer.parseInt(t.split("串")[0]);
                            }
                            rowData.put("36", order.getMerchantCode());
                            rowData.put("37", order.getMerchantName());
                            rowData.put("38", ObjectUtil.equal(order.getUserLevel(), 1) ? (language.equals(LanguageEnum.ZS.getCode()) ? "Vip用户":"VIP user") : (language.equals(LanguageEnum.ZS.getCode()) ? "普通用户":"Ordinary users"));
                            rowData.put("39", order.getOrderAmountTotal().divide(BigDecimal.valueOf(series), 2, BigDecimal.ROUND_FLOOR));
                            rowData.put("40", order.getProfitAmount() == null ? "" : order.getProfitAmount().divide(BigDecimal.valueOf(series), 2, BigDecimal.ROUND_FLOOR));
                            rowData.put("41", getObj(order.getPreBetAmount()));
                            rowData.put("42", order.getVipUpdateTimeStr());
                            rowData.put("43", order.getSumValidBetNo());
                        } else {
                            Integer matchType = order.getOrderDetailList().get(0).getMatchType();
                            rowData.put("21", Objects.equals(order.getSeriesType(), 1) ?
                                    (language.equals(LanguageEnum.ZS.getCode()) ?
                                            (matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) :
                                            (matchType == 1 ? "Prematch" : (matchType == 2 ? "Live" : "Outright"))) :
                                    (language.equals(LanguageEnum.ZS.getCode()) ? "串关" :"Cross connection" ));
                            rowData.put("22", order.getOrderDetailList().get(0).getSportName());
                            rowData.put("23", getObj(order.getOrderDetailList().get(0).getTournamentName()));
                            rowData.put("24", order.getOrderDetailList().get(0).getMatchInfo());
                            rowData.put("25", getObj(order.getOrderDetailList().get(0).getMatchId()));
                            rowData.put("26", getObj(order.getOrderDetailList().get(0).getBeginTimeStr()));
                            rowData.put("27", order.getOrderDetailList().get(0).getPlayName());
                            rowData.put("28", order.getOrderDetailList().get(0).getPlayOptionName() + "\t");
                            rowData.put("29", getObj(order.getOrderDetailList().get(0).getMarketType()));
                            rowData.put("30", getObj(order.getOrderDetailList().get(0).getMarketValue()));
                            rowData.put("31", getObj(order.getOrderDetailList().get(0).getOddFinally()));
                            rowData.put("32", getObj(order.getIp()));
                            rowData.put("33", order.getDeviceImei());
                            rowData.put("34", order.getVipUpdateTimeStr());
                            rowData.put("35", order.getSumValidBetNo());
                            rowData.put("41", getObj(order.getPreBetAmount()));
                        }
                        exportData.add(rowData);
                    }
                } else {
                    rowData.put("21", " ");
                    rowData.put("22", " ");
                    rowData.put("23", " ");
                    rowData.put("24", " ");
                    rowData.put("25", " ");
                    rowData.put("26", " ");
                    rowData.put("27", " ");
                    rowData.put("28", " ");
                    rowData.put("29", " ");
                    rowData.put("30", " ");
                    rowData.put("31", " ");
                    rowData.put("32", getObj(order.getIp()));
                    rowData.put("33", order.getDeviceImei());
                    rowData.put("34", order.getVipUpdateTimeStr());
                    rowData.put("35", order.getSumValidBetNo());
                    rowData.put("41", getObj(order.getPreBetAmount()));
                    exportData.add(rowData);
                }

            } catch (Exception e) {
                log.error("数据异常！data = {}", JSON.toJSONString(order), e);
                //throw new Exception("数据异常！" + JSON.toJSONString(order));
            }
        }
        if (StringUtils.isEmpty(merchantCode) || "trader".equals(merchantCode)) {
            return CsvUtil.exportCSV(getHeaderManage(language), exportData);
        } else {
            return CsvUtil.exportCSV(getHeader(language), exportData);
        }
    }

    private List<Map<String, String>> getAmount(List<OrderSettle> resultList) {
        List<String> orderNoList = resultList.stream().map(OrderSettle::getOrderNo).collect(Collectors.toList());
        return orderMixExportMapper.queryAmountByOrderSettleTimesInfo(orderNoList);
    }

    /**
     * 对内模板
     */
    private LinkedHashMap<String, String> getHeaderManage(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "用户名称");
            header.put("3", "平台名称");
            header.put("4", "商户名称");
            header.put("5", "用户币种");
            header.put("6", "盈亏");
            header.put("7", "下注金额");
            header.put("8", "负帐金额");
            header.put("9", "当前用户余额");

            header.put("10", "注单状态");
            header.put("11", "投注时间");
            header.put("12", "结算时间");
            header.put("13", "注单号");
            header.put("14", "串关值");
            header.put("15", "结算状态");
            header.put("16", "用户ID");
            header.put("17", "盈亏(RMB)");
            header.put("18", "下注金额(RMB)");

            header.put("19", "设备信息");
            header.put("20", "有效注额");
            header.put("21", "注单类型");
            header.put("22", "赛种");
            header.put("23", "联赛名称");
            header.put("24", "比赛对阵");
            header.put("25", "赛事ID");
            header.put("26", "开赛时间");
            header.put("27", "玩法名称");
            header.put("28", "投注项名称");
            header.put("29", "盘口类型");
            header.put("30", "盘口值");
            header.put("31", "赔率");
            header.put("32", "ip");
            header.put("33", "设备号");

            header.put("36", "商户代号");
            header.put("37", "商户名称");
            header.put("38", "团体式");
            header.put("39", "串关投注金额");
            header.put("40", "串关盈利");
            header.put("41", "提前结算金额");
            header.put("42", "vip升级时间");
            header.put("43", "有效投注笔数");
        } else {
            header.put("1", "NO");
            header.put("2", "User");
            header.put("3", "Merchant");
            header.put("4", "MerchantName");
            header.put("5", "Currency");
            header.put("6", "Sport");
            header.put("7", "BetAmount");

            header.put("8", "NegativeAmount");
            header.put("9", "CurrentAmount");

            header.put("10", "MarketType");
            header.put("11", "BetTime");
            header.put("12", "MatchBegin");
            header.put("13", "BetNo");
            header.put("14", "SeriesValue");
            header.put("15", "Outcome");
            header.put("16", "MatchId");
            header.put("17", "Profit(RMB)");
            header.put("18", "BetAmount(RMB)");

            header.put("19", "device");
            header.put("20", "orderValidBetMoney");
            header.put("21", "BetType");
            header.put("22", "sport");
            header.put("23", "Tournament");
            header.put("24", "MatchInfo");
            header.put("25", "matchId");
            header.put("26", "starTime");
            header.put("27", "PlayName");
            header.put("28", "PlayOption");
            header.put("29", "MarketValue");
            header.put("30", "Odds");
            header.put("31", "BetStatus");
            header.put("32", "ip");
            header.put("33", "IMEI");

            header.put("36", "merchantCode");
            header.put("37", "merchantName");
            header.put("38", "userLevel");
            header.put("39", "seriesAmount");
            header.put("40", "seriesProfit");
            header.put("41", "PreBetAmount(RMB)");
            header.put("42", "vipTime");
            header.put("43", "sumValidBetNo");
        }
        return header;
    }

    /**
     * 对外模板
     */
    private LinkedHashMap<String, String> getHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "用户名称");
            header.put("3", "平台名称");
            header.put("4", "商户名称");
            header.put("5", "用户币种");
            header.put("6", "盈亏");
            header.put("7", "下注金额");
            header.put("8", "负帐金额");
            header.put("9", "当前用户余额");
            header.put("10", "注单状态");
            header.put("11", "投注时间");
            header.put("12", "结算时间");
            header.put("13", "注单号");
            header.put("14", "串关值");
            header.put("15", "结算状态");
            header.put("16", "用户ID");
            header.put("17", "盈亏(RMB)");
            header.put("18", "下注金额(RMB)");
            header.put("19", "设备信息");
            header.put("20", "有效注额");
            header.put("21", "注单类型");
            header.put("22", "赛种");
            header.put("23", "联赛名称");
            header.put("24", "比赛对阵");
            header.put("25", "赛事ID");
            header.put("26", "开赛时间");
            header.put("27", "玩法名称");
            header.put("28", "投注项名称");
            header.put("29", "盘口类型");
            header.put("30", "盘口值");
            header.put("31", "赔率");
            header.put("32", "ip");
            header.put("33", "设备号");
            header.put("34", "vip升级时间");
            header.put("35", "有效投注笔数");
            header.put("41", "提前结算金额");
        } else {
            header.put("1", "NO");
            header.put("2", "User");
            header.put("3", "Merchant");
            header.put("4", "MerchantName");
            header.put("5", "Currency");
            header.put("6", "Sport");
            header.put("7", "BetAmount");
            header.put("8", "NegativeAmount");
            header.put("9", "CurrentAmount");
            header.put("10", "MarketType");
            header.put("11", "BetTime");
            header.put("12", "MatchBegin");
            header.put("13", "BetNo");
            header.put("14", "SeriesValue");
            header.put("15", "Outcome");
            header.put("16", "MatchId");
            header.put("17", "Profit(RMB)");
            header.put("18", "BetAmount(RMB)");
            header.put("19", "device");
            header.put("20", "orderValidBetMoney");
            header.put("21", "BetType");
            header.put("22", "sport");
            header.put("23", "Tournament");
            header.put("24", "MatchInfo");
            header.put("25", "matchId");
            header.put("26", "starTime");
            header.put("27", "PlayName");
            header.put("28", "PlayOption");
            header.put("29", "MarketValue");
            header.put("30", "Odds");
            header.put("31", "BetStatus");
            header.put("32", "ip");
            header.put("33", "IMEI");
            header.put("34", "vipTime");
            header.put("35", "sumValidBetNo");
            header.put("41", "PreBetAmount(RMB)");

        }
        return header;
    }

    private String getSeriesValue(Integer seriesType, String language) {
        return !language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? series_en.get(seriesType + "") : series_zs.get(seriesType + "");
    }

    private String getDeviceType(String deviceType,String language) {
        switch (String.valueOf(deviceType)) {
            case "1":
                return "H5";
            case "2":
                return "PC";
            case "3":
                return "Android";
            case "4":
                return "IOS";
            default:
                return (language.equals(LanguageEnum.ZS.getCode()) ? "未知" :"unknown" );
        }
    }
}
