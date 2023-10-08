package com.panda.sport.order.service;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.panda.sport.backup83.mapper.Backup83OrderMixV2Mapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.utils.MatchPeriodScoreEnumUtil;
import com.panda.sport.merchant.common.utils.OddsValuesUtils;
import com.panda.sport.merchant.common.vo.BetOrderV2VO;
import com.panda.sport.merchant.common.vo.UserOrderV2VO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import com.panda.sport.merchant.common.vo.merchant.OrderSettleDetail;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.*;
import static com.panda.sport.merchant.common.utils.MatchPeriodScoreEnumUtil.*;
import static com.panda.sport.merchant.common.utils.MatchPeriodScoreEnumUtil.settleScoreMap;
import static java.util.regex.Pattern.compile;

@Slf4j
@Service("abstractOrderService")
public abstract class AbstractOrderV2Service {

    private final static Set<Integer> HALF_TOTAL = Sets.newHashSet(103, 104);
    private final static Set<Integer> HANDICAP_PLAY =
            Sets.newHashSet(20003, 20004, 20015, 39, 46, 19, 58, 52, 64, 4, 3, 69, 71, 121, 143, 172, 113, 163, 176, 155, 154, 181, 185, 243, 249, 278);
    public static final Set<Integer> NET_SCORE_PLAY = Sets.newHashSet(141, 200, 209, 211, 212, 219, 238);
    private final static Set<Integer> SHOW_PLAY_OPTION_NAME = Sets.newHashSet(49, 55, 61, 67, 102, 170, 36, 148, 150, 151, 152);

    @Setter(onMethod_=@Autowired)
    public MerchantMapper merchantMapper;

    @Setter(onMethod_=@Autowired)
    public LocalCacheService localCacheService;

    @Setter(onMethod_=@Autowired)
    public Backup83OrderMixV2Mapper backup83OrderMixV2Mapper;


    /**
     * 查询注单具体实现,投注时间1,比赛时间2,结算时间3
     */
    protected Map<String, Object> abstractQueryUserOrderList(UserOrderV2VO userOrderV2VO) throws Exception {
        int totalCount;
        List<OrderSettle> betOrderPOList;
        final String traceId = userOrderV2VO.getTraceId();
        final long start = System.currentTimeMillis();
        final int pageNum = userOrderV2VO.getPageNum();
        final int pageSize = userOrderV2VO.getSize();

        Map<String, Object> result = Maps.newHashMap();
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        // 参数预处理
        BetOrderV2VO betOrderVO = assemblyQueryParams(userOrderV2VO);
        log.info("/v2/order/user/queryTicketList:traceId【{}】,参数处理耗时：【{}】", traceId, System.currentTimeMillis() - start);

        // 查数量
        totalCount = queryOrderCount(betOrderVO);
        log.info("/v2/order/user/queryTicketList:traceId【{}】,totalCount:【{}】,查询数量耗时：【{}】", traceId, totalCount, System.currentTimeMillis() - start);

        result.put("total",totalCount);
        if(totalCount == 0) return result;

        // 查注单
        betOrderPOList = queryOrderList(betOrderVO);
        log.info("/v2/order/user/queryTicketList:traceId【{}】,查询注单耗时：【{}】", traceId, System.currentTimeMillis() - start);

        if(CollectionUtils.isEmpty(betOrderPOList)){
            result.put("list",betOrderPOList);
            return result;
        }

        // 结果处理
        assemblyQueryResult(betOrderVO, betOrderPOList);
        result.put("list",betOrderPOList);
        log.info("/v2/order/user/queryTicketList:traceId【{}】,处理结果耗时：【{}】", traceId, System.currentTimeMillis() - start);

        return result;

    }

    private void assemblyQueryResult(BetOrderV2VO betOrderVO, List<OrderSettle> betOrderPOList) {
        final int serviceType = betOrderVO.getServiceType();

        // 补充用户和商户数据
        assembleUserAndMerchantResult(betOrderPOList,serviceType);

    }

    /**
     * 查询注单详情
     * BET_TIME_FILTER : 投注维度
     * SETTLE_TIME_FILTER : 结算维度
     * MATCH_BEGIN_FILTER : 开赛维度
     */
    private List<OrderSettle> queryOrderList(BetOrderV2VO betOrderVO) {
        final String filter = betOrderVO.getFilter();
        if (BET_TIME_FILTER.equals(filter)) {
            return backup83OrderMixV2Mapper.queryBetOrderList(betOrderVO);
        }else if (SETTLE_TIME_FILTER.equals(filter)) {
            return backup83OrderMixV2Mapper.querySettledOrderList(betOrderVO);
        }
        return backup83OrderMixV2Mapper.queryLiveOrderList(betOrderVO);
    }

    /**
     * 查询注单数量
     * BET_TIME_FILTER : 投注维度
     * SETTLE_TIME_FILTER : 结算维度
     * MATCH_BEGIN_FILTER : 开赛维度
     */
    private int queryOrderCount(BetOrderV2VO betOrderVO) {
        final String filter = betOrderVO.getFilter();
        if (BET_TIME_FILTER.equals(filter)) {
            return backup83OrderMixV2Mapper.countBetOrderList(betOrderVO);
        }else if (SETTLE_TIME_FILTER.equals(filter)) {
            return backup83OrderMixV2Mapper.countSettledOrderList(betOrderVO) ;
        }
        return backup83OrderMixV2Mapper.countLiveOrderList(betOrderVO);
    }


    private void assemblyQueryParams4Order(UserOrderV2VO userOrderV2VO) {
        if (StringUtils.isNotEmpty(userOrderV2VO.getMerchantCode())) {
            List<String> list = merchantMapper.queryChildren(userOrderV2VO.getMerchantCode());
            if (CollectionUtils.isNotEmpty(list)) {
                userOrderV2VO.setMerchantCode(null);
                userOrderV2VO.setMerchantCodeList(list);
            }
        }
    }

