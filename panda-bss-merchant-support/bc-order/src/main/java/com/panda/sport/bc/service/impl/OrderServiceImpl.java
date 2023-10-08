package com.panda.sport.bc.service.impl;

import com.google.common.collect.Lists;
import com.panda.sport.bc.service.OrderService;
import com.panda.sport.backup.mapper.SportMapper;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.po.bss.SportPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import com.panda.sport.order.service.AbstractOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.panda.sport.merchant.common.constant.Constant.settleStatusEnMap;
import static com.panda.sport.merchant.common.constant.Constant.settleStatusMap;

/**
 * @ClassName OrderServiceImpl
 * @Description 注单实现
 * @Date 2020-09-05 20:37
 * @Version 1
 */
@Slf4j
@Service
public class OrderServiceImpl extends AbstractOrderService implements OrderService {

    @Value("${database.switch:1}")
    private String databaseSwitch;

    @Resource
    private SportMapper sportMapper;

    private final static List<String> CN_TITLE = Lists.newArrayList("序号", "用户名称", "用户币种", "赛种", "盈亏", "下注金额", "盘口类型", "盘口值",
            "赔率", "注单状态", "下注时间", "开赛时间", "结算时间", "注单号", "结算状态", "赛事ID", "联赛名称", "比赛对阵", "注单类型", "玩法名称", "用户ID");

    private final static List<String> EN_TITLE = Lists.newArrayList("NO.", "userName", "currencyCode", "sportName", "profit", "orderAmount"
            , "marketType", "marketValue", "oddsValue", "orderStatus", "createTime", "beginTime", "settleTime", "orderNo", "settleStatus", "matchId", "matchName",
            "matchInfo", "matchType", "playName", "userId");

    /**
     * 查询BC注单列表,只查询单关
     *
     * @Param: [betOrderVO]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/9/10 10:49
     */
    @Override
    public Response<Object> queryTicketList(BetOrderVO betOrderVO) {
        try {
            int pageSize = betOrderVO.getPageSize() == null || betOrderVO.getPageSize() > 100 ? 20 : betOrderVO.getPageSize();
            int pageNum = betOrderVO.getPageNum() == null ? 1 : betOrderVO.getPageNum();
            betOrderVO.setPageNo(pageNum);
            betOrderVO.setSize(pageSize);
            betOrderVO.setStart((pageNum - 1) * pageSize);
            betOrderVO.setEnd(pageNum * pageSize);
            log.info("betOrderVO:" + betOrderVO);
            betOrderVO.setSeriesType(1);
            betOrderVO.setOddsDataSource("BC");
            Map<String, Object> result = this.abstractQueryUserOrderList(betOrderVO,databaseSwitch);
            log.info("----------------->result betOrderVO:" + betOrderVO);
            List<Map<String, Object>> ratelist = "1".equals(betOrderVO.getFilter()) ? backupOrderMixMapper.queryBcOrderRate(betOrderVO.getStartTimeL(), betOrderVO.getEndTimeL())
                    : backupOrderMixMapper.queryBcOrderRate(null, null);
            if (CollectionUtils.isEmpty(ratelist)) {
                log.error("内部错误!ratelist=" + ratelist);
                return Response.returnFail("内部错误!");
            }
            List<OrderSettle> betOrderPOList = (List<OrderSettle>) result.get("list");
            if (CollectionUtils.isNotEmpty(betOrderPOList)) {
                for (OrderSettle orderSettle : betOrderPOList) {
                    Long createTime = orderSettle.getCreateTime();
                    BigDecimal rate = getRateByCreateTime(createTime, ratelist);
                    orderSettle.setOrderAmountTotal(orderSettle.getOrderAmountTotal().multiply(rate));
                    orderSettle.setProductAmountTotal(orderSettle.getProductAmountTotal().multiply(rate));
                    orderSettle.setLocalBetAmount(orderSettle.getLocalBetAmount().multiply(rate));
                    if (orderSettle.getProfitAmount() != null) {
                        orderSettle.setSettleAmount(orderSettle.getSettleAmount().multiply(rate));
                        orderSettle.setLocalProfitAmount(orderSettle.getLocalProfitAmount().multiply(rate));
                        orderSettle.setProfitAmount(orderSettle.getProfitAmount().multiply(rate));
                        //orderSettle.setLocalSettleAmount(orderSettle.getLocalSettleAmount().multiply(rate));
                    }
                }
            }
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("查询注单异常!", e);
            return Response.returnFail("查询注单异常!");
        }
    }

