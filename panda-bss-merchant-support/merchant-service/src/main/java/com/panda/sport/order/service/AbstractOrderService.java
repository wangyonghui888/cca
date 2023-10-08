package com.panda.sport.order.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.backup.mapper.MergeOrderMixMapper;
import com.panda.sport.backup.mapper.SportMapper;
import com.panda.sport.backup83.mapper.Backup83OrderMixMapper;
import com.panda.sport.backup83.mapper.Backup83TUserMapper;
import com.panda.sport.bss.mapper.*;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.HotPlayNameVO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.common.vo.user.UserOrderAllVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.*;
import static com.panda.sport.merchant.common.utils.MatchPeriodScoreEnumUtil.*;
import static java.util.regex.Pattern.compile;

@Slf4j
@Service("abstractOrderService")
public abstract class AbstractOrderService {

    @Autowired
    public Backup83OrderMixMapper backup83OrderMixMapper;

    @Autowired
    public MergeOrderMixMapper mergeOrderMixMapper;

    @Autowired
    public MerchantMapper merchantMapper;

    @Autowired
    public OrderMapper orderMapper;

    @Autowired
    public SportMapper sportMapper;

    /**
     * 分库以后 所有用户的级别都要去汇总库（备份库查询）
     *
     * @Autowired private TUserMapper tUserMapper;
     */
    @Autowired
    private BackupTUserMapper tUserMapper;

    @Autowired
    private Backup83TUserMapper backup83TUserMapper;

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private LocalCacheService localCacheService;


    @Autowired
    private RedisTemplate redisTemplate;
    private final static Set<Integer> HALF_TOTAL = Sets.newHashSet(103, 104);
    private final static Set<Integer> HANDICAP_PLAY = Sets.newHashSet(20003, 20004, 20015, 39, 46, 19, 58, 52, 64, 4, 3, 69, 71, 121, 143,
            172, 113, 163, 176, 155, 154, 181, 185, 243, 249, 278);
    public static final Set<Integer> NET_SCORE_PLAY = Sets.newHashSet(141, 200, 209, 211, 212, 219, 238);
    private final static Set<Integer> SHOW_PLAYOPTIONNAME = Sets.newHashSet(49, 55, 61, 67, 102, 170, 36, 148, 150, 151, 152);
    public static Map<String, String> userSortMap = ImmutableMap.of("createTime", "u.create_time", "betAmount", "u.bet_amount",
            "profit", "u.profit", "amount", "a.amount", "betNum", "u.total_tickets");

    /**
     * 平局投注项需要特殊处理的玩法
     */
    public static final String createTime = "createTime";
    private static final String ftpUrl = "ftp://admin:IPLEqQZWYM2viq5V@ftp.sportxxx1ceo.com";
    private static final Map<String, String> ftpMap = new HashMap<>();
    private final String exchangeRateCache = "exchange_rate_cache";

    @PostConstruct
    private void init() {
        for (Map<String, String> map : merchantMapper.queryFTPUrlList()) {
            log.info("merchant:" + map.get("merchantCode") + ",ftpUser:" + map.get("ftpUser") + ",ftpPassword:" + map.get("ftpPassword"));
            ftpMap.put(map.get("merchantCode"), "ftp://" + map.get("ftpUser") + ":" + map.get("ftpPassword") + "@ftp.sportxxx1ceo.com");
        }
    }

    public Map<String, List<OrderPreSettleDTO2>> assemblyPreSettleOrder(UserOrderVO userOrderVO) {
        //查询8天前数据
        if (userOrderVO.getBeforeEightDayData() != null && userOrderVO.getBeforeEightDayData()) {
            userOrderVO.setTXXXOrder(T_ORDER_OLD);
            userOrderVO.setTXXXDetail(T_ORDER_DETAIL_OLD);
        } else {
            userOrderVO.setTXXXOrder(T_ORDER);
            userOrderVO.setTXXXDetail(T_ORDER_DETAIL);
        }
        List<OrderPO> orderPOList = backup83OrderMixMapper.getOrderByOrderNos(userOrderVO);
        List<OrderDetailPO> orderDetailPOList = backup83OrderMixMapper.getOrderDetailByOrderNos(userOrderVO);
        List<TPreOrderDetailPO> preOrderDetailPOList = backup83OrderMixMapper.getTPreOrderDetailPO(userOrderVO);
        List<OrderPreSettleDTO2> orderPreSettleDTOList = backup83OrderMixMapper.getOrderPreSettleList(userOrderVO);
        if (CollectionUtils.isEmpty(orderPOList) || CollectionUtils.isEmpty(orderPOList)
                || CollectionUtils.isEmpty(orderDetailPOList) || CollectionUtils.isEmpty(orderPreSettleDTOList)) {
            return Maps.newHashMap();
        }
        Map<String, List<OrderPO>> orderMap = orderPOList.stream().filter(e -> e.getOrderNo() != null).collect(Collectors.groupingBy(OrderPO::getOrderNo));
        Map<String, List<OrderDetailPO>> orderDetailMap = orderDetailPOList.stream().filter(e -> e.getOrderNo() != null).collect(Collectors.groupingBy(OrderDetailPO::getOrderNo));
        Map<String, List<TPreOrderDetailPO>> preOrderDetailMap = preOrderDetailPOList.stream().filter(e -> e.getPreOrderNo() != null).collect(Collectors.groupingBy(TPreOrderDetailPO::getPreOrderNo));
        Map<String, List<OrderPreSettleDTO2>> preSettleMap = orderPreSettleDTOList.stream().filter(e -> e.getOrderNo() != null).collect(Collectors.groupingBy(OrderPreSettleDTO2::getOrderNo));
        for (Map.Entry<String, List<OrderPreSettleDTO2>> entry : preSettleMap.entrySet()) {
            String orderNo = entry.getKey();
            List<OrderPO> tempOrderList = orderMap.get(orderNo);
            List<OrderDetailPO> tempOrderDetailList = orderDetailMap.get(orderNo);
            List<OrderPreSettleDTO2> tempOrderPreSettleDTOList = entry.getValue();
            if (CollectionUtils.isEmpty(tempOrderList) || CollectionUtils.isEmpty(tempOrderPreSettleDTOList)) {
                continue;
            }
            //赔率计算
            for (OrderPreSettleDTO2 orderPreSettleDTO : tempOrderPreSettleDTOList) {
                List<TPreOrderDetailPO> tempTPreOrderDetailPOList = preOrderDetailMap.get(orderPreSettleDTO.getPreOrderNo());
                if (CollectionUtils.isEmpty(tempTPreOrderDetailPOList)) {
                    continue;
                }
                String oddFinally = null;
                Integer cancelType = null;
                //单关赔率计算
                if (tempOrderList.get(0).getSeriesType() == 1) {
                    if (tempTPreOrderDetailPOList.get(0).getOddFinally() != null) {
                        oddFinally = tempTPreOrderDetailPOList.get(0).getOddFinally();
                        cancelType = tempTPreOrderDetailPOList.get(0).getCancelType();
                    }
                } else {
                    //串关的赔率计算
//                    List<Map<String, Object>> seriesList = new ArrayList<>();
//                    Map<String, Object> tempMap = new HashMap<>();
//                    tempMap.put("seriesType", tempOrderList.get(0).getSeriesType());
//                    tempMap.put("orderNo", tempOrderList.get(0).getOrderNo());
//                    List<String> oddFinallyStrList = tempTPreOrderDetailPOList.stream()
//                            .map(TPreOrderDetailPO::getOddFinally).collect(Collectors.toList());
//                    List<Double> douOddFinally = new ArrayList<>();
//                    oddFinallyStrList.forEach(e -> douOddFinally.add(Double.parseDouble(e)));
//                    tempMap.put("managerCode", tempOrderList.get(0).getManagerCode());
//                    tempMap.put("oddsList", douOddFinally);
//                    seriesList.add(tempMap);
//                    Map<String, String> map = OddsValuesUtils.seriesTotalOddsValues(seriesList);
//                    if (map != null) {
//                        oddFinally = map.get(tempOrderList.get(0).getOrderNo());
//                    }
                }
                orderPreSettleDTO.setOddFinally(oddFinally);
                orderPreSettleDTO.setCancelType(cancelType);
                //币种  盘口类型
                String currencyCode = tempOrderList.get(0).getCurrencyCode();
                String marketType = null;
                if (CollectionUtils.isNotEmpty(tempOrderDetailList)) {
                    marketType = tempOrderDetailList.get(0).getMarketType();
                }
                if (StringUtils.isNotEmpty(currencyCode) && CurrencyTypeEnum.getCurrencyTypeEnum(currencyCode).getValue() != null) {
                    orderPreSettleDTO.setCurrency(CurrencyTypeEnum.getCurrencyTypeEnum(currencyCode).getValue());
                }
                if (StringUtils.isNotEmpty(marketType)) {
                    orderPreSettleDTO.setMarketType(marketType);
                }
            }
        }
        return preSettleMap;
    }