    private void assemblyQueryParams4Admin(UserOrderV2VO userOrderV2VO) {

    }

    private BetOrderV2VO assemblyQueryParams(UserOrderV2VO userOrderV2VO) throws Exception {

        final int serviceType = userOrderV2VO.getServiceType();
        // 1order 2admin
        if( 1 == serviceType){
            assemblyQueryParams4Order(userOrderV2VO);
        }else{
            assemblyQueryParams4Admin(userOrderV2VO);
        }
        // 参数类转换
        BetOrderV2VO betOrderVO = assemblUserOrderV2VO2BetOrderV2VO(userOrderV2VO);
        // 时间处理
        assemblyQueryTime(betOrderVO);
        // 处理排序条件
        assemblyParams4Filter(betOrderVO);

        return betOrderVO;
    }

    private void assemblyParams4Filter(BetOrderV2VO betOrderVO) {
        final String filter = StringUtils.defaultString(betOrderVO.getFilter(),BET_TIME_FILTER );
        if (BET_TIME_FILTER.equals(filter)) {
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder())
                    || Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder()) == null ?
                    Constant.ID : Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortBy()) ? Constant.DESC : betOrderVO.getSortBy();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortBy(sort);
        }else if (SETTLE_TIME_FILTER.equals(filter)) {
            betOrderVO.setOrderStatus(null);
            betOrderVO.setOrderStatusList(null);
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder())
                    || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
                    Constant.SID : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortBy()) ? Constant.DESC : betOrderVO.getSortBy();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortBy(sort);
        }else{
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder())
                    || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
                    Constant.ID : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortBy()) ? Constant.DESC : betOrderVO.getSortBy();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortBy(sort);
        }
    }

    /**
     * 组装注单查询参数,自然日
     *
     * @param vo
     * @return
     * @throws Exception
     */
    protected BetOrderV2VO assemblUserOrderV2VO2BetOrderV2VO(UserOrderV2VO vo) {
        BetOrderV2VO betOrderVO = new BetOrderV2VO();
        BeanUtils.copyProperties(vo, betOrderVO);
        int pageSize = vo.getPageSize() == null || vo.getPageSize() > 200 ? 20 : vo.getPageSize();
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        betOrderVO.setPageNo(pageNum);
        betOrderVO.setSize(pageSize);
        betOrderVO.setStart((pageNum - 1) * pageSize);
        betOrderVO.setEnd(pageNum * pageSize);
        betOrderVO.setLanguage(vo.getLanguage());
        if (vo.getUserId() != null) {
            betOrderVO.setUserId(Long.parseLong(vo.getUserId()));
        }
        if (vo.getMatchId() != null) {
            betOrderVO.setMatchId(Long.parseLong(vo.getMatchId()));
        }
        return betOrderVO;
    }

    /**
     * 注单查询 限制查询90天
     *
     * @Param: [betOrderVO]
     * @date: 2020/8/23 15:40
     */
    protected void assemblyQueryTime(BetOrderV2VO betOrderVO) throws Exception {
        Long matchId = betOrderVO.getMatchId();
        String startTime = betOrderVO.getStartTime();
        String endTime = betOrderVO.getEndTime();
        Long userId = betOrderVO.getUserId();
        List<Long> userIdList = betOrderVO.getUserIdList();
        String orderNo = betOrderVO.getOrderNo();
        Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss"),
                endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"), before, now = new Date();
        long pStartTimeL = startDate.getTime();
        long pEndTimeL = endDate.getTime();
        //世界杯期间注单查询限制：导出3天、查询限制到7天
        //120天限制放大到365天；
        if (MATCH_BEGIN_FILTER.equals(betOrderVO.getFilter())
                && userId == null && StringUtils.isEmpty(orderNo)
                && betOrderVO.getOrderNoList() == null && matchId == null
                && CollectionUtils.isEmpty(userIdList)) {
            before = DateUtils.addDays(now, -3);
            betOrderVO.setBetStartTimeL(before.getTime());
        }
        betOrderVO.setStartTimeL(pStartTimeL);
        betOrderVO.setEndTimeL(pEndTimeL + 999);
        if (StringUtils.isNotEmpty(orderNo)) {
            long timeL = System.currentTimeMillis();
            //（120天）360限制放大到365天；
            betOrderVO.setStartTimeL(timeL - (long) 1000 * 60 * 60 * 24 * 360);
            betOrderVO.setEndTimeL(timeL + (long) 1000 * 60 * 60 * 24 * 360);
            betOrderVO.setBetStartTimeL(timeL - (long) 1000 * 60 * 60 * 24 * 360);
        }
        betOrderVO.setLanguage(betOrderVO.getLanguage() == null ? LanguageEnum.ZS.getCode() : betOrderVO.getLanguage());
        if (CollectionUtils.isNotEmpty(betOrderVO.getOutComeList())) {
            betOrderVO.setOrderStatus(1);
        }
        Integer merchantTag = betOrderVO.getMerchantTag();
        if (merchantTag != null) {
            List<String> merchantList = (List<String>) LocalCacheService.tagMap.getIfPresent(merchantTag);
            log.info(merchantTag + ",缓存获取商户编码列表:" + merchantList);
            if (CollectionUtils.isNotEmpty(merchantList)) {
                List<String> codeParamList = betOrderVO.getMerchantCodeList();
                log.info(merchantTag + ",merchantTag:入参merchantCode:" + codeParamList);
                if (CollectionUtils.isNotEmpty(codeParamList)) {
                    merchantList.retainAll(codeParamList);
                }
                log.info(merchantTag + ",merchantTag:根据tag获取merchantList:" + merchantList);
                betOrderVO.setMerchantCodeList(merchantList);
                String codeParam = betOrderVO.getMerchantCode();
                if (StringUtils.isNotEmpty(codeParam) && !merchantList.contains(codeParam)) {
                    log.error("Invalid paramter!" + codeParam);
                    throw new Exception("Invalid paramter!");
                }
            }
        }
        List<String> codeList = betOrderVO.getMerchantCodeList();
        if (CollectionUtils.isNotEmpty(codeList)) {
            betOrderVO.setMerchantIdList(localCacheService.getMerchantIdList(codeList));
        }
        if (StringUtils.isNotEmpty(betOrderVO.getMerchantCode())) {
            betOrderVO.setMerchantId(localCacheService.getMerchantId(betOrderVO.getMerchantCode()));
        }
        log.info("assemblyQueryTime.betOrderVO:" + betOrderVO);
    }

    private List<OrderSettle> assemblySettleScoreAndPlayOptionName(List<OrderSettle> orderList, String language) {
        if (orderList == null || orderList.size() < 1) {
            return null;
        }
        OrderSettle orderSettle = orderList.get(orderList.size() - 1);
        List<Map<String, Object>> seriesList = new ArrayList<>();
        for (OrderSettle settle : orderList) {
            Integer seriesType = settle.getSeriesType();
            Integer settleType = settle.getSettleType();
            Integer managerCode = settle.getManagerCode();
            if (settle.getOutcome() != null && (settle.getOutcome() == 4 || settle.getOutcome() == 5)) {
                if (settle.getLocalProfitAmount() == null) {
                    settle.setLocalOrderValidBetMoney(new BigDecimal(0));
                } else {
                    if (settle.getLocalBetAmount()!=null && settle.getLocalBetAmount().compareTo(settle.getLocalProfitAmount()) < 0) {
                        settle.setLocalOrderValidBetMoney(settle.getLocalBetAmount());
                    } else {
                        settle.setLocalOrderValidBetMoney(settle.getLocalProfitAmount());
                    }
                }
                if (settle.getProfitAmount() == null) {
                    settle.setOrderValidBetMoney(new BigDecimal(0));
                } else {
                    if (settle.getOrderAmountTotal().compareTo(settle.getProfitAmount()) < 0) {
                        settle.setOrderValidBetMoney(settle.getOrderAmountTotal());
                    } else {
                        settle.setOrderValidBetMoney(settle.getProfitAmount());
                    }
                }
            } else {
                settle.setLocalOrderValidBetMoney(settle.getLocalProfitAmount() == null ? new BigDecimal(0) : settle.getLocalProfitAmount().abs());
                settle.setOrderValidBetMoney(settle.getProfitAmount() == null ? new BigDecimal(0) : settle.getProfitAmount().abs());
            }
            if (settle.getOutcome() != null && (settle.getOutcome() == 4 || settle.getOutcome() == 5)) {
                if (settle.getLocalProfitAmount() == null) {
                    settle.setLocalOrderValidBetMoney(new BigDecimal(0));
                } else {
                    if (settle.getLocalBetAmount().compareTo(settle.getLocalProfitAmount()) < 0) {
                        settle.setLocalOrderValidBetMoney(settle.getLocalBetAmount());
                    } else {
                        settle.setLocalOrderValidBetMoney(settle.getLocalProfitAmount());
                    }
                }
                if (settle.getProfitAmount() == null) {
                    settle.setOrderValidBetMoney(new BigDecimal(0));
                } else {
                    if (settle.getOrderAmountTotal().compareTo(settle.getProfitAmount()) < 0) {
                        settle.setOrderValidBetMoney(settle.getOrderAmountTotal());
                    } else {
                        settle.setOrderValidBetMoney(settle.getProfitAmount());
                    }
                }
            } else {
                settle.setLocalOrderValidBetMoney(settle.getLocalProfitAmount() == null ? new BigDecimal(0) : settle.getLocalProfitAmount().abs());
                settle.setOrderValidBetMoney(settle.getProfitAmount() == null ? new BigDecimal(0) : settle.getProfitAmount().abs());
            }
            if (seriesType > 1) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("seriesType", seriesType);
                tempMap.put("orderNo", settle.getOrderNo());
                List<Double> oddsList = settle.getOrderDetailList().stream()
                        .map(OrderSettleDetail::getOddsValue).collect(Collectors.toList());
                tempMap.put("oddsList", oddsList);
                tempMap.put("managerCode", settle.getManagerCode());
                seriesList.add(tempMap);
            }
            for (OrderSettleDetail detail : settle.getOrderDetailList()) {
                String riskEvent = detail.getRiskEvent();
                Integer cancelType = detail.getCancelType();
                Integer sportId = detail.getSportId();
                String matchInfo = detail.getMatchInfo();
                Integer matchType = detail.getMatchType();
                String settleScore = detail.getSettleScore();
                if (matchType == 3) {
                    if (StringUtils.isNotEmpty(settleScore) && NumberUtils.isDigits(settleScore)) {
                        detail.setBeginTime(Long.parseLong(settleScore));
                        detail.setSettleScore(null);
                    } else {
                        detail.setBeginTime(null);
                        detail.setBeginTimeStr(null);
                    }
                }
                detail.setRiskEvent(getRiskEvent(riskEvent, cancelType));
                if (cancelType != null && 18 == cancelType) {
                    detail.setRiskEvent(filterRemark(detail.getRemark()));
                }
                detail.setSettleScore(detail.getBetStatus() == 1 && detail.getBetResult() != 0 ? assemblySettleScore(detail, language, managerCode) : "");
                detail.setMarketValue(!language.equals(LanguageEnum.ZS.getCode()) || orderSettle.getCreateTime() <= 1605843003000L ?
                        assemblyMarketValue(detail, language) : detail.getPlayOptionName());
                if (managerCode != null && managerCode == 3) {
                    if (sportId == 1002 || sportId == 1011) {
                        detail.setMatchInfo(detail.getBatchNo());
                    }
                    if (StringUtils.isNotBlank(riskEvent) && riskEvent.contains("||")) {
                        String[] matchDayBatchNo = riskEvent.split("\\|\\|");
                        if (sportId == 1001 || sportId == 1004) {
                            detail.setMatchInfo(matchDayBatchNo.length > 2 ? matchDayBatchNo[0] + matchDayBatchNo[2] : matchDayBatchNo[0]);
                            detail.setMatchDay(matchDayBatchNo[1]);
                            detail.setBatchNo(matchDayBatchNo[1]);
                        } else {
                            detail.setBatchNo(matchDayBatchNo[1]);
                            detail.setMatchInfo(matchDayBatchNo.length > 2 ? matchDayBatchNo[2] : matchDayBatchNo[1]);
                            detail.setMatchDay(matchDayBatchNo.length > 2 ? matchDayBatchNo[2] : matchDayBatchNo[1]);
                        }
                    }

                }
                detail.setMatchName(detail.getTournamentName());
            }
            if (!language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED)) {
                settle.setSeriesValue(series_en.get(seriesType + ""));
                String ip = settle.getIpArea();
                if (ip.equals("局域网")) {
                    settle.setIpArea("local network");
                }
            } else {
                settle.setSeriesValue(series_zs.get(seriesType + ""));
            }

            // 填充有效投注笔数
            settle.setSumValidBetNo(CommonDefaultValue.ZERO);
            if (settle.getOutcome() != null && (settle.getOutcome() == 3 || settle.getOutcome() == 4 || settle.getOutcome() == 5 || settle.getOutcome() == 6)) {
                settle.setSumValidBetNo(CommonDefaultValue.ONE);
            }
        }
        if (seriesList.size() > 0) {
            Map<String, String> map = OddsValuesUtils.seriesTotalOddsValues(seriesList);
            for (OrderSettle settle : orderList) {
                settle.setOdds(map.get(settle.getOrderNo()));
            }
        }
        return orderList;
    }

    private String getRiskEvent(String riskEvent, Integer cancelType) {
        DangerBallEnum riskEventEnum = DangerBallEnum.getRiskEventEnum(riskEvent);
        if (null == riskEventEnum) {
            if (cancelType != null && cancelType > 0) {
                return RiskEventFromCancelTypeEnum.getDescByCode(cancelType);
            }
        } else {
            return riskEventEnum.getName();
        }
        return null;
    }

    private String filterRemark(String remark) {
        if (StringUtils.isNotBlank(remark)) {
            if (remark.contains("：")) {
                String[] str = remark.split("：");
                return str[str.length - 1];
            } else if (remark.contains(":")) {
                String[] str = remark.split(":");
                return str[str.length - 1];
            }
        }
        return remark;
    }

    /**
     * 组装结算比分,MatchPeriodScoreEnumUtil 映射s_match_info 的match_test_score
     * strategy:ADD 比分加总,HOME_AWAY_ADD 主队和客队各自加总,JOIN 拼接
     *
     * @Param: [orderVO]
     * @return: java.lang.String
     * @date: 2020/8/23 15:41
     */
    private String assemblySettleScore(OrderSettleDetail orderDetailVO, String language, Integer managerCode) {
        try {
            String sportId = orderDetailVO.getSportId() + "";
            String playId = orderDetailVO.getPlayId() + "";
            String sportIdPlayIdStr = sportId + "p" + playId;
            String playName = orderDetailVO.getOriginalPlay();
            String scoreKey;
            String score = "", desc = "";
            language = StringUtils.isEmpty(language) ? LanguageEnum.ZS.getCode() : language;
            String matchScore = orderDetailVO.getMatchScore();
            String settleScore = orderDetailVO.getSettleScore();
            Long childPlayId = orderDetailVO.getChildPlayId();
            log.info("assemblySettleScore:betNo:{},sportIdPlayIdStr:{},score:{},childPlayId:{}",orderDetailVO.getBetNo(),sportIdPlayIdStr,matchScore,childPlayId);
            if (managerCode != null && managerCode == 3 && !sportId.equals("1001") && !sportId.equals("1004")) {
                return regxScore(orderDetailVO.getBetNo(), matchScore, language);
            }
            if (MatchPeriodScoreEnumUtil.fifteenPlay.contains(sportIdPlayIdStr)) {
                if (childPlayId != null) {
                    String intervalStr = childPlayId.toString().replace(playId, "");
                    String interval = "part:" + Integer.parseInt(intervalStr);
                    log.info(sportIdPlayIdStr + ",interval=" + interval);
                    if (StringUtils.isNotEmpty(interval)) {
                        String fifteenKey = settleScoreMap.get(sportIdPlayIdStr + interval);
                        score = getNullableMatchScore(matchScore, fifteenKey);
                        ScoreEnum.getScoreEnumByKey(fifteenKey);
                        if (language.equals(LanguageEnum.ZS.getCode())) {
                            desc = (String) ScoreEnum.ScoreEnumMap.get(language + fifteenKey);
                        } else {
                            desc = (String) ScoreEnum.ScoreEnumMap.get(LanguageEnum.EN.getCode() + fifteenKey);
                        }
                        log.info(desc + ",15min玩法:" + score);
                        if (StringUtils.isEmpty(desc) && StringUtils.isNotBlank(playName)) {
                            if (playName.contains("(") && playName.contains(")")) {
                                desc = playName.substring(playName.indexOf("(")).replace("(", "").replace(")", "");
                            } else {
                                desc = playName;
                            }
                        }
                    }
                }
            } else if (MatchPeriodScoreEnumUtil.compoundScoreMap.get(sportIdPlayIdStr) != null) {
                Map<String, Object> strategyMap = MatchPeriodScoreEnumUtil.compoundScoreMap.get(sportIdPlayIdStr);
                String strategy = (String) strategyMap.get(MatchPeriodScoreEnumUtil.STRATEGY);
                desc = (String) strategyMap.get(playId + MatchPeriodScoreEnumUtil.DESC + language);
                if (StringUtils.isEmpty(desc)) {
                    desc = (String) strategyMap.get(MatchPeriodScoreEnumUtil.DESC + language);
                }
                Object obj = strategyMap.get(playId + MatchPeriodScoreEnumUtil.CHILDREN);
                if (obj == null) {
                    obj = strategyMap.get(MatchPeriodScoreEnumUtil.CHILDREN);
                }
                switch (strategy) {
                    case OT: {
                        String score1111 = getNullableMatchScore(matchScore, ScoreEnum.SCORE_S1111.getKey());
                        String score7 = getNullableMatchScore(matchScore, ScoreEnum.SCORE_S7.getKey());
                        if (StringUtils.isNotEmpty(score1111) && StringUtils.isNotEmpty(score7)) {
                            score = score1111 + "(" + score7 + ")";
                        } else if (StringUtils.isNotEmpty(score1111)) {
                            score = score1111;
                        } else {
                            score = "";
                        }
                        break;
                    }
                    case COMPUTE: {
                        String formula = (String) obj;
                        log.info("1formula=" + formula);
                        StringBuilder sb = new StringBuilder();
                        boolean stinge = false;
                        int length = formula.length();
                        String tempFormula = formula;
                        for (int i = 0; i < length; i++) {
                            char a = formula.charAt(i);
                            if (a == 'S') {
                                stinge = true;
                                sb.append(a);
                            } else if (stinge && Character.isDigit(a)) {
                                sb.append(a);
                            }
                            if ((a != 'S' && !Character.isDigit(a)) || (i == length - 1)) {
                                stinge = false;
                                log.info("sb=" + sb);
                                if (StringUtils.isNotEmpty(sb) && sb.length() > 1 && matchScore.contains(sb)) {
                                    String tempScore = getNullableMatchScore(matchScore, sb.toString());
                                    tempFormula = tempFormula.replace(sb.toString(), "(" + tempScore + ")");
                                    log.info("tempFormula=" + tempFormula);
                                    sb.setLength(0);
                                }
                            }
                        }
                        score = getScoreFromFormula(tempFormula);
                        break;
                    }
                    case MAX: {
                        List<String> children = (List<String>) obj;
                        String firstScore = children.get(0);
                        log.info("firstScore=" + firstScore);
                        String[] a = firstScore.split(":");
                        int maxValue = Integer.parseInt(a[0]) + Integer.parseInt(a[1]);
                        String max = firstScore;
                        String innerDesc = "";
                        for (String child : children) {
                            String innerScore = getNullableMatchScore(matchScore, child);
                            log.info("innerScore=" + innerScore);
                            String[] b = innerScore.split(":");
                            int current = Integer.parseInt(b[0]) + Integer.parseInt(b[1]);
                            if (current > maxValue) {
                                max = innerScore;
                                if (language.equals(LanguageEnum.ZS.getCode())) {
                                    innerDesc = (String) ScoreEnum.ScoreEnumMap.get(language + child);
                                } else {
                                    innerDesc = (String) ScoreEnum.ScoreEnumMap.get(LanguageEnum.EN.getCode() + child);
                                }
                            }
                        }
                        score = max;
                        desc = desc + " " + innerDesc;
                        break;
                    }
                    case ADD: {
                        int tempResult = 0;
                        List<String> children = (List<String>) obj;
                        for (String child : children) {
                            String tempScore = getNullableMatchScore(matchScore, child);
                            if (StringUtils.isNotBlank(tempScore)) {
                                String[] a = tempScore.split(":");
                                tempResult = tempResult + Integer.parseInt(a[0]) + Integer.parseInt(a[1]);
                            }
                        }
                        score = (tempResult == 0 ? "" : (tempResult + ""));
                        break;
                    }
                    case HOME_AWAY_ADD: {
                        int homeResult = 0, awayResult = 0;
                        List<String> children = (List<String>) obj;
                        for (String child : children) {
                            String tempScore = getNullableMatchScore(matchScore, child);
                            if (StringUtils.isNotBlank(tempScore)) {
                                String[] a = tempScore.split(":");
                                homeResult = homeResult + Integer.parseInt(a[0]);
                                awayResult = awayResult + Integer.parseInt(a[1]);
                            }
                        }
                        score = (homeResult == 0 && awayResult == 0) ? "" : (homeResult + ":" + awayResult);
                        break;
                    }
                    case JOIN: {
                        StringBuilder tempResult = new StringBuilder();
                        List<String> children = (List<String>) obj;
                        for (String child : children) {
                            String tempScore = getNullableMatchScore(matchScore, child);
                            if (StringUtils.isNotBlank(tempScore))
                                tempResult.append(tempScore).append(MatchPeriodScoreEnumUtil.JOIN);
                        }
                        score = tempResult.toString();
                        if (score.length() > 1)
                            score = score.substring(0, score.length() - 1);
                        break;
                    }
                    case FROM_TO: {
                        log.info("----------------------------playId:{}------------", playId);
                        Map<Integer, String> tempMap = (Map<Integer, String>) obj;
                        int firstNum = 0, secondNum = 0;
                        try {
                            firstNum = Integer.parseInt(parseString(playName, 0));
                            secondNum = Integer.parseInt(parseString(playName, 1));
                        } catch (Exception e) {
                            log.error("playName解析出异常了.......playName:{}" + playName, e);
                        }
                        int homeResult = 0, awayResult = 0;
                        for (int i = firstNum; i <= secondNum; i++) {
                            String tempScore = getNullableMatchScore(matchScore, tempMap.get(i));
                            log.info("-----------------tempScore:{}------------", tempScore);
                            if (StringUtils.isNotBlank(tempScore)) {
                                String[] a = tempScore.split(":");
                                homeResult = homeResult + Integer.parseInt(a[0]);
                                awayResult = awayResult + Integer.parseInt(a[1]);
                            }
                        }
                        log.info("---------------------homeResult:{} ,awayResult:{}---------", homeResult, awayResult);
                        score = (homeResult == 0 && awayResult == 0) ? "0:0" : (homeResult + ":" + awayResult);
                        desc = language.equalsIgnoreCase(LanguageEnum.ZS.getCode()) ?
                                (String) strategyMap.get(MatchPeriodScoreEnumUtil.DESC + language) : String.valueOf(language.equalsIgnoreCase(LanguageEnum.EN.getCode()));
                        desc = desc.replace("a", firstNum + "").replace("b", secondNum + "");
                        break;
                    }
                }
            } else {
                if (superSpecialPlay.contains(sportIdPlayIdStr)) {
                    String firstNum = parseString(playName, 0);
                    String secondNum = parseString(playName, 1);
                    scoreKey = settleScoreMap.get(sportIdPlayIdStr + firstNum + secondNum);
                } else if (specialPlay.contains(sportIdPlayIdStr)) {
                    String num = parseString(playName, 0);
                    scoreKey = settleScoreMap.get(sportIdPlayIdStr + num);
                } else if (fiveMinitesPlay.contains(sportIdPlayIdStr)) {
                    String xNumber = parseString(playName, 0);
                    if (StringUtils.isNotEmpty(xNumber)) {
                        scoreKey = settleScore(Integer.parseInt(xNumber), matchScore);
                    } else {
                        scoreKey = "";
                    }
                    if (!scoreKey.equalsIgnoreCase(ScoreEnum.SCORE_S1.getKey())) {
                        ScoreEnum.getScoreEnumByKey(scoreKey);
                        String temp;
                        if (language.equals(LanguageEnum.ZS.getCode())) {
                            temp = (String) ScoreEnum.ScoreEnumMap.get(language + scoreKey);
                        } else {
                            temp = (String) ScoreEnum.ScoreEnumMap.get(LanguageEnum.EN.getCode() + scoreKey);
                        }
                        desc = processSportDisplay(temp, sportId);
                        return desc;
                    }
                } else {
                    scoreKey = settleScoreMap.get(sportIdPlayIdStr);
                }
                if (StringUtils.isNotEmpty(scoreKey)) {
                    ScoreEnum.getScoreEnumByKey(scoreKey);
                    score = getNullableMatchScore(matchScore, scoreKey);
                    String temp;
                    if (language.equals(LanguageEnum.ZS.getCode())) {
                        temp = (String) ScoreEnum.ScoreEnumMap.get(language + scoreKey);
                    } else {
                        temp = (String) ScoreEnum.ScoreEnumMap.get(LanguageEnum.EN.getCode() + scoreKey);
                    }
                    desc = processSportDisplay(temp, sportId);
                } /*else {
					score = MatchScoreUtil.getNullableMatchScore(orderVO.getScore(), ScoreEnum.SCORE_S1.getKey());
					desc = Objects.requireNonNull(ScoreEnum.getScoreEnumByKey(ScoreEnum.SCORE_S1.getKey())).getValue();
				}*/
            }
            return StringUtils.isNotEmpty(score) ? (desc + " " + score.replaceAll(":", "-")) : "";
        } catch (Exception e) {
            log.error("结算比分异常", e);
            return "";
        }
    }

    private String assemblyMarketValue(OrderSettleDetail orderSettleDetail, String language) {
        String playOptions = orderSettleDetail.getPlayOptions(), optionValue = orderSettleDetail.getOptionValue(),
                playName = orderSettleDetail.getPlayName(), marketValue = orderSettleDetail.getMarketValue(),
                playOptionName = StringUtils.isEmpty(orderSettleDetail.getPlayOptionName()) ? "" : orderSettleDetail.getPlayOptionName();
        Integer playId = orderSettleDetail.getPlayId();
        if (language.equals(LanguageEnum.ZS.getCode()) && playOptionName.toUpperCase().contains("OTHER")) {
            playOptionName = playOptionName.toUpperCase().replaceAll("ANYOTHER", "其他").replaceAll("OTHER", "其他");
        }
        if (SHOW_PLAY_OPTION_NAME.contains(playId)) {
            return playOptionName;
        }
        try {
            String[] matchArray = {orderSettleDetail.getHomeName(), orderSettleDetail.getAwayName()};
            if (NET_SCORE_PLAY.contains(playId)) {
                optionValue = assemblyNetScore(matchArray, playOptions);
            }
            if (StringUtils.isNotEmpty(optionValue) && optionValue.contains(Constant.TITLE_SHOW_NAME_COMPETITOR1_AHCP)) {
                optionValue = optionValue.replace(Constant.TITLE_SHOW_NAME_COMPETITOR1_AHCP, matchArray[0]);
            }
            if (StringUtils.isNotEmpty(optionValue) && optionValue.contains(Constant.TITLE_SHOW_NAME_COMPETITOR2_AHCP)) {
                optionValue = optionValue.replace(Constant.TITLE_SHOW_NAME_COMPETITOR2_AHCP, matchArray[1]);
            }
            if (StringUtils.isNotEmpty(optionValue) && optionValue.contains(Constant.TITLE_SHOW_NAME_TOTAL)) {
                if (StringUtils.isNotEmpty(marketValue)) {
                    if (marketValue.contains("大") || marketValue.contains("小")) {
                        marketValue = marketValue.replace("大 ", "").replace("小 ", "");
                    }
                }
                optionValue = optionValue.replace(Constant.TITLE_SHOW_NAME_TOTAL, "") + ("," + marketValue);
            }
            if (StringUtils.isNotEmpty(optionValue) && optionValue.contains(Constant.HCP)) {
                optionValue = optionValue.replace(Constant.TITLE_SHOW_NAME_HCP1, "").
                        replace(Constant.TITLE_SHOW_NAME_HCP2, "").replace(Constant.TITLE_SHOW_NAME_HCP, "")
                        .replace(Constant.TITLE_SHOW_NAME_HCP0, "");
            }
            if (HANDICAP_PLAY.contains(playId) || (StringUtils.isNotEmpty(playName) && playName.contains("让"))) {
                marketValue = StringUtils.isEmpty(marketValue) ? playOptionName : marketValue;
                if ("1".equals(playOptions) && !"0".equals(marketValue)) {
                    marketValue = !marketValue.contains("-") ? "+" + marketValue : marketValue;
                } else if ("2".equals(playOptions) && !"0".equals(marketValue)) {
                    marketValue = !marketValue.contains("-") ? "-" + marketValue : marketValue.replace("-", "+");
                } else if ("X".equalsIgnoreCase(playOptions)) {
                    marketValue = !marketValue.contains("-") ? "+" + marketValue : marketValue;
                }
                if ("X".equalsIgnoreCase(playOptions)) {
                    optionValue = optionValue + "(" + matchArray[0] + " " + marketValue + ")";
                } else {
                    optionValue += (" " + marketValue);
                }
            } else if (HALF_TOTAL.contains(playId)) {
                optionValue = assemblyHalfTotalScore(playOptions, matchArray[0], matchArray[1]);
            }
            if (StringUtils.isEmpty(optionValue) || "null".equals(optionValue)) {
                optionValue = playOptions;
            }
            if (optionValue.contains("null") || optionValue.contains(":")) {
                optionValue = optionValue.replaceAll("null", "").replaceAll(":", "-");
            }
            return language.equals(LanguageEnum.ZS.getCode()) ? (optionValue.toUpperCase().contains("OTHER") || optionValue.toUpperCase().contains("NONE") ? optionValue.toUpperCase().replaceAll("ANYOTHER", "其他").replaceAll("OTHER", "其他")
                    .replaceAll("NONE", "无") : optionValue) : optionValue;
        } catch (Exception e) {
            log.error("组装玩法盘口投注项异常!", e);
            return playOptionName;
        }
    }

    private String regxScore(String betNO, String score, String language) {
        if (StringUtils.isNotEmpty(score)) {
            try {
                String[] resultArray = score.split(",");
                for (String str : resultArray) {
                    String nameCode = str.substring(2, str.length() - 1);
                    if (StringUtils.isNumeric(nameCode)) {
                        String name;
                        if (language.equals(LanguageEnum.EN.getCode())) {
                            name = backup83OrderMixV2Mapper.getNameByNameCode(language, nameCode);
                        } else {
                            name = backup83OrderMixV2Mapper.getNameByNameCode(LanguageEnum.ZS.getCode(), nameCode);
                        }
                        score = score.replace(nameCode, name);
                    }
                }
                log.info("betNO=" + betNO + ",score=" + score);
                return score;
            } catch (Exception e) {
                log.error(betNO + "组装虚拟赛果异常!score=" + score + ",language=" + language, e);
            }
        }
        return "";
    }

    /**
     * 获取某一比分
     *
     * @param scoreBase 比分字符串、
     * @param scoreType 比分类型 如： S1,S2
     */
    private static String getNullableMatchScore(String scoreBase, String scoreType) {
        if (StringUtils.isAnyEmpty(scoreBase, scoreType)) {
            return "";
        }
        if (StringUtils.isNotBlank(scoreBase) && scoreBase.length() > 1) {
            scoreBase = scoreBase.substring(1, scoreBase.length() - 1).replaceAll("\"", "");
            String[] string = scoreBase.split(",");
            for (String s : string) {
                String b = s.split("\\|")[0];
                if (b.equals(scoreType)) {
                    return s.split("\\|")[1];
                }
            }
        }
        return "";
    }

    private String parseString(String playName, int num) {
        try {
            //正则表达式，用于匹配非数字串，+号用于匹配出多个非数字串
            String regEx = "[^0-9]+";
            Pattern pattern = compile(regEx);
            Matcher m = pattern.matcher(playName);
            //将输入的字符串中非数字部分用空格取代并存入一个字符串
            String string = m.replaceAll(" ").trim();
            //以空格为分割符在讲数字存入一个字符串数组中
            String[] str = string.split(" ");
            return str[num];
        } catch (Exception e) {
            log.error("从字符串提取数字异常!", e);
        }
        return "";
    }

    private String processSportDisplay(String desc, String sportId) {
        return sportId.equals(SportEnum.SPORT_ICEHOCKEY.getKey() + "") && StringUtils.isNotEmpty(desc) ? desc.replaceAll("局", "节") : desc;
    }

    private static String assemblyHalfTotalScore(String playOptions, String home, String away) {
        String a = "", b = "";
        if (playOptions.contains("+")) {
            String[] tempArray = playOptions.split(" ");
            a = (tempArray[0].contains("+")) ? "其他" : tempArray[0];
            b = (tempArray[1].contains("+")) ? "其他" : tempArray[1];
        } else if (playOptions.contains(":")) {
            String[] tempArray = playOptions.split(" ");
            a = tempArray[0];
            b = tempArray[1];
        } else {
            if (playOptions.length() == 2) {
                String tempA = playOptions.substring(0, 1);
                String tempB = playOptions.substring(1, 2);
                a = replaceTeamName(tempA, home, away);
                b = replaceTeamName(tempB, home, away);
            }
        }
        return a + "/" + b;
    }

    private static String replaceTeamName(String str, String home, String away) {
        return str.replaceAll("1", home).replaceAll("2", away).replaceAll("X", "平局").replaceAll("x", "平局");
    }

    /**
     * 净胜分玩法,单独处理
     *
     * @param matchArray
     * @param playOptions
     * @return
     */
    private String assemblyNetScore(String[] matchArray, String playOptions) {
        if (playOptions.toLowerCase().contains("and")) {
            String[] temStrArr = playOptions.toLowerCase().split("and");
            String league = matchArray[Integer.parseInt(temStrArr[0]) - 1];
            return league + "-净胜" + temStrArr[1];
        } else {
            return playOptions;
        }
    }

    private String settleScore(Integer xNumber, String matchScore) {
        //默认全场比分
        String defaultKey = ScoreEnum.SCORE_S1.getKey();
        int homeResult = 0, awayResult = 0, totalResult = 0;
        for (String scoreKey : fiveMinitesPlayScoreKeyEnum) {
            String tempScore = getNullableMatchScore(matchScore, scoreKey);
            if (StringUtils.isNotBlank(tempScore)) {
                String[] a = tempScore.split(":");
                homeResult = homeResult + Integer.parseInt(a[0]);
                awayResult = awayResult + Integer.parseInt(a[1]);
                totalResult = homeResult + awayResult;
                if (totalResult >= xNumber) {
                    return scoreKey;
                }
            }
        }
        return defaultKey;
    }

    private String getScoreFromFormula(String formula) {
        if (formula.contains("S")) return "";
        if (formula.contains("+")) {
            String[] formulaStr = formula.split("\\+");
            String a = formulaStr[0];
            String b = formulaStr[1];
            List<Integer> a1 = new ArrayList<>();
            List<Integer> b1 = new ArrayList<>();
            if (a.contains("2*")) {
                a = a.replace("2*", "").replace("(", "").replace(")", "");
                String[] aarry = a.split(":");
                String c = aarry[0];
                String d = aarry[1];
                a1.add(Integer.parseInt(c) * 2);
                a1.add(Integer.parseInt(d) * 2);
            } else {
                a = a.replace("(", "").replace(")", "");
                String[] aarry = a.split(":");
                String c = aarry[0];
                String d = aarry[1];
                a1.add(Integer.parseInt(c));
                a1.add(Integer.parseInt(d));
            }
            if (b.contains("2*")) {
                b = b.replace("2*", "").replace("(", "").replace(")", "");
                String[] aarry = b.split(":");
                String c = aarry[0];
                String d = aarry[1];
                b1.add(Integer.parseInt(c) * 2);
                b1.add(Integer.parseInt(d) * 2);
            } else {
                b = b.replace("(", "").replace(")", "");
                String[] aarry = b.split(":");
                String c = aarry[0];
                String d = aarry[1];
                b1.add(Integer.parseInt(c));
                b1.add(Integer.parseInt(d));
            }
            Integer e = a1.get(0) + b1.get(0);
            Integer f = a1.get(1) + b1.get(1);
            return e + ":" + f;
        } else {
            String[] formulaStr = formula.split("-");
            String a = formulaStr[0];
            String b = formulaStr[1];
            List<Integer> a1 = new ArrayList<>();
            List<Integer> b1 = new ArrayList<>();
            if (a.contains("2*")) {
                a = a.replace("2*", "").replace("(", "").replace(")", "");
                String[] aarry = a.split(":");
                String c = aarry[0];
                String d = aarry[1];
                a1.add(Integer.parseInt(c) * 2);
                a1.add(Integer.parseInt(d) * 2);
            } else {
                a = a.replace("(", "").replace(")", "");
                String[] aarry = a.split(":");
                String c = aarry[0];
                String d = aarry[1];
                a1.add(Integer.parseInt(c));
                a1.add(Integer.parseInt(d));
            }
            if (b.contains("2*")) {
                b = b.replace("2*", "").replace("(", "").replace(")", "");
                String[] aarry = b.split(":");
                String c = aarry[0];
                String d = aarry[1];
                b1.add(Integer.parseInt(c) * 2);
                b1.add(Integer.parseInt(d) * 2);
            } else {
                b = b.replace("(", "").replace(")", "");
                String[] aarry = b.split(":");
                String c = aarry[0];
                String d = aarry[1];
                b1.add(Integer.parseInt(c));
                b1.add(Integer.parseInt(d));
            }
            Integer e = a1.get(0) - b1.get(0);
            Integer f = a1.get(1) - b1.get(1);
            return e + ":" + f;
        }
    }

    private void assembleUserAndMerchantResult(List<OrderSettle> betOrderPOList, int serviceType){

        Map<String,List<OrderSettle>> userMerchantPOMapList = Maps.newHashMap();

        Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
        //把t_user表，t_merchant 抽出来单独处理
        List<OrderSettle> userMerchantPOList = backup83OrderMixV2Mapper.queryUserMerchantPOList(uidList) ;

        // 处理转账模式
        Map<String, Integer> merchantTransferModeMap = localCacheService.queryMerchantTransferModeMap("queryMerchantTransferModeMap");

        boolean userInfoSink = CollectionUtils.isNotEmpty(userMerchantPOList);

        if(userInfoSink){
            userMerchantPOMapList = userMerchantPOList.stream().collect(Collectors.groupingBy(OrderSettle::getUid));
        }

        for(OrderSettle orderSettle : betOrderPOList){
            if (ObjectUtil.isNotNull(orderSettle.getMerchantCode())) {
                orderSettle.setTransferMode(merchantTransferModeMap.get(orderSettle.getMerchantCode()));
            }
            if( 1 == serviceType){
                orderSettle.setMerchantName(orderSettle.getMerchantCode());
                orderSettle.setUserName(StringUtils.isNotEmpty(orderSettle.getFakeName()) ? orderSettle.getFakeName() : orderSettle.getUserName());
            }

            if (userInfoSink){
                List<OrderSettle> orderSettleList = userMerchantPOMapList.get(orderSettle.getUid());
                if (CollectionUtils.isNotEmpty(orderSettleList)){
                    OrderSettle order = orderSettleList.get(0);
                    orderSettle.setUserName(order.getUserName());
                    orderSettle.setFakeName(order.getFakeName());
                    orderSettle.setUserLevel(order.getUserLevel());
                    orderSettle.setCurrencyCode(order.getCurrencyCode());
                    orderSettle.setMerchantName(order.getMerchantName());
                    orderSettle.setMerchantCode(order.getMerchantCode());
                    orderSettle.setTransferMode(order.getTransferMode());
                }
            }
        }
    }

}