    private BigDecimal getRateByCreateTime(Long createTime, List<Map<String, Object>> ratelist) {
        for (Map<String, Object> map : ratelist) {
            BigDecimal rate = (BigDecimal) map.get("rate");
            long tempStartL = (long) map.get("start");
            long tempEndL = (long) map.get("end");
            if (createTime >= tempStartL && createTime <= tempEndL) {
                return rate;
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 查询BC朱丹列表统计
     *
     * @Param: [betOrderVO]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/9/10 10:49
     */
    @Override
    public Response<Object> getStatistics(BetOrderVO betOrderVO) {
        try {
            assemblyQueryTime(betOrderVO,databaseSwitch);
            betOrderVO.setSeriesType(1);
            betOrderVO.setOddsDataSource("BC");
            Long startTimeL = betOrderVO.getStartTimeL();
            Long endTimeL = betOrderVO.getEndTimeL();
            List<Map<String, Object>> list = backupOrderMixMapper.queryBcOrderRate(startTimeL, endTimeL);

            if (CollectionUtils.isEmpty(list)) {
                log.error("---------->getStatistics内部错误!ratelist=" + list);
                return Response.returnFail("内部错误!");
            }
            Map<String, Object> firstMap = list.get(0);
            BigDecimal rate = (BigDecimal) firstMap.get("rate");
            long tempStartL = (long) firstMap.get("start");
            long tempEndL = (long) firstMap.get("end");
            BigDecimal bet = BigDecimal.ZERO, profit = BigDecimal.ZERO;
            if (!"1".equals(betOrderVO.getFilter())) {
                Set<String> orderSet = new HashSet<>();
                Set<Long> userSet = new HashSet<>();
                List<Map<String, Object>> rateList = backupOrderMixMapper.queryBcOrderRate(null, null);
                List<Map<String, Object>> resultList = backupOrderMixMapper.getSettleStatisticsWithRate(betOrderVO);
                for (Map<String, Object> tempMap : resultList) {
                    BigDecimal tempBet = (BigDecimal) tempMap.get("betAmount");
                    BigDecimal tempProfit = (BigDecimal) tempMap.get("sumProfitAmount");
                    Long createTime = (Long) tempMap.get("createTime");
                    String orderNo = (String) tempMap.get("orderNo");
                    Long userId = (Long) tempMap.get("userId");
                    BigDecimal tRate = getRateByCreateTime(createTime, rateList);
                    bet = bet.add(tempBet.multiply(tRate));
                    profit = profit.add(tempProfit.multiply(tRate));
                    orderSet.add(orderNo);
                    userSet.add(userId);
                }
                Map<String, Object> result = new HashMap<>();
                result.put("betAmount", bet.setScale(2, BigDecimal.ROUND_FLOOR));
                result.put("sumProfitAmount", profit.setScale(2, BigDecimal.ROUND_FLOOR));
                result.put("sumBetNo", orderSet.size());
                result.put("userAmount", userSet.size());
                return Response.returnSuccess(result);
            } else {
                Map<String, Object> result = (Map<String, Object>) this.abstractGetStatistics(betOrderVO,databaseSwitch);
                log.info("startTimeL=" + startTimeL + ",endTimeL=" + endTimeL + ",tempStartL=" + tempStartL + ",tempEndL=" + tempEndL);
                if (tempStartL <= startTimeL && startTimeL <= endTimeL && endTimeL <= tempEndL) {
                    bet = (BigDecimal) result.get("betAmount");
                    profit = (BigDecimal) result.get("sumProfitAmount");
                    result.put("betAmount", bet.multiply(rate).setScale(2, BigDecimal.ROUND_FLOOR));
                    result.put("sumProfitAmount", profit == null ? BigDecimal.ZERO : profit.multiply(rate).setScale(2, BigDecimal.ROUND_FLOOR));
                    return Response.returnSuccess(result);
                } else if (tempStartL <= startTimeL && startTimeL <= tempEndL && tempEndL <= endTimeL) {
                    Map<String, Object> lastMap = list.get(list.size() - 1);
                    long lastEndL = (long) lastMap.get("end");
                    if (lastEndL < endTimeL) {
                        log.error("查询2,lastEndL=" + lastEndL + ",lastEndL=" + endTimeL);
                        Response.returnFail("内部错误!");
                    }
                    for (int i = 0; i < list.size(); i++) {
                        BigDecimal tempRate = (BigDecimal) list.get(i).get("rate");
                        long tempS = (long) list.get(i).get("start");
                        long tempE = (long) list.get(i).get("end");
                        if (i == 0) {
                            betOrderVO.setEndTimeL(tempE);
                        } else if (i == list.size() - 1) {
                            betOrderVO.setStartTimeL(tempS);
                            betOrderVO.setEndTimeL(endTimeL);
                        } else {
                            betOrderVO.setStartTimeL(tempS);
                            betOrderVO.setEndTimeL(tempE);
                        }
                        Map<String, Object> tempR = (Map<String, Object>) this.abstractGetStatistics(betOrderVO,databaseSwitch);
                        BigDecimal tempBet = (BigDecimal) tempR.get("betAmount");
                        BigDecimal tempProfit = (BigDecimal) tempR.get("sumProfitAmount");
                        if (tempBet == null) {
                            continue;
                        }
                        bet = bet.add(tempBet.multiply(tempRate));
                        profit = profit.add(tempProfit.multiply(tempRate));
                    }
                    result.put("betAmount", bet.setScale(2, BigDecimal.ROUND_FLOOR));
                    result.put("sumProfitAmount", profit.setScale(2, BigDecimal.ROUND_FLOOR));
                    return Response.returnSuccess(result);
                }
            }
            log.error("查询3,内部错误!");
            return Response.returnFail("内部错误!");
        } catch (Exception e) {
            log.error("BC查询注单统计异常!", e);
            return Response.returnFail("查询注单统计异常!");
        }
    }

    /**
     * 查询BC注单导出
     *
     * @Param: [betOrderVO]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/9/10 10:49
     */
    @Override
    public void exportTicketList(HttpServletResponse response, BetOrderVO betOrderVO) {
        try {
            betOrderVO.setPageNo(1);
            betOrderVO.setStart(0);
            betOrderVO.setEnd(100000);
            betOrderVO.setSeriesType(1);
            betOrderVO.setOddsDataSource("BC");
            betOrderVO.setSize(100000);
            log.info("BC注单导出betOrderVO:" + betOrderVO);
            Map<String, Object> result = this.abstractQueryUserOrderList(betOrderVO,databaseSwitch);
            List<OrderSettle> orderSettleList = (List<OrderSettle>) result.get("list");
            if (CollectionUtils.isEmpty(orderSettleList)) {
                return;
            }
            List<Map<String, Object>> ratelist = "1".equals(betOrderVO.getFilter()) ? backupOrderMixMapper.queryBcOrderRate(betOrderVO.getStartTimeL(), betOrderVO.getEndTimeL())
                    : backupOrderMixMapper.queryBcOrderRate(null, null);
            if (CollectionUtils.isEmpty(ratelist)) {
                log.error("内部错误!ratelist=" + ratelist);
                return;
            }
            log.info("导出rateList:" + ratelist);
            if (CollectionUtils.isNotEmpty(orderSettleList)) {
                for (OrderSettle orderSettle : orderSettleList) {
                    Long createTime = orderSettle.getCreateTime();
                    BigDecimal rate = getRateByCreateTime(createTime, ratelist);
                    orderSettle.setOrderAmountTotal(orderSettle.getOrderAmountTotal().multiply(rate));
                    orderSettle.setProductAmountTotal(orderSettle.getProductAmountTotal().multiply(rate));
                    orderSettle.setLocalBetAmount(orderSettle.getLocalBetAmount().multiply(rate));
                    if (orderSettle.getProfitAmount() != null) {
                        orderSettle.setSettleAmount(orderSettle.getSettleAmount().multiply(rate));
                        orderSettle.setLocalProfitAmount(orderSettle.getLocalProfitAmount().multiply(rate));
                        orderSettle.setProfitAmount(orderSettle.getProfitAmount().multiply(rate));
                        //orderSettle.setLocalSettleAmount(orderSettle.getLocalSettleAmount().multiply(rate));
                    }
                }
            }
            String fileName = URLEncoder.encode("panda-ticket.csv", StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            FileCopyUtils.copy(exportTicketsToCsv(orderSettleList, betOrderVO.getLanguage()), response.getOutputStream());
        } catch (Exception e) {
            log.error("导出注单列表!", e);
        }
    }

    @Override
    public Response getSportList(String language) {
        try {
            List<SportPO> sportPOList = sportMapper.getSportList(language);
            SportPO sportPO = new SportPO();
            sportPO.setName(language.equals(LanguageEnum.ZS.getCode()) ? "全部赛种" : "sport");
            sportPOList.add(0, sportPO);
            return Response.returnSuccess(sportPOList);
        } catch (Exception e) {
            log.error("查询体种异常!", e);
            return Response.returnFail("查询体种异常");
        }
    }

    @Override
    protected List<UserOrderAllPO> callUserReportService(UserOrderVO vo) {
        return null;
    }

    @Override
    protected UserOrderAllPO callReportSelectByUser(String userId) {
        return null;
    }


    private byte[] exportTicketsToCsv(List<OrderSettle> resultList, String language) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        int i = 0;
        for (OrderSettle order : resultList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", order.getUserName());
            rowData.put("3", CurrencyTypeEnum.optGetCurrency(order.getCurrencyCode()) + " ");
            rowData.put("4", order.getOrderDetailList().get(0).getSportName() + " ");
            rowData.put("5", getObj(order.getProfitAmount()) + " ");
            rowData.put("6", getObj(order.getOrderAmountTotal()) + " ");
            rowData.put("7", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMarketType()) : " ");
            rowData.put("8", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMarketValue()) : " ");
            rowData.put("9", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getOddFinally()) : " ");
            rowData.put("10", getOrderStatus(order.getOrderStatus(), language) + " ");
            rowData.put("11", getObj(order.getCreateTimeStr()) + " ");
            rowData.put("12", getObj(order.getOrderDetailList().get(0).getBeginTimeStr()) + " ");
            rowData.put("13", getObj(order.getSettleTimeStr()) + " ");
            rowData.put("14", order.getOrderNo() + "\t");
            rowData.put("15", assemblySettleStatus(order.getOutcome(), language) + " ");
            rowData.put("16", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMatchId()) : " ");
            rowData.put("17", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMatchName()) : " ");
            rowData.put("18", order.getSeriesType() == 1 ? order.getOrderDetailList().get(0).getMatchInfo() : " ");
            Integer matchType = order.getOrderDetailList().get(0).getMatchType();
            rowData.put("19", assemblyMatchType(matchType, language) + " ");
            rowData.put("20", order.getSeriesType() == 1 ? order.getOrderDetailList().get(0).getPlayName() : " ");
            rowData.put("21", order.getUid() + "\t");
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        assemblyTitle((language.equals(LanguageEnum.ZS.getCode()) ? CN_TITLE : EN_TITLE), header);
        return CsvUtil.exportCSV(header, exportData);
    }

    private Object assemblyMatchType(Integer matchType, String language) {
        return language.equals(LanguageEnum.ZS.getCode()) ? (matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) : (matchType == 1 ? "Prematch" : (matchType == 2 ? "Live" : "Champion"));
    }

    private Object assemblySettleStatus(Integer outcome, String language) {
        return language.equals(LanguageEnum.ZS.getCode()) ? (outcome == null ? "未结算" : settleStatusMap.get(outcome)) : (outcome == null ? "unsettled" : settleStatusEnMap.get(outcome));
    }

    private void assemblyTitle(List<String> titles, LinkedHashMap<String, String> header) {
        for (int i = 0; i < titles.size(); i++) {
            header.put(i + "", titles.get(i));
        }
    }
}