    protected UserOrderAllPO getAbstractUserDetail(UserOrderVO vo) {
        UserOrderAllPO po = backup83TUserMapper.getUserDetail(vo);
        if (po != null) {
            List<UserOrderAllPO> list = this.callUserReportService(vo);
            log.info("rpc调用结果:" + list);
            /*if (CollectionUtils.isNotEmpty(list)) {
                po.setLastBet(list.get(0).getLastBet());
                po.setLastBetStr(list.get(0).getLastBetStr());
            }*/
            Object rate = redisTemplate.opsForHash().get(exchangeRateCache, po.getCurrencyCode());
            if (rate != null) {
                BigDecimal rateb = BigDecimal.valueOf((Double) rate);
                if (po.getProfit() != null) {
                    po.setProfit(po.getProfit().multiply(rateb));
                }
                if (po.getBetAmount() != null) {
                    po.setBetAmount(po.getBetAmount().multiply(rateb));
                }
            } else {
                log.error("汇率查询异常！");
            }
            List<Long> uidList = new ArrayList<>();
            uidList.add(Long.parseLong(po.getUserId()));
            List<Map<Long, Long>> vipUserUpdateList = backup83TUserMapper.queryVipUpdateTime(uidList);
            if (vipUserUpdateList != null && vipUserUpdateList.size() > 0) {
                Long vipUpdateDate = vipUserUpdateList.get(0).get("modify_time");
                po.setVipUpdateTime(DateFormatUtils.format(new Date(vipUpdateDate), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        return po;
    }

    /**
     * 抽象方法实现调用报表服务
     *
     * @Param: [vo]
     * @return: java.util.List<com.panda.sport.merchant.common.po.merchant.UserOrderAllPO>
     * @date: 2020/8/23 15:35
     */
    protected abstract List<UserOrderAllPO> callUserReportService(UserOrderVO vo);

    /**
     * 查询用户,投注额需要从报表服务获取
     *
     * @Param: [vo]
     * @return: com.github.pagehelper.PageInfo<?>
     * @date: 2020/8/23 15:35
     */
    protected PageInfo<?> queryUserAllOrderList(UserOrderVO vo, String language,Integer isExport) {
        if(StringUtils.isNotBlank(vo.getUserId()) || StringUtils.isNotBlank(vo.getUserName()) || CollectionUtils.isNotEmpty(vo.getMerchantCodeList())) {
            vo.setTimeType(null);
            vo.setBeginDateTime(null);
            vo.setEndDateTime(null);
        }
        int pageSize = vo.getPageSize() == null || vo.getPageSize() > 100 ? 20 : vo.getPageSize();
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        String orderBy = StringUtils.isEmpty(vo.getOrderBy()) || !Constant.enabledSortColumnList.contains(vo.getOrderBy()) ?
                "u.modify_time" : vo.getOrderBy();
        String sort = StringUtils.isEmpty(vo.getSort()) ? Constant.DESC : vo.getSort();
        vo.setOrderBy(userSortMap.get(orderBy) == null ? orderBy : userSortMap.get(orderBy));

        vo.setSort(sort);
        int total=0;
        if(!(null !=isExport && 1==isExport)){
            vo.setStart((pageNum - 1) * pageSize);
            vo.setEnd(pageNum * pageSize);
            total = tUserMapper.countAllUserList(vo);
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotal(total);
        if (total > 0 || (null !=isExport && 1==isExport)) {
            List<UserOrderAllPO> resultList = tUserMapper.queryAllUserList(vo);
            pageInfo.setList(execUserInfo(resultList,vo));
        }
        return pageInfo;
    }

    public List<UserOrderAllPO> execUserInfo(List<UserOrderAllPO> resultList,UserOrderVO vo){
        if (CollectionUtils.isNotEmpty(resultList)) {
            List<String> idList = resultList.stream().map(UserOrderAllPO::getUserId).collect(Collectors.toList());
            vo.setUserIdList(idList);
            List<Long> uidList = new ArrayList<>();
            for (String uid : idList) {
                uidList.add(Long.parseLong(uid));
            }
            List<Map<Long, Long>> vipUpdateTimeList = tUserMapper.queryVipUpdateTime(uidList);
            for (UserOrderAllPO po : resultList) {
                for (Map<Long, Long> userVipUpdateTime : vipUpdateTimeList) {
                    Long vipUpdateTime = userVipUpdateTime.get("modify_time");
                    String uid1 = userVipUpdateTime.get("uid").toString();
                    String uid2 = po.getUserId();
                    if (vipUpdateTime != null && uid1.equals(uid2)) {
                        po.setVipUpdateTime(DateFormatUtils.format(new Date(vipUpdateTime), "yyyy-MM-dd HH:mm:ss"));
                        break;

                    }
                }
                Long lastBet = po.getLastBet();
                Long lastLogin = po.getLastLogin();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (lastBet != null && lastBet > 0) {
                    String lastStr = sdf.format(lastBet);
                    po.setLastBetStr(lastStr);
                }
                if(lastLogin != null){
                    String lastLoginStr = sdf.format(lastLogin);
                    po.setLastLoginStr(lastLoginStr);
                }
            }
            List<UserOrderAllPO> tempList = this.callUserReportService(vo);
            if (CollectionUtils.isNotEmpty(tempList)) {
                Map<String, UserOrderAllPO> appleMap = tempList.stream().collect(Collectors.toMap(UserOrderAllPO::getUserId, a -> a, (k1, k2) -> k1));
                for (UserOrderAllPO po : resultList) {
                    String userId = po.getUserId();
                    UserOrderAllPO newPo = appleMap.get(userId);
                    if (newPo == null) {
                        continue;
                    }
                        /*Long lastBet = po.getLastBet();
                        Long lastLogin = po.getLastLogin();
                        if (lastBet != null) {
                            po.setLastBetStr(getTimeStr(lastBet, language));
                        }
                        if (lastLogin != null) {
                            po.setLastLoginStr(getTimeStr(lastLogin, language));
                        }
                        po.setLastBet(newPo.getLastBet());
                        po.setLastBetStr(newPo.getLastBetStr());*/
                    Object rate = redisTemplate.opsForHash().get(exchangeRateCache, po.getCurrencyCode());
                    if (rate != null) {
                        BigDecimal rateb = BigDecimal.valueOf((Double) rate);
                        if (po.getProfit() != null) {
                            po.setProfit(po.getProfit().multiply(rateb));
                        }
                        if (po.getBetAmount() != null) {
                            po.setBetAmount(po.getBetAmount().multiply(rateb));
                        }
                    } else {
                        log.error("汇率查询异常！");
                    }
                }
            }
        }
        return resultList;
    }

    private String getTimeStr(Long time, String language) {
        if (time != null) {
            Date now = new Date();
            long min = (now.getTime() - time) / (1000 * 60);
            if (min <= 1) {
                return language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "刚刚" : "Just a moment";
            } else if (min <= 60) {
                return min + (language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "分钟" : "min");
            } else if (min <= 60 * 24) {
                return min / 60 + (language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "小时" : "hour");
            } else {
                return min / (60 * 24) + (language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "天" : "day");
            }
        }
        return "";
    }

    protected UserOrderAllVO getUserAllOrder(String userId) {
        UserOrderVO vo = new UserOrderVO();
        vo.setUserId(userId);
        UserOrderAllPO po = tUserMapper.getUserDetail(vo);
        UserOrderAllVO userOrderAllVO = null;
        if (po != null) {
            userOrderAllVO = new UserOrderAllVO();
            UserOrderAllPO userOrderAllPO = this.callReportSelectByUser(userId);
            if (userOrderAllPO != null) {
                BeanUtils.copyProperties(userOrderAllPO, userOrderAllVO);
                Integer order = userOrderAllPO.getBetNum();
                Long startTime = po.getCreateTime();
                int newL = (int) ((System.currentTimeMillis() - startTime) / (1000 * 3600 * 24));
                userOrderAllVO.setBetAvgDay(newL == 0 || order == null ? order : order / newL);
                userOrderAllVO.setBetAmount(po.getBetAmount());
                userOrderAllVO.setProfit(po.getProfit());
                userOrderAllVO.setBetNum(po.getBetNum());
            } else {
                BeanUtils.copyProperties(po, userOrderAllVO);
            }
            if (userOrderAllVO.getBetAmount() != null) {
                userOrderAllVO.setProfitRate(userOrderAllVO.getProfit().divide(userOrderAllVO.getBetAmount(), 4, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(100)));
            }
        }
        return userOrderAllVO;
    }

    /**
     * 抽象方法,调用报表服务获取玩家投注额度
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.po.merchant.UserOrderAllPO
     * @date: 2020/8/23 15:36
     */
    protected abstract UserOrderAllPO callReportSelectByUser(String userId);

    /**
     * 注单统计
     *
     * @Param: [betOrderVO]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:37
     */
    protected Object abstractGetStatistics(BetOrderVO betOrderVO, String databaseSwitch) {
        //查询8天前数据
        if (betOrderVO.getBeforeEightDayData() != null && betOrderVO.getBeforeEightDayData()) {
            betOrderVO.setTXXXOrder(T_ORDER_OLD);
            betOrderVO.setTXXXDetail(T_ORDER_DETAIL_OLD);
            betOrderVO.setTXXXSettle(T_SETTLE_OLD);
            betOrderVO.setTXXXInternational(T_ORDER_INTERNATIONALIZE_OLD);
        } else {
            betOrderVO.setTXXXOrder(T_ORDER);
            betOrderVO.setTXXXDetail(T_ORDER_DETAIL);
            betOrderVO.setTXXXSettle(T_SETTLE);
            betOrderVO.setTXXXInternational(T_ORDER_INTERNATIONALIZE);
        }
        log.info("abstractGetStatistics获取统计注单数据:" + betOrderVO);
        Long uid = betOrderVO.getUserId();
        Map<String, Object> reslut;
        List<OrderStatisticsPO> list;
        if (SETTLE_TIME_FILTER.equals(betOrderVO.getFilter())) {
            reslut = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.getSettleStatistics(betOrderVO) :
                    mergeOrderMixMapper.getStatistics(betOrderVO);
            list = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.getOrderSettleStatistics(betOrderVO) :
                    mergeOrderMixMapper.getOrderStatistics(betOrderVO);
            this.convertCurrencyCode(list);
            reslut.put("orderStatisticsPO", list);
        } else if (MATCH_BEGIN_FILTER.equals(betOrderVO.getFilter())) {
            reslut = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.getBeginTimeStatistics(betOrderVO) :
                    mergeOrderMixMapper.getStatistics(betOrderVO);
            list = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.getBeginTimeOrderStatistics(betOrderVO) :
                    mergeOrderMixMapper.getOrderStatistics(betOrderVO);
            this.convertCurrencyCode(list);
            reslut.put("orderStatisticsPO", list);
        } else {
            reslut = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.getStatistics(betOrderVO) :
                    mergeOrderMixMapper.getStatistics(betOrderVO);
            list = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.getOrderStatistics(betOrderVO) :
                    mergeOrderMixMapper.getOrderStatistics(betOrderVO);
            this.convertCurrencyCode(list);
            reslut.put("orderStatisticsPO", list);
        }
        /*if (uid != null) {
            log.info("获取用户信息！uid = {}", betOrderVO.getUserId());
            UserPO userPO = tUserMapper.getUserInfo(uid);
            if (userPO != null) {
                log.info("获取用户汇率信息！currencyRatePO = {}", userPO.getCurrencyCode());
                CurrencyRatePO currencyRatePO = backup83OrderMixMapper.queryCurrencyRateByCode(userPO.getCurrencyCode());
                if (currencyRatePO != null) {
                    log.info("开始处理汇率问题！");
                    try {
                        BigDecimal betAmount = (BigDecimal) reslut.get("betAmount");
                        BigDecimal sumProfitAmount = (BigDecimal) reslut.get("sumProfitAmount");
                        BigDecimal orderValidBetMoney = (BigDecimal) reslut.get("orderValidBetMoney");
                        BigDecimal sumValidBetMoney = (BigDecimal) reslut.get("sumValidBetMoney");
                        BigDecimal rate = BigDecimal.valueOf(currencyRatePO.getRate());
                            reslut.put("betAmount", betAmount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            reslut.put("sumProfitAmount", sumProfitAmount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            reslut.put("orderValidBetMoney", orderValidBetMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                            reslut.put("sumValidBetMoney", sumValidBetMoney.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP));
                    } catch (Exception e) {
                        log.error("处理汇率异常！", e);
                    }
                    log.info("结束处理汇率问题！");
                }
            }
        }*/
        return reslut;
    }

    private void convertCurrencyCode(List<OrderStatisticsPO> list) {
        for (OrderStatisticsPO po : list){
            String currencyCn = CurrencyUtils.convertCurrencyCode(po.getCurrencyCode());
            po.setCurrencyCode(currencyCn);
        }
    }

    /**
     * 热门玩法
     *
     * @Param: [betOrderVO]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:37
     */
    protected Object abstractQueryHotPlayList(Integer sportId, String language) {
        List<HotPlayNameVO> list = localCacheService.getPlayNameList(sportId, language);
        return list;
    }

    /**
     * 组装注单查询参数,美东时间
     *
     * @param vo
     * @return
     * @throws Exception
     */
    protected BetOrderVO assemblyQueryOrderParamET(UserOrderVO vo) throws Exception {
        //查询8天前数据
        if (vo.getBeforeEightDayData() != null && vo.getBeforeEightDayData()) {
            vo.setTXXXOrder(T_ORDER_OLD);
            vo.setTXXXDetail(T_ORDER_DETAIL_OLD);
            vo.setTXXXSettle(T_SETTLE_OLD);
            vo.setTXXXInternational(T_ORDER_INTERNATIONALIZE_OLD);
        } else {
            vo.setTXXXOrder(T_ORDER);
            vo.setTXXXDetail(T_ORDER_DETAIL);
            vo.setTXXXSettle(T_SETTLE);
            vo.setTXXXInternational(T_ORDER_INTERNATIONALIZE);
        }
        String dateType = vo.getDateType();
        String date = StringUtils.isEmpty(vo.getDate()) ? DateFormatUtils.format(new Date(), "yyyyMMdd") : vo.getDate();
        LocalTime MAX = LocalTime.of(11, 59, 59, 999_999_999);
        LocalTime min = LocalTime.of(12, 0, 0, 0);
        BetOrderVO betOrderVO = new BetOrderVO();
        BeanUtils.copyProperties(vo, betOrderVO);
        int pageSize = vo.getPageSize() == null || vo.getPageSize() > 100 ? 20 : vo.getPageSize();
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        betOrderVO.setPageNo(pageNum);
        betOrderVO.setSize(pageSize);
        betOrderVO.setStart((pageNum - 1) * pageSize);
        betOrderVO.setEnd(pageNum * pageSize);
        if (StringUtils.isNotEmpty(dateType) && dateType.equals(Constant.DAY)) {
            Date start1 = DateUtils.parseDate(date, "yyyyMMdd");
            String start1str = DateFormatUtils.format(start1, "yyyy-MM-dd HH:mm:ss");
            String end1str = DateFormatUtils.format(start1, "yyyy-MM-dd HH:mm:ss");
            betOrderVO.setStartTime(start1str);
            betOrderVO.setEndTime(end1str);
            List<Integer> orderStatusList = new ArrayList<>();
            orderStatusList.add(0);
            orderStatusList.add(1);
            betOrderVO.setOrderStatusList(orderStatusList);
        } else if (StringUtils.isNotEmpty(dateType) && dateType.equals(Constant.WEEK)) {
            String year = date.substring(0, 4);
            String week = date.substring(4, 6);
            int wk = Integer.parseInt(week) + 1;
            LocalDate dateTimeStart = LocalDate.parse(year + "-" + wk + "-1", DateTimeFormatter.ofPattern("YYYY-ww-e", Locale.CHINA));
            LocalDate dateTimeEnd = LocalDate.parse(year + "-" + wk + "-7", DateTimeFormatter.ofPattern("YYYY-ww-e", Locale.CHINA));
            LocalDateTime todayStart = LocalDateTime.of(dateTimeStart, min);
            String startTime = todayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime todayEnd = LocalDateTime.of(dateTimeEnd, MAX);
            String endTime = todayEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            betOrderVO.setStartTime(startTime);
            betOrderVO.setEndTime(endTime);
        } else if (StringUtils.isNotEmpty(dateType) && dateType.equals(Constant.MONTH)) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            LocalDate dateTimeStart = LocalDate.parse(year + "-" + month + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dateTimeEnd = LocalDate.parse(year + "-" + month + "-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime todayStart = LocalDateTime.of(dateTimeStart, min);
            String startTime = todayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime todayEnd = LocalDateTime.of(dateTimeEnd, MAX);
            String endTime = todayEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            betOrderVO.setStartTime(startTime);
            betOrderVO.setEndTime(endTime);
        } else if (StringUtils.isNotEmpty(dateType) && dateType.equals(Constant.YEAR)) {
            LocalDate dateTimeStart = LocalDate.parse(date + "-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dateTimeEnd = LocalDate.parse(date + "-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime todayStart = LocalDateTime.of(dateTimeStart, min);
            String startTime = todayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime todayEnd = LocalDateTime.of(dateTimeEnd, MAX);
            String endTime = todayEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            betOrderVO.setStartTime(startTime);
            betOrderVO.setEndTime(endTime);
        }
        Date startDate = DateUtils.parseDate(betOrderVO.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        Date endDate = DateUtils.parseDate(betOrderVO.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        if (StringUtil.isBlankOrNull(vo.getTimeZone()) || UTC8.equals(vo.getTimeZone())) {
            startDate = DateUtils.setMilliseconds(DateUtils.setSeconds(DateUtils.setMinutes(DateUtils.setHours(startDate, 0), 0), 0), 0);
            endDate = DateUtils.setMilliseconds(DateUtils.setSeconds(DateUtils.setMinutes(DateUtils.setHours(startDate, 23), 59), 59), 999);
        } else if (EZ.equals(vo.getTimeZone())) {
            startDate = DateUtils.setMilliseconds(DateUtils.setSeconds(DateUtils.setMinutes(DateUtils.setHours(startDate, 12), 0), 0), 0);
            endDate = DateUtils.setMilliseconds(DateUtils.setSeconds(DateUtils.setMinutes(DateUtils.setHours(DateUtils.addDays(endDate, 1), 11), 59), 59), 999);
        }
        betOrderVO.setStartTime(DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss"));
        betOrderVO.setEndTime(DateFormatUtils.format(endDate, "yyyy-MM-dd HH:mm:ss"));
        betOrderVO.setSortby(vo.getSort());
        betOrderVO.setSqlOrder(vo.getOrderBy());
        return betOrderVO;
    }
    /**
     * 组装注单预约查询参数,自然日
     *
     * @param vo
     * @return
     * @throws Exception
     */
    protected BetOrderVO assemblyQueryAppointmentList(UserOrderVO vo, String language) {

        //查询注单预约表信息
        vo.setTXXXOrder(T_PRO_BET_ORDER);
        vo.setTXXXDetail(T_PRO_BET_ORDER_DETAIL);
        BetOrderVO betOrderVO = new BetOrderVO();
        BeanUtils.copyProperties(vo, betOrderVO);
        int pageSize = vo.getPageSize() == null || vo.getPageSize() > 100 ? 20 : vo.getPageSize();
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        betOrderVO.setPageNo(pageNum);
        betOrderVO.setSize(pageSize);
        betOrderVO.setStart((pageNum - 1) * pageSize);
        betOrderVO.setEnd(pageNum * pageSize);
        betOrderVO.setLanguage(language);
        if (vo.getUserId() != null) {
            betOrderVO.setUserId(Long.parseLong(vo.getUserId()));
        }
        if (vo.getMatchId() != null) {
            betOrderVO.setMatchId(Long.parseLong(vo.getMatchId()));
        }
        betOrderVO.setDatabaseSwitch(vo.getDatabaseSwitch());
        return betOrderVO;
    }



    /**
     * 组装注单查询参数,自然日
     *
     * @param vo
     * @return
     * @throws Exception
     */
    protected BetOrderVO assemblyQueryOrderParamUTC8(UserOrderVO vo, String language) {

        //查询8天前数据
        if (vo.getBeforeEightDayData() != null && vo.getBeforeEightDayData()) {
            vo.setTXXXOrder(T_ORDER_OLD);
            vo.setTXXXDetail(T_ORDER_DETAIL_OLD);
            vo.setTXXXSettle(T_SETTLE_OLD);
            vo.setTXXXInternational(T_ORDER_INTERNATIONALIZE_OLD);
        } else {
            vo.setTXXXOrder(T_ORDER);
            vo.setTXXXDetail(T_ORDER_DETAIL);
            vo.setTXXXSettle(T_SETTLE);
            vo.setTXXXInternational(T_ORDER_INTERNATIONALIZE);
        }
        BetOrderVO betOrderVO = new BetOrderVO();
        BeanUtils.copyProperties(vo, betOrderVO);
        int pageSize = vo.getPageSize() == null || vo.getPageSize() > 100 ? 20 : vo.getPageSize();
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        betOrderVO.setPageNo(pageNum);
        betOrderVO.setSize(pageSize);
        betOrderVO.setStart((pageNum - 1) * pageSize);
        betOrderVO.setEnd(pageNum * pageSize);
        betOrderVO.setLanguage(language);
        if (vo.getUserId() != null) {
            betOrderVO.setUserId(Long.parseLong(vo.getUserId()));
        }
        if (vo.getMatchId() != null) {
            betOrderVO.setMatchId(Long.parseLong(vo.getMatchId()));
        }
        betOrderVO.setDatabaseSwitch(vo.getDatabaseSwitch());
        return betOrderVO;
    }


    /**
     * 查询注单预约具体实现,投注时间1,结算时间3,比赛时间2
     *
     * @param betOrderVO
     * @param databaseSwitch
     * @return
     * @throws Exception
     */
    protected Map<String, Object> abstractQueryAppointmentList(BetOrderVO betOrderVO, String databaseSwitch) throws Exception {
        assemblyQueryTime(betOrderVO, databaseSwitch);
        int pageNum = betOrderVO.getPageNo();
        int pageSize = betOrderVO.getSize();
        List<PreOrderSettle> betOrderPOList = null;
        Integer totCnt;
        long executeStartL = System.currentTimeMillis();
        String filter = StringUtils.isEmpty(betOrderVO.getFilter()) ? BET_TIME_FILTER : betOrderVO.getFilter();
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder()) == null ?
                    Constant.ID : Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortby(sort);
            totCnt = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.countPreBetOrderList(betOrderVO) : getCountFromCache(betOrderVO);
            log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",countPreBetOrderList=" + (System.currentTimeMillis() - executeStartL) + ",totCnt=" + totCnt);
            if (totCnt > 0) {
                betOrderPOList = backup83OrderMixMapper.queryPreBetOrderList(betOrderVO);
                betOrderPOList = assemblyPreSettleScoreAndPlayOptionName(betOrderPOList,betOrderVO.getLanguage());
                assemblyPreOddsValueByMarket(betOrderPOList);
                log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",queryBetOrderList=" + (System.currentTimeMillis() - executeStartL));
            }
        Map<String, Object> param = new HashMap<>();
        param.put("list", betOrderPOList);
        param.put("pageNum", pageNum);
        param.put("pageSize", pageSize);
        param.put("total", totCnt);
        return param;
    }


    /**
     * 查询注单具体实现,投注时间1,结算时间3,比赛时间2
     *
     * @param betOrderVO
     * @param databaseSwitch
     * @return
     * @throws Exception
     */
    protected Map<String, Object> abstractQueryUserOrderList(BetOrderVO betOrderVO, String databaseSwitch) throws Exception {
        assemblyQueryTime(betOrderVO, databaseSwitch);
        int pageNum = betOrderVO.getPageNo();
        int pageSize = betOrderVO.getSize();
        List<OrderSettle> betOrderPOList = null;
        List<OrderSettle> userMerchantPOList = null;
        Integer totCnt;
        long executeStartL = System.currentTimeMillis();
        String filter = StringUtils.isEmpty(betOrderVO.getFilter()) ? BET_TIME_FILTER : betOrderVO.getFilter();
        if (BET_TIME_FILTER.equals(filter)) {
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder()) == null ?
                    Constant.ID : Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortby(sort);
            totCnt = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.countBetOrderList(betOrderVO) : getCountFromCache(betOrderVO);
            log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",countBetOrderList=" + (System.currentTimeMillis() - executeStartL) + ",totCnt=" + totCnt);
            if (totCnt > 0) {
                    betOrderPOList = assemblySettleScoreAndPlayOptionName((databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.queryBetOrderList(betOrderVO) :
                            mergeOrderMixMapper.queryBetOrderList(betOrderVO)), betOrderVO.getLanguage());
                if(CollectionUtils.isNotEmpty(betOrderPOList)) {
                    Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
                    //把t_user表，t_merchant 抽出来单独处理
                    userMerchantPOList = (databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.queryUserMerchantPOList(uidList) :
                            mergeOrderMixMapper.queryUserMerchantPOList(uidList));
                }
                log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",queryBetOrderList=" + (System.currentTimeMillis() - executeStartL));
            }
        } else if (SETTLE_TIME_FILTER.equals(filter)) {
            betOrderVO.setOrderStatus(null);
            betOrderVO.setOrderStatusList(null);
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
                    "s1.id" : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
            totCnt = databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.countSettledOrderList(betOrderVO) :
                    getCountFromCache(betOrderVO);
            log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",countByCriteria=" + (System.currentTimeMillis() - executeStartL) + ",totCnt=" + totCnt);
            if (totCnt > 0) {
                betOrderVO.setSqlOrder(orderBy);
                betOrderVO.setSortby(sort);
                betOrderPOList = assemblySettleScoreAndPlayOptionName(searchByCriteria(betOrderVO, databaseSwitch), betOrderVO.getLanguage());
              if(databaseSwitch.equals(REALTIME_TABLE)  &&  CollectionUtils.isNotEmpty(betOrderPOList)){
                      Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
                      userMerchantPOList = (databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.queryUserMerchantPOList(uidList) :
                              mergeOrderMixMapper.queryUserMerchantPOList(uidList));
              }
                log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",searchByCriteria=" + (System.currentTimeMillis() - executeStartL));
            }
        } else {
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
                    Constant.ID : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
            totCnt =databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.countLiveOrderList(betOrderVO) :
                    getCountFromCache(betOrderVO);
            log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",countLiveOrderList=" + (System.currentTimeMillis() - executeStartL) + ",totCnt=" + totCnt);
            if (totCnt > 0) {
                betOrderVO.setSqlOrder(orderBy);
                betOrderVO.setSortby(sort);
                betOrderPOList = assemblySettleScoreAndPlayOptionName((databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.queryLiveOrderList(betOrderVO) :
                        mergeOrderMixMapper.queryBetOrderList(betOrderVO)), betOrderVO.getLanguage());
               if(databaseSwitch.equals(REALTIME_TABLE) &&  CollectionUtils.isNotEmpty(betOrderPOList)){
                       Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
                       userMerchantPOList = (databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.queryUserMerchantPOList(uidList) :
                               mergeOrderMixMapper.queryUserMerchantPOList(uidList));
               }
                log.info("databaseSwitch=" + databaseSwitch + ",filter=" + filter + ",queryLiveOrderList=" + (System.currentTimeMillis() - executeStartL));
            }
        }

        //组装betOrderPOList
        if(CollectionUtils.isNotEmpty(userMerchantPOList)){
            betOrderPOList= assembleBetOrderList(betOrderPOList,userMerchantPOList);
        }
        if (ObjectUtil.isNotEmpty(betOrderPOList)) {
            Map<String, Integer> merchantTransferModeMap = localCacheService.queryMerchantTransferModeMap("queryMerchantTransferModeMap");
            for (OrderSettle orderSettle : betOrderPOList) {
                if (ObjectUtil.isNotNull(orderSettle.getMerchantCode())) {
                    orderSettle.setTransferMode(merchantTransferModeMap.get(orderSettle.getMerchantCode()));
                }
            }
            this.assemblyOddsValueByMarket(betOrderPOList);
        }
        Map<String, Object> param = new HashMap<>();
        param.put("list", betOrderPOList);
        param.put("pageNum", pageNum);
        param.put("pageSize", pageSize);
        param.put("total", totCnt);
        return param;
    }

    /**
     * 盘口值和最终赔率吧转换
     */
    public void assemblyOddsValueByMarket(List<OrderSettle> betOrderPOList) {
        for (OrderSettle betOrderDTO : betOrderPOList) {
            for (OrderSettleDetail po : betOrderDTO.getOrderDetailList()) {
                final String marketType = po.getMarketType();
                BigDecimal odds = BigDecimal.valueOf(po.getOddsValue()).setScale(4, RoundingMode.HALF_UP);
                if (marketType.equals(MarketTypeEnum.HK.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.HK.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertHK(odds)).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                } else if (marketType.equals(MarketTypeEnum.US.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.US.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertUS(odds)).setScale(0, RoundingMode.HALF_UP)).doubleValue());
                } else if (marketType.equals(MarketTypeEnum.MY.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.MY.getMarketType());
                    po.setOddFinally((OddsConvertUtil.oddsEUConvertMY(odds)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else if (marketType.equals(MarketTypeEnum.ID.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.ID.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertID(odds)).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                } else if (marketType.equals(MarketTypeEnum.GB.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.GB.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertUk(odds)).setScale(2, RoundingMode.HALF_UP)).doubleValue());
                } else if (marketType.equals(MarketTypeEnum.EU.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.EU.getMarketType());
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.DOWN);
                    po.setOddFinally(Double.valueOf(df.format(po.getOddsValue())));
                }
            }
        }
    }
 /**
     * 盘口值和最终赔率吧转换
     */
    public void assemblyPreOddsValueByMarket(List<PreOrderSettle> betOrderPOList) {
        for (PreOrderSettle betOrderDTO : betOrderPOList) {
            for (PreOrderSettleDetail po : betOrderDTO.getPreOrderSettleDetail()) {
                final String marketType = po.getMarketType();
                BigDecimal odds = BigDecimal.valueOf(po.getOddsValue()).setScale(4, RoundingMode.HALF_UP);
                if (marketType.equals(MarketTypeEnum.HK.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.HK.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertHK(odds)).setScale(2, RoundingMode.HALF_UP)).toString());
                } else if (marketType.equals(MarketTypeEnum.US.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.US.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertUS(odds)).setScale(0, RoundingMode.HALF_UP)).toString());
                } else if (marketType.equals(MarketTypeEnum.MY.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.MY.getMarketType());
                    po.setOddFinally((OddsConvertUtil.oddsEUConvertMY(odds)).setScale(2, RoundingMode.HALF_UP).toString());
                } else if (marketType.equals(MarketTypeEnum.ID.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.ID.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertID(odds)).setScale(2, RoundingMode.HALF_UP)).toString());
                } else if (marketType.equals(MarketTypeEnum.GB.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.GB.getMarketType());
                    po.setOddFinally(((OddsConvertUtil.oddsEUConvertUk(odds)).setScale(2, RoundingMode.HALF_UP)).toString());
                } else if (marketType.equals(MarketTypeEnum.EU.getMarketType())) {
                    po.setMarketType(MarketTypeEnum.EU.getMarketType());
                    po.setOddFinally((odds.setScale(2, RoundingMode.HALF_UP)).toString());
                }
            }
        }
    }





    public List<TicketOrderSettle> assembleBetOrderListES(List<TicketOrderSettle> orderList, List<OrderSettle> userMerchantPOList){
        for(TicketOrderSettle orderSettle : orderList){
           A: for(OrderSettle order:userMerchantPOList) {
                    if (orderSettle.getUid().equals(order.getUid())){
                        orderSettle.setUserName(order.getUserName());
                        orderSettle.setFakeName(order.getFakeName());
                        orderSettle.setUserLevel(order.getUserLevel());
                        orderSettle.setCurrencyCode(order.getCurrencyCode());
                        orderSettle.setMerchantName(order.getMerchantName());
                        orderSettle.setMerchantCode(order.getMerchantCode());
                        orderSettle.setTransferMode(order.getTransferMode());
                        break A;
                    }
                  }
              }
        return orderList;
    }




    public List<OrderSettle> assembleBetOrderList(List<OrderSettle> orderList, List<OrderSettle> userMerchantPOList){
        for(OrderSettle orderSettle : orderList){
            A: for(OrderSettle order:userMerchantPOList) {
                if (orderSettle.getUid().equals(order.getUid())){
                    orderSettle.setUserName(order.getUserName());
                    orderSettle.setFakeName(order.getFakeName());
                    orderSettle.setUserLevel(order.getUserLevel());
                    orderSettle.setCurrencyCode(order.getCurrencyCode());
                    orderSettle.setMerchantName(order.getMerchantName());
                    orderSettle.setMerchantCode(order.getMerchantCode());
                    orderSettle.setTransferMode(order.getTransferMode());
                    break A;
                }
            }
        }


        return orderList;
    }

    private Integer getCountFromCache(BetOrderVO betOrderVO) {
        StringBuilder key = new StringBuilder("orderNo=" + betOrderVO.getOrderNo() + "betNo=" + betOrderVO.getBetNo() + "startTimeL=" + betOrderVO.getStartTimeL() + "endTimeL=" + betOrderVO.getEndTimeL() + "userName=" +
                betOrderVO.getUserName() + "userId=" + betOrderVO.getUserId() + "filter=" + betOrderVO.getFilter() + "currency=" + betOrderVO.getCurrency() + "merchantCode=" + betOrderVO.getMerchantCode() +
                "marketType=" + betOrderVO.getMarketType() + "language=" +
                betOrderVO.getLanguage() + "fakeName=" +
                betOrderVO.getFakeName() +"vip=" + betOrderVO.getUserVip() + "sportId=" + betOrderVO.getSportId() + "playId=" + betOrderVO.getPlayId() + "settleTimes=" + betOrderVO.getSettleTimes() +
                "orderStatus=" + betOrderVO.getOrderStatus() + "seriesType=" +
                betOrderVO.getSeriesType() + betOrderVO.getSettleType() + betOrderVO.getMaxOdds() + betOrderVO.getMinOdds() + "managerCode=" + betOrderVO.getManagerCode() + "betAmount=" + betOrderVO.getMaxBetAmount() + betOrderVO.getMinBetAmount() +
                "matchId=" + betOrderVO.getManagerCode() + "matchType=" + betOrderVO.getMatchType() + "profit=" + betOrderVO.getMaxProfit() + betOrderVO.getMinProfit()
                + "settleStatus=" + betOrderVO.getSettleStatus() + "settleCancel=" + betOrderVO.getSettleCancle() + "cashout=" + betOrderVO.getEnablePreSettle() + "accountTag=" + betOrderVO.getAccountTag() + "tournamentId=" + betOrderVO.getTournamentId() + "ip=" + betOrderVO.getIp());
        List<String> merchantCodeList = betOrderVO.getMerchantCodeList();
        if (CollectionUtils.isNotEmpty(merchantCodeList)) {
            key.append("merchantCodeList=");
            for (String code : merchantCodeList) {
                key.append(code);
            }
        }
        List<Integer> playList = betOrderVO.getPlayIdList();
        if (CollectionUtils.isNotEmpty(playList)) {
            key.append("playList=");
            for (Integer code : playList) {
                key.append(code);
            }
        }
        List<Integer> orderStatusList = betOrderVO.getOrderStatusList();
        if (CollectionUtils.isNotEmpty(orderStatusList)) {
            key.append("orderStatusList=");
            for (Integer code : orderStatusList) {
                key.append(code);
            }
        }
        List<Integer> outComeList = betOrderVO.getOutComeList();
        if (CollectionUtils.isNotEmpty(outComeList)) {
            key.append("outComeList=");
            for (Integer code : outComeList) {
                key.append(code);
            }
        }
        Integer total = LocalCacheService.ticketSearchMap.getIfPresent(key.toString());
        log.info("缓存获取getCountFromCache:" + total);
        if (total == null) {
            total = mergeOrderMixMapper.countBetOrderList(betOrderVO);
            LocalCacheService.ticketSearchMap.put(key.toString(), total);
        }
        return total;
    }

    private int countByCriteria(BetOrderVO betOrderVO, String databaseSwitch) {
        return databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.countSettledOrderList(betOrderVO) :
                getCountFromCache(betOrderVO);
    }

    private List<OrderSettle> searchByCriteria(BetOrderVO betOrderVO, String databaseSwitch) {
        return databaseSwitch.equals(REALTIME_TABLE) ? backup83OrderMixMapper.querySettledOrderList(betOrderVO) :
                mergeOrderMixMapper.queryBetOrderList(betOrderVO);
    }

    /**
     * 注单查询 限制查询90天
     *
     * @Param: [betOrderVO]
     * @date: 2020/8/23 15:40
     */
    protected void assemblyQueryTime(BetOrderVO betOrderVO, String databaseSwitch) throws Exception {
        //查询8天前数据
        if (betOrderVO.getBeforeEightDayData() != null && betOrderVO.getBeforeEightDayData()) {
            betOrderVO.setTXXXOrder(T_ORDER_OLD);
            betOrderVO.setTXXXDetail(T_ORDER_DETAIL_OLD);
            betOrderVO.setTXXXSettle(T_SETTLE_OLD);
            betOrderVO.setTXXXInternational(T_ORDER_INTERNATIONALIZE_OLD);
        } else {
            betOrderVO.setTXXXOrder(T_ORDER);
            betOrderVO.setTXXXDetail(T_ORDER_DETAIL);
            betOrderVO.setTXXXSettle(T_SETTLE);
            betOrderVO.setTXXXInternational(T_ORDER_INTERNATIONALIZE);
        }
        Long matchId = betOrderVO.getMatchId();
        String startTime = betOrderVO.getStartTime();
        String endTime = betOrderVO.getEndTime();
        Long userId = betOrderVO.getUserId();
        List<Long> userIdList = betOrderVO.getUserIdList();
        String orderNo = betOrderVO.getOrderNo();
        String userName = betOrderVO.getUserName();
        Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss"),
                endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"), before, now = new Date();
        long pStartTimeL = startDate.getTime();
        long pEndTimeL = endDate.getTime();
        //世界杯期间注单查询限制：导出3天、查询限制到7天
        //120天限制放大到365天；
        if (MATCH_BEGIN_FILTER.equals(betOrderVO.getFilter()) && StringUtils.isEmpty(userName) &&
                userId == null && StringUtils.isEmpty(orderNo) && betOrderVO.getOrderNoList()==null && matchId == null && CollectionUtils.isEmpty(userIdList)) {
            before = DateUtils.addDays(now, -3);
            betOrderVO.setBetStartTimeL(before.getTime());
        }
        if (databaseSwitch.equals(REALTIME_TABLE) && StringUtils.isEmpty(userName) &&
                userId == null && StringUtils.isEmpty(orderNo) && betOrderVO.getOrderNoList()==null  && matchId == null && CollectionUtils.isEmpty(userIdList)) {
            if (pEndTimeL - pStartTimeL > 1000 * 60 * 60 * 24 * 3) {
                pEndTimeL = pStartTimeL + 1000 * 60 * 60 * 24 * 3;
            }
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
        if (betOrderVO.getMinBetAmount() != null) {
            betOrderVO.setMinBetAmount(BigDecimal.valueOf(betOrderVO.getMinBetAmount().doubleValue() * 100));
        }
        if (betOrderVO.getMaxBetAmount() != null) {
            betOrderVO.setMaxBetAmount(BigDecimal.valueOf(betOrderVO.getMaxBetAmount().doubleValue() * 100));
        }
        if (betOrderVO.getMinProfit() != null) {
            betOrderVO.setMinProfit(BigDecimal.valueOf(betOrderVO.getMinProfit().doubleValue() * 100));
        }
        if (betOrderVO.getMaxProfit() != null) {
            betOrderVO.setMaxProfit(BigDecimal.valueOf(betOrderVO.getMaxProfit().doubleValue() * 100));
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

    /**
     * 报表导出
     *
     * @Param: [betOrderVO]
     * @return: boolean
     * @date: 2020/8/23 15:40
     */
    protected void abstractUserOrderExport(UserOrderVO vo, String filter, HttpServletResponse response) throws Exception {
/*        List<OrderPO> resultList = abstractExport(vo);
        String fileName = "用户投注统计.csv";
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
        FileCopyUtils.copy(exportUsersToCsv(resultList, filter), response.getOutputStream());*/
    }

    protected void abstractUserOrder(UserOrderVO vo, HttpServletResponse response) throws Exception {
        String fileName = "用户注单.csv";
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        Map<String, Object> result = this.abstractQueryUserOrderList(assemblyQueryOrderParamET(vo), "1");
        List<OrderSettle> resultList = (List<OrderSettle>) result.get("list");
        if (CollectionUtils.isEmpty(resultList)) {
            log.warn("at this very time,you just cant export the tickets!!!");
            return;
        }
        FileCopyUtils.copy(exportOrderToCsv(resultList), outputStream);
    }

    public Map abstractExportOrderAccountHistory(String username, UserOrderVO vo, String merchantCode, String language, String databaseSwitch) {
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("code", "0000");
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        try {
            BetOrderVO betOrderVO = assemblyQueryOrderParamUTC8(vo, language);
            assemblyQueryTime(betOrderVO, databaseSwitch);
            String filter = StringUtils.isEmpty(betOrderVO.getFilter()) ? "1" : betOrderVO.getFilter();
            betOrderVO.setFilter(filter);
            int total = countOrder(filter, betOrderVO, databaseSwitch);
            //8月28对时间的限制注单导出单个文件限制放到100万条,原来是50万
            if (total > 1000000) {
                resultMap.put("code", "0001");
                resultMap.put("msg", "导出数据超过最大限制1000000条");
                resultMap.put("total", total);
                resultMap.put("url", StringUtils.isEmpty(merchantCode) ? ftpUrl : ftpMap.get(merchantCode));
            } else {
                String bean = "orderAccountExportServiceImpl";
                merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "注单账变查询导出_" : "AccountChangeExport_", merchantCode, username, JSON.toJSONString(betOrderVO),
                        language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-注单查询" : "Report Center-Tickets Center", bean, null);
            }
        } catch (RuntimeException e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        } catch (Exception e) {
            log.error("abstractExportOrderAccountHistory异常！", e);
        }
        return resultMap;
    }

    protected Map abstractExportOrder(UserOrderVO vo, String username, String merchantCode, String language, String databaseSwitch) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0000");
        try {
            BetOrderVO betOrderVO = assemblyQueryOrderParamUTC8(vo, language);
            assemblyQueryTime(betOrderVO, databaseSwitch);
            String filter = StringUtils.isEmpty(betOrderVO.getFilter()) ? "1" : betOrderVO.getFilter();
            betOrderVO.setFilter(filter);
            if (MATCH_BEGIN_FILTER.equals(betOrderVO.getFilter())) {
                if (betOrderVO.getMatchId() == null && CollectionUtil.isEmpty(betOrderVO.getUserIdList()) && betOrderVO.getUserId() == null) {
                    map.put("code", "0001");
                    map.put("msg", "按开赛时间导出查询必须输入赛事ID或用户ID");
                    map.put("total", 0);
                    map.put("url", null);
                    return map;
                }
                if (betOrderVO.getUserId() != null) {
                    List<Long> list = new ArrayList<>();
                    list.add(betOrderVO.getUserId());
                    betOrderVO.setUserIdList(list);
                }
            }
            int total = countOrder(filter, betOrderVO, databaseSwitch);
            if (total > merchantFileService.getExportDataSize()) {
                map.put("code", "0001");
                map.put("msg", "导出数据超过最大限制" + merchantFileService.getExportDataSize() + "条");
                map.put("total", total);
                map.put("url", StringUtils.isEmpty(merchantCode) ? ftpUrl : ftpMap.get(merchantCode));
            } else {
                String bean = "orderExportServiceImpl";
                if (total > MerchantFileService.SPLIT_FILE_DATE_SIZE) {
                    //走大文件
                    bean = "orderMaxExportServiceImpl";
                }
                // 负余额导出单独处理
                if (betOrderVO.getAccountTag() != null && betOrderVO.getAccountTag().equals(1)) {
                    bean = "orderAccountTagExportServiceImpl";
                }
                //创建导出任务
                merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "注单查询导出_" : "Tickets Exporting_"),
                        merchantCode, username, JSON.toJSONString(betOrderVO), (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-注单查询" : "Report Center-Tickets Center"), bean, total);
                map.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "注单导出任务创建成功,请在文件列表等候下载！"
                        : "The tickets exporting task has been created,please click at the Download Task menu to check!");
            }

        } catch (Exception e) {
            log.error("abstractExportOrder异常！", e);
            map.put("code", "0002");
            map.put("msg", e.getMessage());
        }
        return map;
    }



    protected Map abstractExportOrderES(BetOrderVO betOrderVO,String username, String merchantCode, String language, String databaseSwitch,int total) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0000");
        try {
            assemblyQueryTime(betOrderVO, databaseSwitch);
            String filter = StringUtils.isEmpty(betOrderVO.getFilter()) ? "1" : betOrderVO.getFilter();
            betOrderVO.setFilter(filter);
            if (total > merchantFileService.getExportDataSize()) {
                map.put("code", "0001");
                map.put("msg", "导出数据超过最大限制" + merchantFileService.getExportDataSize() + "条");
                map.put("total", total);
                map.put("url", StringUtils.isEmpty(merchantCode) ? ftpUrl : ftpMap.get(merchantCode));
            } else {
                String bean = "ticketOrderExportESServiceImpl";
                //创建导出任务
                merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "注单查询ES导出_" : "Tickets ES Exporting_"),
                        merchantCode, username, JSON.toJSONString(betOrderVO), (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-注单查询ES" : "Report Center-Tickets Center ES"), bean, total);
                map.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "注单导出任务创建成功,请在文件列表等候下载！"
                        : "The tickets exporting task has been created,please click at the Download Task menu to check!");
            }

        } catch (Exception e) {
            log.error("abstractExportOrder异常！", e);
            map.put("code", "0002");
            map.put("msg", e.getMessage());
        }
        return map;
    }



    protected int countOrder(String filter, BetOrderVO betOrderVO, String databaseSwitch) {
        log.info("countOrder:" + betOrderVO.getStartTimeL() + "," + betOrderVO.getEndTimeL() + "," + betOrderVO.getFilter() +
                ",outcomeList=" + betOrderVO.getOutComeList() + ",matchType=" + betOrderVO.getMatchType());
        if (BET_TIME_FILTER.equals(filter)) {
            return REALTIME_TABLE.equals(databaseSwitch) ? backup83OrderMixMapper.countBetOrderList(betOrderVO) : getCountFromCache(betOrderVO);
            //return backup83OrderMixMapper.countBetOrderList(betOrderVO);
        } else if (SETTLE_TIME_FILTER.equals(filter)) {
            return REALTIME_TABLE.equals(databaseSwitch) ? backup83OrderMixMapper.countSettledOrderList(betOrderVO) :
                    getCountFromCache(betOrderVO);
            //return backup83OrderMixMapper.countSettledOrderList(betOrderVO);
        } else {
            return getCountFromCache(betOrderVO);
            //return REALTIME_TABLE.equals(databaseSwitch) ? backup83OrderMixMapper.countLiveOrderList(betOrderVO) : getCountFromCache(betOrderVO);
            //return backup83OrderMixMapper.countLiveOrderList(betOrderVO);
        }
    }

    protected byte[] exportOrderToCsv(List<OrderSettle> resultList) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        int i = 0;
        for (OrderSettle order : resultList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", order.getUserName());
            rowData.put("3", "OB体育");
            rowData.put("4", order.getMerchantName());
            rowData.put("5", CurrencyTypeEnum.optGetDescription(order.getCurrencyCode()));
            rowData.put("6", order.getOrderDetailList().get(0).getSportName());
            rowData.put("7", getObj(order.getProfitAmount()));
            rowData.put("8", getObj(order.getOrderAmountTotal()));
            rowData.put("9", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMarketType()) : " ");
            rowData.put("10", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMarketValue()) + "\t" : " ");
            rowData.put("11", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getOddFinally()) : " ");
            rowData.put("12", getOrderStatus(order.getOrderStatus(), LanguageEnum.ZS.getCode()));
            rowData.put("13", getObj(order.getCreateTimeStr()));
            rowData.put("14", getObj(order.getOrderDetailList().get(0).getBeginTimeStr()));
            rowData.put("15", getObj(order.getSettleTimeStr()));
            rowData.put("16", order.getOrderNo() + "\t");
            rowData.put("17", getObj(order.getSeriesValue()));
            rowData.put("18", settleStatusMap.get(order.getOutcome()) == null ? "未结算" : settleStatusMap.get(order.getOutcome()));
            rowData.put("19", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMatchId()) : " ");
            rowData.put("20", order.getSeriesType() == 1 ? getObj(order.getOrderDetailList().get(0).getMatchName()) : " ");
            rowData.put("21", order.getSeriesType() == 1 ? order.getOrderDetailList().get(0).getMatchInfo() : " ");
            Integer matchType = order.getOrderDetailList().get(0).getMatchType();
            rowData.put("22", order.getSeriesType() == 1 ? "".equals(getObj(matchType)) ? "" : (matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) : " ");
            rowData.put("23", order.getSeriesType() == 1 ? order.getOrderDetailList().get(0).getPlayOptionName() + "\t" : " ");
            rowData.put("24", order.getSeriesType() == 1 ? order.getOrderDetailList().get(0).getPlayName() : " ");
            rowData.put("25", getObj(order.getUid()) + "\t");
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "序号");
        header.put("2", "用户名称");
        header.put("3", "平台名称");
        header.put("4", "商户名称");
        header.put("5", "用户币种");
        header.put("6", "赛种");
        header.put("7", "盈亏");
        header.put("8", "下注金额");
        header.put("9", "盘口类型");
        header.put("10", "盘口值");
        header.put("11", "赔率");
        header.put("12", "注单状态");
        header.put("13", "投注时间");
        header.put("14", "开赛时间");
        header.put("15", "结算时间");
        header.put("16", "注单号");
        header.put("17", "串关值");
        header.put("18", "结算状态");
        header.put("19", "赛事ID");
        header.put("20", "联赛名称");
        header.put("21", "比赛对阵");
        header.put("22", "注单类型");
        header.put("23", "投注项名称");
        header.put("24", "玩法名称");
        header.put("25", "用户ID");
        return CsvUtil.exportCSV(header, exportData);
    }

    protected static String getOrderStatus(Integer orderStatus, String language) {
        return language.equals(LanguageEnum.ZS.getCode()) ? orderStatusMap.get(orderStatus) : orderStatusEnMap.get(orderStatus);
    }

    protected static Object getObj(Object o) {
        if (o == null) {
            return "";
        }
        return o;
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
            rowData.put("3", orderPO.getUserName());
            rowData.put("4", orderPO.getTime());
            rowData.put("5", orderPO.getMerchantName());
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

    protected Map<String, Object> geBetTodaytMap(MerchantOrderDayPO merchantOrderDaypo) {
        if (merchantOrderDaypo == null) {
            return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        BigDecimal amount = new BigDecimal(0);
        map.put("betAmount", merchantOrderDaypo.getValidBetAmount() == null ? amount :
                merchantOrderDaypo.getValidBetAmount().setScale(2, BigDecimal.ROUND_FLOOR));
        map.put("betNum", merchantOrderDaypo.getValidTickets() == null ? amount :
                merchantOrderDaypo.getValidTickets());
        map.put("settleAmount", merchantOrderDaypo.getSettleBetAmount() == null ? amount :
                merchantOrderDaypo.getSettleBetAmount().setScale(2, BigDecimal.ROUND_FLOOR));
        map.put("profit", merchantOrderDaypo.getSettleProfit() == null ? amount :
                merchantOrderDaypo.getSettleProfit().setScale(2, BigDecimal.ROUND_FLOOR));
        map.put("orderValidBetMoney", merchantOrderDaypo.getOrderValidBetMoney() == null ? amount :
                merchantOrderDaypo.getOrderValidBetMoney().setScale(2, BigDecimal.ROUND_FLOOR));
        map.put("settleValidBetMoney", merchantOrderDaypo.getSettleValidBetMoney() == null ? amount :
                merchantOrderDaypo.getSettleValidBetMoney().setScale(2, BigDecimal.ROUND_FLOOR));
        return map;
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



    protected void assemblySettleScoreAndPlayOptionNameES(TicketOrderSettle settle,Map<String,String> matchSorceMap, String language) {
            Integer seriesType = settle.getSeriesType();
            Integer managerCode = settle.getManagerCode();
        log.info("assemblyQueryTime.matchSorce:" + matchSorceMap);
            for (TicketOrderSettleDetail detail : settle.getOrderDetailList()) {
                    Integer sportId =Integer.valueOf(detail.getSportId());
                    Integer playId =Integer.valueOf(detail.getPlayId());
                    String matchScore=matchSorceMap.get(detail.getMatchId());
                    String settleScore=detail.getSettleScore();
                     detail.setMatchScore(matchScore);
                    Long childPlayId=detail.getChildPlayId()==null?null:Long.parseLong(detail.getChildPlayId());
                    Integer matchType =detail.getMatchType()==null?null:Integer.valueOf(detail.getMatchType());
                    Integer cancelType =detail.getCancelType()==null?null:Integer.valueOf(detail.getCancelType());
                if (matchType == 3) {
                    if (StringUtils.isNotEmpty(settleScore) && NumberUtils.isDigits(settleScore)) {
                        detail.setBeginTime(settleScore);
                        detail.setSettleScore(null);
                    } else {
                        detail.setBeginTime(null);
                        detail.setBeginTimeStr(null);
                    }
                }
                detail.setRiskEvent(getRiskEvent(detail.getRiskEvent(), cancelType));
                if (cancelType != null && 18 == cancelType) {
                    detail.setRiskEvent(filterRemark(detail.getRemark()));
                }
                detail.setSettleScore("1".equals(detail.getBetStatus()) && Integer.valueOf(detail.getBetResult()) != 0 ?
                        assemblySettleScoreES(detail.getSportId(),detail.getPlayId(),detail.getPlayName(),matchScore,childPlayId,detail.getBetNo(),settleScore, language, managerCode) : "");
                detail.setMarketValue(!language.equals(LanguageEnum.ZS.getCode()) || settle.getCreateTime() <= 1605843003000L ?
                        assemblyMarketValueES(detail.getPlayOptions(),detail.getPlayName(),detail.getPlayOptionName(),detail.getMarketValue(),playId,detail.getHomeName(),detail.getAwayName(), language) : detail.getPlayOptionName());
                if (managerCode != null && managerCode == 3) {
                    if (sportId == 1002 || sportId == 1011) {
                        detail.setMatchInfo(detail.getBatchNo());
                    }
                    if (StringUtils.isNotBlank(detail.getRiskEvent()) && detail.getRiskEvent().contains("||")) {
                        String[] matchDayBatchNo = detail.getRiskEvent().split("\\|\\|");
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
    }


    private List<PreOrderSettle> assemblyPreSettleScoreAndPlayOptionName(List<PreOrderSettle> orderList, String language) {
        if (orderList == null || orderList.size() < 1) {
            return null;
        }
       return orderList;
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

    private String assemblyPlayName(OrderSettleDetail detail) {
        String playName = detail.getPlayName();
        String home = detail.getHomeName();
        String away = detail.getAwayName();
        String marketValue = detail.getMarketValue();
        playName = playName.replace(Constant.TITLE_SHOW_NAME_COMPETITOR1_AHCP, home);
        playName = playName.replace(Constant.TITLE_SHOW_NAME_COMPETITOR2_AHCP, away);
        playName = playName.replace(Constant.TITLE_SHOW_NAME_POINT, marketValue);
        playName = playName.replace(Constant.TITLE_SHOW_NAME_GOAL, marketValue);
        playName = playName.replace(Constant.TITLE_SHOW_NAME_TOTAL, marketValue);
        return playName;
    }

    private String assemblyMarketValue(OrderSettleDetail orderSettleDetail, String language) {
        String playOptions = orderSettleDetail.getPlayOptions(), optionValue = orderSettleDetail.getOptionValue(),
                playName = orderSettleDetail.getPlayName(), marketValue = orderSettleDetail.getMarketValue(),
                playOptionName = StringUtils.isEmpty(orderSettleDetail.getPlayOptionName()) ? "" : orderSettleDetail.getPlayOptionName();
        Integer playId = orderSettleDetail.getPlayId();
        if (language.equals(LanguageEnum.ZS.getCode()) && playOptionName.toUpperCase().contains("OTHER")) {
            playOptionName = playOptionName.toUpperCase().replaceAll("ANYOTHER", "其他").replaceAll("OTHER", "其他");
        }
        if (SHOW_PLAYOPTIONNAME.contains(playId)) {
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




    private String assemblyMarketValueES(String playOptions,String playName,String playOptionName,String marketValue,Integer playId,String homeName,String awayName, String language) {
        String optionValue =null;
        if (language.equals(LanguageEnum.ZS.getCode()) && playOptionName.toUpperCase().contains("OTHER")) {
            playOptionName = playOptionName.toUpperCase().replaceAll("ANYOTHER", "其他").replaceAll("OTHER", "其他");
        }
        if (SHOW_PLAYOPTIONNAME.contains(playId)) {
            return playOptionName;
        }
        try {
            String[] matchArray = {homeName, awayName};
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
/*

    private void assemblyRMBBetAndTimeStr(List<OrderSettle> betOrderPOList) {
        if (betOrderPOList == null || betOrderPOList.size() < 1) {
            return;
        }
        for (OrderSettle po : betOrderPOList) {
            if (po.getLocalBetAmount() == null || po.getLocalBetAmount().compareTo(BigDecimal.ZERO) == 0) {
                po.setLocalBetAmount(po.getOrderAmountTotal());
                po.setLocalProfitAmount(po.getProfitAmount());
                po.setLocalSettleAmount(po.getSettleAmount());
            }
        }
    }
*/

    private String regxScore(String betNO, String score, String language) {
        if (StringUtils.isNotEmpty(score)) {
            try {
                String[] resultArray = score.split(",");
                for (String str : resultArray) {
                    String nameCode = str.substring(2, str.length() - 1);
                    if (StringUtils.isNumeric(nameCode)) {
                        String name;
                        if (language.equals(LanguageEnum.EN.getCode())) {
                            name = backup83OrderMixMapper.getNameByNameCode(language, nameCode);
                        } else {
                            name = backup83OrderMixMapper.getNameByNameCode(LanguageEnum.ZS.getCode(), nameCode);
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




    /**
     * 组装结算比分,MatchPeriodScoreEnumUtil 映射s_match_info 的match_test_score
     * strategy:ADD 比分加总,HOME_AWAY_ADD 主队和客队各自加总,JOIN 拼接
     *
     * @Param: [orderVO]
     * @return: java.lang.String
     * @date: 2020/8/23 15:41
     */
    private String assemblySettleScoreES(String sportId,String playId,String playName,String matchScore,Long childPlayId,String betNO,String settleScore, String language, Integer managerCode) {
        try {
            String sportIdPlayIdStr = sportId + "p" + playId;
            String scoreKey;
            String score = "", desc = "";
            language = StringUtils.isEmpty(language) ? LanguageEnum.ZS.getCode() : language;
            log.info("assemblySettleScore:betNo:{},sportIdPlayIdStr:{},score:{},childPlayId:{}",betNO,sportIdPlayIdStr,matchScore,childPlayId);
            if (managerCode != null && managerCode == 3 && !sportId.equals("1001") && !sportId.equals("1004")) {
                return regxScore(betNO, matchScore, language);
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

    /**
     * 提取数字,
     *
     * @param score
     * @return
     */
    private String regxScore(String score) {
        if (StringUtils.isNotEmpty(score)) {
            Pattern pattern = Pattern.compile("[^0-9,]");
            Matcher matcher = pattern.matcher(score);
            return matcher.replaceAll("");
        }
        return "";
    }

    /**
     * 获取所有的子商户code
     *
     * @param filterList 需要过滤的商户List
     * @return 所有的子商户code集合
     */
    public static Set<String> getAllParentMerchantCodeSet(List<MerchantOrderDayPO> filterList) {
        //将结果集中查找代理商或渠道商户的code
        Set<String> merchantCodeSet = new HashSet<>();
        for (MerchantOrderDayPO po : filterList) {
            if (AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(po.getAgentLevel()) || AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(po.getAgentLevel())) {
                merchantCodeSet.add(po.getMerchantCode());
            }
        }
        return merchantCodeSet;
    }

    /**
     * 设置渠道商户的time为了重新排序
     */
    public static Map<String, Integer> setParentTimeForOrder(List<MerchantOrderDayPO> filterList) {
        Map<String, Integer> resultMap = new HashMap<>();
        for (int i = 0; i < filterList.size(); i++) {
            MerchantOrderDayPO temp = filterList.get(i);
            temp.setTime(i);
            if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(temp.getAgentLevel())) {
                temp.setTime(i * 100);
            }
            resultMap.put(temp.getMerchantCode(), temp.getTime());
        }
        return resultMap;
    }

    /**
     * 设置渠道商户的time为了重新排序
     */
    public static List<String> getAllChildMerchantCode(List<MerchantOrderDayPO> filterList) {
        List<String> resultList = new ArrayList<>();
        for (MerchantOrderDayPO temp : filterList) {
            if (AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(temp.getAgentLevel())) {
                resultList.add(temp.getMerchantCode());
            }
        }
        return resultList;
    }

    /**
     * 设置重复的用户数量，settleUsers betSettledUsers
     *
     * @param resourceList                   需要过滤的原数据list
     * @param resultData                     已经统计出的结果data
     * @param parentWithChildMerchantCodeMap 根据上级查找所有的子集商户code
     */
    public static void resetSettleUsers(List<MerchantOrderDayPO> resourceList, Map<String, Object> resultData, Map<String, List<String>> parentWithChildMerchantCodeMap) {
        for (MerchantOrderDayPO po : resourceList) {
            if (AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(po.getAgentLevel()) || AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(po.getAgentLevel())) {
                List<String> childCode = parentWithChildMerchantCodeMap.get(po.getMerchantCode());
                Integer totalSettleUser = 0;
                Integer totalBetSettledUsers = 0;
                if (CollectionUtils.isNotEmpty(childCode)) {
                    for (String code : childCode) {
                        Map temp = (Map<String, Object>) resultData.get(code);
                        if (null != temp) {
                            if (null != temp.get("settleUsers")) {
                                totalSettleUser = (Integer) temp.get("settleUsers") + totalSettleUser;
                            }
                            if (null != temp.get("betSettledUsers")) {
                                totalBetSettledUsers = (Integer) temp.get("betSettledUsers") + totalBetSettledUsers;
                            }
                        }
                    }
                }
                po.setSettleUsers(totalSettleUser);
                po.setBetSettledUsers(totalBetSettledUsers);
                po.setValidBetUsers(totalBetSettledUsers);
            }
        }
    }

    private String processSportDisplay(String desc, String sportId) {
        return sportId.equals(SportEnum.SPORT_ICEHOCKEY.getKey() + "") && StringUtils.isNotEmpty(desc) ? desc.replaceAll("局", "节") : desc;
    }

    public String settleScore(Integer xNumber, String matchScore) {
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
}
