package com.panda.sport.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.backup.mapper.BackupOrderMapper;
import com.panda.sport.backup.mapper.BackupOrderMixMapper;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.backup83.mapper.Backup83OrderMixMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.OddsValuesUtils;
import com.panda.sport.merchant.common.utils.RegexUtils;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.common.vo.user.OrderVO;
import com.panda.sport.merchant.common.vo.user.UserFakeVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.config.OrderRedisUtilBean;
import com.panda.sport.order.feign.MerchantApiClient;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.AbstractOrderService;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.MerchantOrderService;
import com.panda.sport.order.service.UserOrderService;
import com.panda.sport.order.util.ExecutorInstance;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;
import static com.panda.sport.merchant.common.constant.Constant.REALTIME_TABLE;

@Slf4j
@RefreshScope
@Service
public class OrderServiceImpl extends AbstractOrderService implements UserOrderService {

    private static final String TIME_PATTEN = "yyyyMMdd";
    private static final String ORDER_HISTORY = "Order_history:";
    private static final String UNSETTLE_ORDER_CACHE = "UNSETTLE_ORDERS_CACHE:0:";

    @Value("${order.history.cache.ttl:3600}")
    private Long orderCacheTtl;

    @Value("${order.history.batch.count:20}")
    private int orderBatchCount;

    @Value("${order.history.groupcode:s}")
    private String execGroupCode;

    @Autowired
    private MerchantReportClient rpcClient;

    @Autowired
    private MerchantApiClient merchantApiClient;

    /**
     * 分库以后 所有用户的级别都要去汇总库（备份库查询）
     *
     * @Autowired private TUserMapper tUserMapper;
     */
    @Autowired
    private BackupTUserMapper tUserMapper;

    @Autowired
    private BackupOrderMapper backupOrderMapper;

    @Autowired
    private BackupOrderMixMapper backupOrderMixMapper;

    @Autowired
    private Backup83OrderMixMapper backup83OrderMixMapper;

    @Autowired
    private OrderRedisUtilBean orderRedisUtil;
    @Autowired
    private MerchantOrderService merchantOrderService;

    @Autowired
    private MerchantLogService merchantLogService;

    public static Cache<String, List<String>> merchantByGroupCacheMap = null;

    @Autowired
    private LocalCacheService localCacheService;

    @PostConstruct
    private void init() {
        merchantByGroupCacheMap = Caffeine.newBuilder()
                .maximumSize(1_000) // 设置缓存的最大容量
                .expireAfterWrite(24, TimeUnit.HOURS)
                .recordStats() // 开启缓存统计
                .build();
    }

    @Data
    private static class User {
        private Long uid;
        private String userName;
        private String vipUpdateTime;
    }

    /**
     * 查询用户列表
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:12
     */
    @Override
    public Response<Object> userOrderAll(String userId) {
        return Response.returnSuccess(this.getUserAllOrder(userId));
    }

    /**
     * 查询月用户投注
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:12
     */
    @Override
    public Response userOrderMonth(String userId) {
        return rpcClient.userOrderMonth(userId);
    }

    /**
     * 查询用户30天数据
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:12
     */
    @Override
    public Response userOrderOneMonthDays(String userId, Integer time) {
        return rpcClient.userOrderMonthDays(userId, time);
    }

    /**
     * 查询用户盈利
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:12
     */
    @Override
    public Response userProfitAll(String userId) {
        return rpcClient.getPlayerProfit(userId);
    }

    /**
     * 查询用户全生命周期数据
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:12
     */
    @Override
    public Response<?> queryUserBetList(UserOrderVO vo, String language, Integer isExport) {
        try {
            return Response.returnSuccess(this.queryUserAllOrderList(vo, language, isExport));
        } catch (Exception e) {
            log.error("UserController.queryUserBetList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 查询热门玩法名称
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:12
     */
    @Override

    public Response<Object> queryHotPlayName(Integer sportId, String language) {
        try {
            return Response.returnSuccess(this.abstractQueryHotPlayList(sportId, language));
        } catch (Exception e) {
            log.error("查询热门玩法名称列表!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    public Response<List<UserVipVO>> findUserInfo(UserVipVO vo) {
        List<String> userIdList = Arrays.asList(vo.getUserIds().split(","));
        List<Long> userIds = userIdList.stream().map(Long::parseLong).collect(Collectors.toList());
        try {
            List<UserVipVO> idList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(userIds)) {
                idList = tUserMapper.listUserInfoById(userIds);
            }
            return Response.returnSuccess(idList);
        } catch (Exception e) {
            log.error("查询热门玩法名称列表!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询用户报表
     *
     * @Param: [userId]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:12
     */
    @Override
    public Response<?> queryUserBetListByTime(UserOrderVO vo) {
        Response<?> response = rpcClient.queryUserBetListByTime(vo);
        if (vo.getSportId() != null) {
            Map<String, Object> map = (Map<String, Object>) response.getData();
            List<?> resultList = (List<?>) map.get("list");
            ObjectMapper mapper = new ObjectMapper();
            List<UserOrderDayPO> filterList = mapper.convertValue(resultList, new TypeReference<List<UserOrderDayPO>>() {
            });
            for (UserOrderDayPO po : filterList) {
                if ((po.getBetAmount() != null && po.getBetAmount().compareTo(BigDecimal.ZERO) > 0) &&
                        (po.getValidBetAmount() == null || po.getValidBetAmount().compareTo(BigDecimal.ZERO) <= 0)) {
                    po.setValidBetAmount(po.getBetAmount());
                }
                if ((po.getBetNum() != null && po.getBetNum() > 0) &&
                        (po.getValidTickets() == null || po.getValidTickets() <= 0)) {
                    po.setValidTickets(po.getBetNum());
                }
            }
            map.put("list", filterList);
        }
        return response;
    }

    /**
     * 查询用户报表
     */
    @Override
    public Response<?> listUserBetGroupByUser(UserOrderVO vo) {
        try {
            vo.setMerchantName(null);
            if (StringUtils.isNotBlank(vo.getFakeName())) {
                List<String> idList = tUserMapper.listIdByUserName(vo.getFakeName());
                if (idList.size() == 0) {
                    return Response.returnSuccess(new PageInfo<>());
                }
                vo.setUserIdList(idList);
            }
            Response<?> response = rpcClient.listUserBetGroupByUser(vo);
            if (response.getData() != null) {
                Map<String, Object> map = (Map<String, Object>) response.getData();
                List<?> resultList = (List<?>) map.get("list");
                ObjectMapper mapper = new ObjectMapper();
                List<UserOrderDayPO> filterList = mapper.convertValue(resultList, new TypeReference<List<UserOrderDayPO>>() {
                });
                if (CollectionUtils.isNotEmpty(filterList)) {
                    List<Long> userIdList = new ArrayList<>();
                    for (UserOrderDayPO po : filterList) {
                        userIdList.add(Long.parseLong(po.getUserId()));
                    }
                    List<Map<String, Object>> userNameList = tUserMapper.queryUserListByIdList(userIdList);

                    List<Map<Long, Long>> userVipUpdateTimeList = tUserMapper.queryVipUpdateTime(userIdList);

                    Map<Long, User> userNameMap = new HashMap<>();
                    for (Map<String, Object> mapVo : userNameList) {
                        Long uid = (Long) mapVo.get("uid");
                        String fakeName = (String) mapVo.get("fakeName");
                        User user = new User();
                        user.setUserName(fakeName);
                        user.setUid(uid);


                        userNameMap.put(uid, user);
                    }
                    for (UserOrderDayPO po : filterList) {
                        po.setMerchantName(po.getMerchantCode());
                        if (null != vo.getSportId()) {
                            if (po.getBetAmount().compareTo(BigDecimal.ZERO) > 0 && po.getValidBetAmount().compareTo(BigDecimal.ZERO) <= 0) {
                                po.setValidBetAmount(po.getBetAmount());
                            }
                            if (po.getBetNum() > 0 && po.getValidTickets() <= 0) {
                                po.setValidTickets(po.getBetNum());
                            }
                        }
                        User user = userNameMap.get(Long.valueOf(po.getUserId()));
                        if (!Objects.isNull(user)) {
                            String userName = user.getUserName();
                            if (StringUtils.isNotEmpty(userName)) {
                                po.setUserName(userName);
                            }
                        }


                        for (Map<Long, Long> userVipUpdateTime : userVipUpdateTimeList) {
                            Long vipUpdateTime = userVipUpdateTime.get("modify_time");
                            Long uid1 = userVipUpdateTime.get("uid");
                            if (!Objects.isNull(user)) {
                                Long uid2 = user.getUid();
                                if (vipUpdateTime != null && uid1.equals(uid2)) {
                                    po.setVipUpdateTime(DateFormatUtils.format(new Date(vipUpdateTime), "yyyy-MM-dd HH:mm:ss"));
                                    break;
                                }
                            }
                        }
                    }
                    map.put("list", filterList);
                }
            }
            return response;
        } catch (Exception e) {
            log.error("UserController.queryUserBetListByTime,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 导出用户统计报表Excel
     * filer 时间维度数据:(1投注,2结算,3比赛时间)
     *
     * @param response
     * @param request
     * @throws Exception
     */
    public void exportUserBetList(HttpServletResponse response, HttpServletRequest request) {
        try {
            String merchantName = request.getHeader("merchantName");
            String orderBy = org.springframework.util.StringUtils.isEmpty(request.getParameter("orderBy")) || !Constant.enabledSortColumnList.contains(request.getParameter("orderBy")) ?
                    Constant.enabledSortColumnList.get(0) : request.getParameter("orderBy");
            String sort = org.springframework.util.StringUtils.isEmpty(request.getParameter("sort")) ? "desc" : request.getParameter("sort");
            int pageSize = org.springframework.util.StringUtils.isEmpty(request.getParameter("pageSize")) ? 20 : Integer.parseInt(request.getParameter("pageSize"));
            int pageNum = org.springframework.util.StringUtils.isEmpty(request.getParameter("pageNum")) ? 1 : Integer.parseInt(request.getParameter("pageNum"));
            Integer offset = (pageNum - 1) * pageSize;
            String dateType = request.getParameter("dateType");
            String date = request.getParameter("date");
            String userId = request.getParameter("userId");
            String filter = org.springframework.util.StringUtils.isEmpty(request.getParameter("filter")) ? "1" : request.getParameter("filter");
            String sportId = request.getParameter("sportId");
            dateType = org.springframework.util.StringUtils.isEmpty(dateType) ? Constant.DAY : dateType;
            date = org.springframework.util.StringUtils.isEmpty(date) ? DateFormatUtils.format(new Date(), "yyyyMMdd") : date;
            Long time = Long.valueOf(date);
            String user = request.getParameter("user");
            UserOrderVO vo = new UserOrderVO();
            vo.setUser(user);
            vo.setMerchantName(merchantName);
            vo.setDateType(dateType);
            vo.setDate(date);
            vo.setOrderBy(orderBy);
            vo.setSort(sort);
            if (StringUtils.isNotEmpty(userId)) {
                vo.setUserId(userId);
            }
            if (StringUtils.isNotEmpty(sportId)) {
                vo.setSportId(Integer.parseInt(sportId));
            }
            vo.setTime(time);
            List<?> list = rpcClient.queryUserReport(vo);
            String fileName = "用户投注统计.csv";
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            FileCopyUtils.copy(exportUsersToCsv(list, filter), response.getOutputStream());
        } catch (Exception e) {
            log.error("UserController.export error!", e);
        }
    }

    @Autowired
    private MerchantFileService merchantFileService;

    /**
     * 导出用户报表
     *
     * @Param: [response, request]
     * @return: void
     * @date: 2020/8/23 15:20
     */
    @Override
    public void export(String username, HttpServletRequest request, UserOrderVO vo) {
        String orderBy = org.springframework.util.StringUtils.isEmpty(request.getParameter("orderBy")) || !Constant.enabledSortColumnList.contains(request.getParameter("orderBy")) ?
                Constant.enabledSortColumnList.get(0) : request.getParameter("orderBy");
        vo.setOrderBy(orderBy);
        if (vo.getSort() == null) {
            vo.setSort("desc");
        }
        if (StringUtils.isEmpty(vo.getFilter())) {
            vo.setFilter("1");
        }
        if (vo.getDateType() == null) {
            vo.setDateType(Constant.DAY);
        }
        vo.setPageSize(10000);
        vo.setPageNum(1);
        String language = vo.getLanguage();
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "用户投注统计_" : "User Report Exporting_"), null, username, JSON.toJSONString(vo),
                (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-用户投注统计" : "Report Center-User Report"), "groupByUserReportExportServiceImpl");
    }


    /**
     * 投注用户管理导出
     *
     * @Param: [response, request]
     * @return: void
     * @date: 2020/8/23 15:20
     */
    @Override
    public void queryUserBetListExport(String username, HttpServletRequest request, UserOrderVO vo) {
        String language = vo.getLanguage();
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "投注用户管理导出_" : "Bet User Report Exporting_"), null, username, JSON.toJSONString(vo),
                (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-投注用户管理导出" : "Report Center-Bet-User Report"), "betUserReportExportServiceImpl");
    }

    /**
     * 查询用户注单
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override

    public Response<Object> queryUserOrderList(UserOrderVO vo) {
        try {
            return Response.returnSuccess(this.abstractQueryUserOrderList(assemblyQueryOrderParamET(vo), StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    "1" : vo.getDatabaseSwitch()));
        } catch (Exception e) {
            log.error("查询用户注单异常!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 注单统计
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Response<Object> getStatistics(UserOrderVO vo) throws Exception {
        BetOrderVO betOrderVO = new BetOrderVO();
        BeanUtils.copyProperties(vo, betOrderVO);
        if (StringUtils.isNotEmpty(vo.getMerchantCode())) {
            List<String> list = merchantMapper.queryChildren(vo.getMerchantCode());
            if (CollectionUtils.isNotEmpty(list)) {
                betOrderVO.setMerchantCode(null);
                betOrderVO.setMerchantCodeList(list);
            }
        }
        if (vo.getMatchId() != null) {
            betOrderVO.setMatchId(Long.parseLong(vo.getMatchId()));
        }
        if (vo.getUserId() != null) {
            betOrderVO.setUserId(Long.parseLong(vo.getUserId()));
        }
        assemblyQueryTime(betOrderVO, StringUtils.isEmpty(vo.getDatabaseSwitch()) ? REALTIME_TABLE : vo.getDatabaseSwitch());
        betOrderVO.setMerchantName(null);
        return Response.returnSuccess(this.abstractGetStatistics(betOrderVO, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                REALTIME_TABLE : vo.getDatabaseSwitch()));
    }


    /**
     * 注单统计
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Response<Object> getStatisticsES(UserOrderVO vo) throws Exception {
            if (StringUtils.isNotEmpty(vo.getMerchantCode())) {
                List<String> list = merchantMapper.queryChildren(vo.getMerchantCode());
                if (CollectionUtils.isNotEmpty(list)) {
                    vo.setMerchantCode(null);
                    vo.setMerchantCodeList(list);
                }
            }
            vo.setMerchantName(null);
            vo.setUserName(null);
            if(vo.getFakeName()!=null) {
                String uid = tUserMapper.getUserIdByFakeName(vo.getFakeName());
                vo.setUserId(uid);
            }

            BetOrderVO betOrderVO = new BetOrderVO();
            BeanUtils.copyProperties(vo, betOrderVO);

            if (vo.getUserId() != null) {
                betOrderVO.setUserId(Long.parseLong(vo.getUserId()));
            }
            if (vo.getMatchId() != null) {
                betOrderVO.setMatchId(Long.parseLong(vo.getMatchId()));
            }

            if (CollectionUtils.isNotEmpty(betOrderVO.getOutComeList())) {
                betOrderVO.setOrderStatus(1);
            }

            log.info("assemblyQueryTime.betOrderVO:" + betOrderVO);

            TicketResponseVO result = rpcClient.getStatistics(betOrderVO);
            return Response.returnSuccess(result);
    }

    /**
     * 查询注单
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Response<Object> queryTicketList(UserOrderVO vo, String language) {
        try {
            if (StringUtils.isNotEmpty(vo.getMerchantCode())) {
                List<String> list = merchantMapper.queryChildren(vo.getMerchantCode());
                if (CollectionUtils.isNotEmpty(list)) {
                    vo.setMerchantCode(null);
                    vo.setMerchantCodeList(list);
                }
            }
            vo.setMerchantName(null);
            vo.setUserName(null);
            Map<String, Object> result = this.abstractQueryUserOrderList(assemblyQueryOrderParamUTC8(vo, language), StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    REALTIME_TABLE : vo.getDatabaseSwitch());
            if (result != null && result.get("list") != null) {
                List<OrderSettle> betOrderPOList = (List<OrderSettle>) result.get("list");
                for (OrderSettle settle : betOrderPOList) {
                    settle.setMerchantName(settle.getMerchantCode());
                    settle.setUserName(StringUtils.isNotEmpty(settle.getFakeName()) ? settle.getFakeName() : settle.getUserName());
                }
            }
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("查询UTC8自然日期注单列表异常!", e);
            return Response.returnFail("查询UTC8自然日期注单列表异常");
        }
    }



    /**
     * 查询注单
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Response<Object> queryTicketListES(UserOrderVO vo, String language) {
        try {
            long startL = System.currentTimeMillis();
            if (StringUtils.isNotEmpty(vo.getMerchantCode())) {
                List<String> list = merchantMapper.queryChildren(vo.getMerchantCode());
                if (CollectionUtils.isNotEmpty(list)) {
                    vo.setMerchantCode(null);
                    vo.setMerchantCodeList(list);
                }
            }
            vo.setMerchantName(null);
            vo.setUserName(null);
            if(vo.getFakeName()!=null) {
                String uid = tUserMapper.getUserIdByFakeName(vo.getFakeName());
                vo.setUserId(uid);
            }

                BetOrderVO betOrderVO = new BetOrderVO();
                BeanUtils.copyProperties(vo, betOrderVO);
                int pageSize = vo.getPageSize() == null || vo.getPageSize() > 200 ? 20 : vo.getPageSize();
                int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
                betOrderVO.setPageNo(pageNum);
                betOrderVO.setSize(pageSize);
                betOrderVO.setEnd(pageNum * pageSize);
                betOrderVO.setLanguage(language);

                if (vo.getUserId() != null) {
                    betOrderVO.setUserId(Long.parseLong(vo.getUserId()));
                }
                if (vo.getMatchId() != null) {
                    betOrderVO.setMatchId(Long.parseLong(vo.getMatchId()));
                }
                betOrderVO.setDatabaseSwitch(vo.getDatabaseSwitch());

                betOrderVO.setLanguage(betOrderVO.getLanguage() == null ? LanguageEnum.ZS.getCode() : betOrderVO.getLanguage());
                if (CollectionUtils.isNotEmpty(betOrderVO.getOutComeList())) {
                    betOrderVO.setOrderStatus(1);
                }

                log.info("assemblyQueryTime.betOrderVO:" + betOrderVO);
            long startL2 = System.currentTimeMillis();
            TicketResponseVO result = rpcClient.queryTicketList(betOrderVO);
            log.info( "queryTicketListES:TicketResponseVO" + (System.currentTimeMillis() - startL2));
            if (result != null && result.getList() != null) {
                log.info("queryTicketListES.betOrderVO:" + result.getTotal());
                List<TicketOrderSettle> betOrderPOList =  result.getList();
                List<OrderSettle> userMerchantPOList = null;
                if( CollectionUtils.isNotEmpty(betOrderPOList)){
                    Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
                    userMerchantPOList =  mergeOrderMixMapper.queryUserMerchantPOList(uidList);
                }

                //组装betOrderPOList
                if(CollectionUtils.isNotEmpty(userMerchantPOList)){
                    betOrderPOList= this.assembleBetOrderListES(betOrderPOList,userMerchantPOList);
                }
               Set<String>  matchIdList = result.getMatchIdList();
               List<Map<String,String>> sorceList = mergeOrderMixMapper.querySorceByMatchIds(matchIdList);
               Map<String,String> matchSorce = Maps.newHashMap();
               for(Map<String,String> map:sorceList){
                   matchSorce.put(String.valueOf(map.get("matchId")).trim(),map.get("score"));
               }
                List<Map<String, Object>> seriesList = new ArrayList<>();
                for (TicketOrderSettle settle : betOrderPOList) {
                    settle.setMerchantName(settle.getMerchantCode());
                    settle.setUserName(StringUtils.isNotEmpty(settle.getFakeName()) ? settle.getFakeName() : settle.getUserName());
                    Integer seriesType = settle.getSeriesType();
                    if (seriesType > 1) {
                        Map<String, Object> tempMap = new HashMap<>();
                        tempMap.put("seriesType", seriesType);
                        tempMap.put("orderNo", settle.getOrderNo());
                        List<String> oddsList = settle.getOrderDetailList().stream()
                                .map(TicketOrderSettleDetail::getOddsValue).collect(Collectors.toList());
                        List<Double> odds = Lists.newArrayList();
                        for(String str:oddsList){
                            odds.add(Double.valueOf(str)/100000);
                        }
                        tempMap.put("oddsList", odds);
                        tempMap.put("managerCode", settle.getManagerCode());
                        seriesList.add(tempMap);
                    }
                   assemblySettleScoreAndPlayOptionNameES(settle,matchSorce,language);
                }
                if (seriesList.size() > 0) {
                    Map<String, String> map = OddsValuesUtils.seriesTotalOddsValues(seriesList);
                    for (TicketOrderSettle settle : betOrderPOList) {
                        settle.setOdds(map.get(settle.getOrderNo()));
                    }
                }
            }
            log.info( "queryTicketListES:," + (System.currentTimeMillis() - startL));
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("查询queryTicketListES自然日期注单列表异常!", e);
            return Response.returnFail("查询queryTicketListES自然日期注单列表异常");
        }
    }

    /**
     * 查询注单预约模式
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Response<Object> queryAppointmentList(UserOrderVO vo, String language) {
        try {
            if (StringUtils.isNotEmpty(vo.getMerchantCode())) {
                List<String> list = merchantMapper.queryChildren(vo.getMerchantCode());
                if (CollectionUtils.isNotEmpty(list)) {
                    vo.setMerchantCode(null);
                    vo.setMerchantCodeList(list);
                }
            }
            vo.setMerchantName(null);
            vo.setUserName(null);
            Map<String, Object> result = this.abstractQueryAppointmentList(assemblyQueryAppointmentList(vo, language), StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    REALTIME_TABLE : vo.getDatabaseSwitch());
            if (result != null && result.get("list") != null) {
                List<PreOrderSettle> betOrderPOList = (List<PreOrderSettle>) result.get("list");
                for (PreOrderSettle settle : betOrderPOList) {
                    settle.setFakeName(StringUtils.isNotEmpty(settle.getFakeName()) ? settle.getFakeName() : settle.getUserName());
                }
            }
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("查询注单预约模式列表异常!", e);
            return Response.returnFail("查询注单预约模式列表异常");
        }
    }

    @Override
    public Response<Object> queryPreSettleOrder(UserOrderVO userOrderVO) {
        return Response.returnSuccess(this.assemblyPreSettleOrder(userOrderVO));
    }

    /**
     * 导出注单
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Map exportTicketList(String username, UserOrderVO vo, String language) {
        vo.setMerchantName(null);
        String merchantCode = null;
        if (vo.getExportType() == 2) {
            merchantCode = "trader";
        }
        return abstractExportOrder(vo, username, merchantCode, language, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                "1" : vo.getDatabaseSwitch());
    }


    /**
     * 导出注单
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Map exportTicketListES(String username, UserOrderVO vo, String language) {
        vo.setMerchantName(null);
        String merchantCode = null;
        if (vo.getExportType() == 2) {
            merchantCode = "trader";
        }
        BetOrderVO betOrderVO = assemblyQueryOrderParamUTC8(vo, language);
        betOrderVO.setStart(0);
        betOrderVO.setPageSize(1);
        TicketResponseVO responseVO = rpcClient.queryTicketList(betOrderVO);
        return abstractExportOrderES( betOrderVO, username, merchantCode, language, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                "1" : vo.getDatabaseSwitch(),responseVO.getTotal().intValue());
    }

    /**
     * 注单账变导出
     *
     * @param vo
     * @param language
     */
    @Override
    public Map exportTicketAccountHistoryList(String username, UserOrderVO vo, String language) {
        return abstractExportOrderAccountHistory(username, vo, null, language, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                "1" : vo.getDatabaseSwitch());
    }

    @Override
    public void exportUserOrder(HttpServletResponse response, HttpServletRequest request, UserOrderVO vo) {
        try {
            if (StringUtils.isEmpty(vo.getUserId())) {
                throw new Exception("入参为空");
            }
            this.abstractUserOrder(vo, response);
        } catch (Exception e) {
            log.error("导出用户注单异常!", e);
        }
    }

    @Override
    public Response<?> importVipUser(HttpServletResponse response, HttpServletRequest request, MultipartFile vipCsv) {
        try {
            int total = 0;
//            List<Long> vipls = CsvUtil.getCsvToList(vipCsv);
            List<Long> vipls = CsvUtil.getExcelToList(vipCsv);
            log.info("importVipUser导入VIP用户,vipls:" + vipls);
            if (CollectionUtils.isNotEmpty(vipls)) {
                List<UserVipVO> userls = tUserMapper.queryUserInfoByIdList(vipls);
                log.info("importVipUser导入VIP用户,userls:" + userls);
                Map<String, List<UserVipVO>> resultmap = userls.stream().collect(Collectors.groupingBy(UserVipVO::getMerchantCode));
                //按商户code分组调用feign
                for (String key : resultmap.keySet()) {
                    int result = merchantApiClient.upsertUserVip(key, resultmap.get(key));
                    log.info("importVipUser导入VIP用户,feign key:{},list:{},result:{}", key, resultmap.get(key), result);
                    total = total + result;
                }
            }
            return Response.returnSuccess(total);
        } catch (Exception e) {
            log.error("importVipUser导入VIP用户异常!", e);
            return Response.returnFail("importVipUser导入VIP用户异常!" + e.toString());
        }
    }

    @Override
    public Response<?> uploadVipUser(HttpServletResponse response, HttpServletRequest request, String vipuserls, Integer type) {
        try {
            int total = 0;
            String[] vuls = vipuserls.split(",");
            List<Long> vipls = Lists.newArrayList();
            List<String> before = new ArrayList<>();
            before.add("-");
            List<String> after = new ArrayList<>();
            after.add(vipuserls);
            for (String vu : vuls) {
                vipls.add(Long.parseLong(vu));
            }
            log.info("uploadVipUser导入VIP用户,vipls:" + vipls);
            if (CollectionUtils.isNotEmpty(vipls)) {
                List<UserVipVO> userls = tUserMapper.queryUserInfoByIdList(vipls);
                for (UserVipVO vo : userls) {
                    vo.setImporttype(type);
                }
                log.info("uploadVipUser导入VIP用户,userls:" + userls);
                Map<String, List<UserVipVO>> resultmap = userls.stream().collect(Collectors.groupingBy(UserVipVO::getMerchantCode));
                //按商户code分组调用feign
                for (String key : resultmap.keySet()) {
                    int result = merchantApiClient.upsertUserVip(key, resultmap.get(key));
                    log.info("uploadVipUser导入VIP用户,feign key:{},list:{},result:{}", key, resultmap.get(key), result);
                    total = total + result;
                }
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.setFieldName(Arrays.asList("导入方式:" + (type == 1 ? "系统计算" : "手动计算")));
                vo.setBeforeValues(before);
                vo.setAfterValues(after);
                merchantLogService.saveLog(MerchantLogPageEnum.UPLOAD_VIPUSER, MerchantLogTypeEnum.EDIT_INFO, vo,
                        MerchantLogConstants.MERCHANT_IN, request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), request.getHeader("language"), IPUtils.getIpAddr(request));
            }
            return Response.returnSuccess(total);
        } catch (Exception e) {
            log.error("uploadVipUser导入VIP用户异常!", e);
            return Response.returnFail("uploadVipUser导入VIP用户异常!" + e.toString());
        }
    }

    @Override
    public Response<?> importToDisabled(HttpServletResponse response, HttpServletRequest request, String userIds, Integer disabled, String remark) {
        int total = 0;
        List<Long> userAllowList = Lists.newArrayList();
        try {
            String[] userArrays = userIds.split(",");
            List<Long> userList = Lists.newArrayList();
            List<String> fieldName = new ArrayList<>();
            List<String> beforeValues = new ArrayList<>();
            List<String> afterValues = new ArrayList<>();
            for (String vu : userArrays) {
                userList.add(Long.parseLong(vu));
            }
            log.info("importToDisabled导入禁用用户,userList:" + userList);
            MerchantLogFiledVO mvo = new MerchantLogFiledVO();
            if (CollectionUtils.isNotEmpty(userList)) {
                List<UserVipVO> userls = tUserMapper.queryUserInfoByIdList(userList);
                fieldName.add("批量启用&禁用");
                for (UserVipVO vo : userls) {
                    if (Objects.equals(disabled, UserAllowListSourceEnum.SOURCE_MERCHANT_DISABLE.getCode()) && (vo.getDisabled() != 0 && vo.getDisabled() != 1)) {
                        userAllowList.add(vo.getUid());
                    } else if (Objects.equals(disabled, UserAllowListSourceEnum.SOURCE_MERCHANT_ENABLE.getCode()) && (vo.getDisabled() != 0 && vo.getDisabled() != 1)) {
                        userAllowList.add(vo.getUid());
                    } else {
                        vo.setDisabled(disabled);
                        vo.setRemark(remark);
                        fieldName.add(vo.getUid() + " " + vo.getUserName());
                        beforeValues.add(1 == vo.getDisabled() ? "禁用" : "启用");
                        afterValues.add(0 == disabled ? "启用" : "禁用");
                    }
                }
                log.info("importToDisabled导入禁用用户,userList:" + userls);
                Map<String, List<UserVipVO>> resultmap = userls.stream()
                        .filter(u -> u.getDisabled() != null)
                        // 这里其他状态不可以处理
                        .filter(u -> u.getDisabled() == 0 || u.getDisabled() == 1)
                        .collect(Collectors.groupingBy(UserVipVO::getMerchantCode));

                //按商户code分组调用feign
                for (String key : resultmap.keySet()) {
                    int result = merchantApiClient.upsertUserDisabled(key, resultmap.get(key));
                    log.info("importToDisabled导入禁用用户,feign key:{},list:{},result:{}", key, resultmap.get(key), result);
                    total = total + result;
                }
                mvo.setFieldName(fieldName);
                mvo.setBeforeValues(beforeValues);
                mvo.setAfterValues(afterValues);
                merchantLogService.saveLog(MerchantLogPageEnum.BATCH_DISABLE, MerchantLogTypeEnum.EDIT_INFO, mvo,
                        MerchantLogConstants.MERCHANT_IN, request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), request.getHeader("language"), IPUtils.getIpAddr(request));
            }
            if (CollectionUtils.isEmpty(userAllowList)) {
                return Response.returnSuccess(total);
            }
            return Response.returnSuccess("操作成功，白名单用户自动忽略操作，忽略的用户ID为：[" + userAllowList + "]");
        } catch (Exception e) {
            log.error("uploadVipUser导入禁用用户异常!", e);
            return Response.returnFail("uploadVipUser导入禁用用户异常!" + e.toString());
        }
    }

    @Override
    public Response<?> updateDisabled(HttpServletResponse response, HttpServletRequest request, String userId, Integer disabled) {
        try {
            log.info("updateDisabled修改启/禁用状态,userId:{}" + userId);
            if (StringUtils.isBlank(userId)) {
                return Response.returnFail("无用户Id!!!");
            }
            UserVipVO user = tUserMapper.queryUserInfoById(Long.parseLong(userId));
            if (Objects.equals(disabled, UserAllowListSourceEnum.SOURCE_MERCHANT_DISABLE.getCode()) && (user.getDisabled() != 0 && user.getDisabled() != 1)) {
                return Response.returnFail("该用户属于白名单用户，不可禁用！");
            }
            String afterValue = disabled == 1 ? "禁用" : "启用";
            String beforeValue = user.getDisabled() == 1 ? "启用" : "禁用";
            user.setDisabled(disabled);
            log.info("updateDisabled修改启/禁用状态,user:{}" + JSON.toJSONString(user));

            int result = merchantApiClient.updateDisabled(user.getMerchantCode(), user);
            log.info("updateDisabled修改启/禁用状态,feign key:{},user:{}", user.getMerchantCode(), JSON.toJSONString(user));

            //记录日志
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add("启&禁用");
            vo.getBeforeValues().add(beforeValue);
            vo.getAfterValues().add(afterValue);
            String language = request.getHeader("language");

            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            merchantLogService.saveLog(MerchantLogPageEnum.BATCH_DISABLE, MerchantLogTypeEnum.EDIT_INFO, vo, MerchantLogConstants.MERCHANT_IN, userId, request.getHeader("merchantName")
                    , null, null, userId, language, IPUtils.getIpAddr(request));

            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("updateDisabled修改启/禁用状态异常!", e);
            return Response.returnFail("updateDisabled修改启/禁用状态异常!" + e);
        }
    }

    /**
     * 今日投注统计
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Response queryBetToday() {
        try {
            MerchantOrderDayPO po = (MerchantOrderDayPO) LocalCacheService.betMap.getIfPresent("queryBetToday:all");
            if (po == null) {
                MerchantOrderVO vo = new MerchantOrderVO();
                List<String> list = (List<String>) LocalCacheService.cacheMap.getIfPresent("allPlatformCode");
                if (CollectionUtils.isEmpty(list)) {
                    list = merchantMapper.queryAllPlatformCode();
                    LocalCacheService.cacheMap.put("allPlatformCode", list);
                    log.info("queryBetToday所有站点的编码放入缓存成功!" + list.size());
                }
                vo.setMerchantCodeList(list);
                Response response = rpcClient.queryBetToday(vo);
                log.info("RPC查询每日投注结果:" + response.getData());
                if (response.getData() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    po = mapper.convertValue(response.getData(), new TypeReference<MerchantOrderDayPO>() {
                    });
                    LocalCacheService.betMap.put("queryBetToday:all", po);
                }
            }
            return Response.returnSuccess(geBetTodaytMap(po));
        } catch (Exception e) {
            log.error("HomeController.queryBetToday,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 今日用户信息
     */
    @Override
    public Response queryUserToday() {
        try {
            Map<String, Object> cacheMap = (Map<String, Object>) LocalCacheService.betMap.getIfPresent("queryUserToday:all");
            if (cacheMap == null) {
                List<String> list = (List<String>) LocalCacheService.cacheMap.getIfPresent("allPlatformCode");
                if (CollectionUtils.isEmpty(list)) {
                    list = merchantMapper.queryAllPlatformCode();
                    LocalCacheService.cacheMap.put("allPlatformCode", list);
                    log.info("queryUserToday所有站点的编码放入缓存成功!");
                }
                MerchantOrderVO vo = new MerchantOrderVO();
                vo.setMerchantCodeList(list);
                cacheMap = rpcClient.queryUserToday(vo);
                LocalCacheService.betMap.put("queryUserToday:all", cacheMap);
                log.info("RPC查询今日用户信息结果:" + cacheMap);
            }
            return Response.returnSuccess(cacheMap);
        } catch (Exception e) {
            log.error("HomeController.queryUserToday,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 30日投注统计
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:20
     */
    @Override
    public Response<?> userOrderDay30() {
        try {
            MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
            merchantOrderVO.setMerchantCodeList(merchantMapper.queryAllPlatformCode());
            return rpcClient.merchantOrderDay(merchantOrderVO);
        } catch (Exception e) {
            log.error("HomeController.userOrderDay30,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 14天投注用户分布
     */
    @Override
    public Response userDaySpread14(UserOrderVO vo) {
        try {
            return rpcClient.userDaySpread14(vo);
        } catch (Exception e) {
            log.error("HomeController.userDaySpread14,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 30天投注量趋势
     */
    @Override
    public Response<?> getMerchantOrderDay() {
        try {
            MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
            merchantOrderVO.setMerchantCodeList(merchantMapper.queryAllPlatformCode());
            return rpcClient.merchantOrderDay(merchantOrderVO);
        } catch (Exception e) {
            log.error("HomeController.userDay14,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    @Override
    public Response merchantUser() {
        try {
            MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
            merchantOrderVO.setMerchantCodeList(merchantMapper.queryAllPlatformCode());
            return rpcClient.merchantUser(merchantOrderVO);
        } catch (Exception e) {
            log.error("HomeController.userDay14,exception:", e);
            return Response.returnFail("查询异常!");
        }
    }

    @Override

    public Response<Object> queryFakeNameByCondition(String fakeName) {
        List<UserFakeVO> mapList = backupOrderMixMapper.queryFakeNameByCondition(fakeName);
        log.info(mapList.toString());
        return Response.returnSuccess(mapList);
    }

    /**
     * 查询玩家列表
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<?>
     * @date: 2020/8/23 12:04
     */
    @Override
    public Response<?> queryUserList(UserOrderVO vo) {
        try {
            int pageSize = vo.getPageSize() == null || vo.getPageSize() > 100 ? 20 : vo.getPageSize();
            int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
            String orderBy;
            if (vo.getLevelId() != null) {
                orderBy = "levelTime";
            } else if (vo.getMinBalance() != null || vo.getMaxBalance() != null) {
                orderBy = "balance";
            } else {
                orderBy = Constant.profit;
            }
            if (StringUtils.isNotEmpty(vo.getMerchantCode())) {
                vo.setMerchantCodeList(Arrays.asList(vo.getMerchantCode().split(",")));
                vo.setMerchantCode(null);
            }
            if (null != vo.getLevelId()) {
                vo.setLevelList(Arrays.asList(vo.getLevelId().split(",")));
            }
            String sort = StringUtils.isEmpty(vo.getSort()) ? Constant.ASC : vo.getSort();
            PageHelper.startPage(pageNum, pageSize, orderBy + " " + sort);
            return Response.returnSuccess(new PageInfo<>(tUserMapper.queryRiskUserList(vo)));
        } catch (Exception e) {
            log.error("查询user 异常!", e);
            return Response.returnFail("查询user 异常!");
        }
    }

    /**
     * 获取玩家详情
     *
     * @Param: [uid]
     * @return: com.panda.sport.merchant.common.vo.Response<?>
     * @date: 2020/8/23 12:05
     */
    @Override
    public Response<?> getUserDetail(String uid) {
        UserOrderVO vo = new UserOrderVO();
        vo.setUserId(uid);
        return Response.returnSuccess(this.getAbstractUserDetail(vo));
    }

    @Override
    protected List<UserOrderAllPO> callUserReportService(UserOrderVO vo) {
        List<?> resultList = rpcClient.queryUserOrderAllList(vo);
        return CollectionUtils.isNotEmpty(resultList) ? new ObjectMapper().convertValue(resultList, new TypeReference<List<UserOrderAllPO>>() {
        }) : null;
    }

    @Override
    protected UserOrderAllPO callReportSelectByUser(String userId) {
        return rpcClient.selectByUser(userId);
    }


    @Override
    public Response<OrderOverLimitVO> queryOneOrderOverLimitInfo(String orderNo) {
        try {
            OrderOverLimitVO orderOverLimitVO = backupOrderMapper.queryOneOrderOverLimitInfo(orderNo);
            if (orderOverLimitVO == null) {
                orderOverLimitVO = new OrderOverLimitVO();
                orderOverLimitVO.setOrderNo(orderNo);
                orderOverLimitVO.setRemark("注单正常结算");
            }
            return Response.returnSuccess(orderOverLimitVO);
        } catch (Exception e) {
            return Response.returnFail("查询失败！");
        }
    }

    @Override
    public Response<List<OrderOverLimitVO>> queryOrderOverLimitInfos(List<String> orderNos) {
        Response<List<OrderOverLimitVO>> result = null;
        List<OrderOverLimitVO> orderOverLimitVOs = null;
        try {
            log.info("orderNos:{}", orderNos);
            orderOverLimitVOs = backupOrderMapper.queryOrderOverLimitInfos(orderNos);
            log.info("orderOverLimitVOs:{}", orderOverLimitVOs);
            if (orderOverLimitVOs == null || orderOverLimitVOs.size() == 0) {
                orderOverLimitVOs = new ArrayList<>(orderNos.size());
                for (int i = 0; i < orderNos.size(); i++) {
                    OrderOverLimitVO orderOverLimitVO = new OrderOverLimitVO();
                    orderOverLimitVO.setOrderNo(orderNos.get(i));
                    orderOverLimitVO.setRemark("注单正常结算");
                    orderOverLimitVOs.add(orderOverLimitVO);
                }
            } else {
                if (orderOverLimitVOs.size() != orderNos.size()) {
                    Map<String, String> orderOverLimitMap = new HashMap<>(orderOverLimitVOs.size());
                    for (int i = 0; i < orderOverLimitVOs.size(); i++) {
                        orderOverLimitMap.put(orderOverLimitVOs.get(i).getOrderNo(), orderOverLimitVOs.get(i).getRemark());
                    }
                    orderOverLimitVOs = new ArrayList<>(orderNos.size());
                    for (int i = 0; i < orderNos.size(); i++) {
                        OrderOverLimitVO orderOverLimitVO = new OrderOverLimitVO();
                        orderOverLimitVO.setOrderNo(orderNos.get(i));
                        if (orderOverLimitMap.containsKey(orderNos.get(i))) {
                            orderOverLimitVO.setRemark(orderOverLimitMap.get(orderNos.get(i)));
                        } else {
                            orderOverLimitVO.setRemark("注单正常结算");
                        }
                        orderOverLimitVOs.add(orderOverLimitVO);
                    }
                }
            }
            result = Response.returnSuccess(orderOverLimitVOs);
        } catch (Exception e) {
            log.error("查询失败", e);
            result = Response.returnFail("查询失败!");
        }
        return result;
    }

    @Override
    public Response<?> updateIsVipDomain(String userId, Integer isVipDomain) {
        UserPO po = tUserMapper.getUserInfo(Long.parseLong(userId));
        if (po != null) {
            merchantApiClient.updateIsVipDomain(po.getMerchantCode(), userId, isVipDomain);
            return Response.returnSuccess();
        }
        return Response.returnFail("查询失败!");
    }

    @Override
    public List<Long> getUserIdListByParam(Map<String, Object> paramMap) {
        return tUserMapper.getUserIdListByParam(paramMap);
    }

    @Override
    public int getUserCountByParam(Map<String, Object> paramMap) {
        return tUserMapper.getUserCountByParam(paramMap);
    }

    @Override
    public List<OrderVO> getOrderList(Map<String, Object> paramMap) {
        return backup83OrderMixMapper.getOrderList(paramMap);
    }


    @Override
    public void execOrderBySToRedisTask(String param) {
        String groupCode = execGroupCode;
        if (execGroupCode.contains(",")) {
            String[] groupCodes = groupCode.split(",");
            for (String code : groupCodes) {
                execOrderBySToRedisTask(code, param);
            }
        } else {
            execOrderBySToRedisTask(execGroupCode, param);
        }
    }


    private void execOrderBySToRedisTask(String groupCode, String param) {
        int startTime = Integer.parseInt(DateUtil.format(new Date(), TIME_PATTEN));
        if (StringUtils.isNotBlank(param) && RegexUtils.isInteger(param)) {
            startTime = Integer.parseInt(param);
        }

        // 查询X组下所有商户
        List<String> merchantCodes = merchantByGroupCacheMap.getIfPresent(groupCode);
        if (CollectionUtils.isEmpty(merchantCodes)) {
            merchantCodes = merchantOrderService.queryMerchantByGroupCode(groupCode);
            merchantByGroupCacheMap.put(groupCode, merchantCodes);
        }

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("startTime", startTime);
        paramMap.put("merchantCodes", merchantCodes);

        // 查询商户下1天内投注过的用户数
        int count = getUserCountByParam(paramMap);
        if (count == 0) {
            printLog("OrderBySToRedisTask-- 未查询到{}组有【{}】有投注记录的用户", groupCode, startTime);
            return;
        }

        int totalBatch = count / orderBatchCount + 1;
        printLog("OrderBySToRedisTask--总待处理用户数：{},每批{},共{}批", count, orderBatchCount, totalBatch);


        for (int i = 1; i <= totalBatch; i++) {
            int batchTimes = i;

            ExecutorInstance.executorService.submit(() -> {
                long now = System.currentTimeMillis();
                printLog("OrderBySToRedisTask--第{}批注单写入缓存处理开始", batchTimes);
                execWriteOrderCatch(batchTimes, paramMap);
                printLog("OrderBySToRedisTask--第{}批注单写入缓存处理结束,总耗时【{}】ms", batchTimes, System.currentTimeMillis() - now);
            });
        }

    }

    private void execWriteOrderCatch(int batchTimes, Map<String, Object> paramMap) {
        try {
/*            List<OrderVO> orderList;
            Map<Long, List<OrderVO>> userOrderMap;
            String key;
            Boolean flag;
            int orderCount;
            long uid;


            paramMap.put("startNum", (batchTimes - 1) * orderBatchCount);
            paramMap.put("rows", orderBatchCount);

            List<Long> resultUidList = getUserIdListByParam(paramMap);
            if (CollectionUtils.isEmpty(resultUidList)) {
                printLog("OrderBySToRedisTask--批次：{}无需处理1", batchTimes);
                return;
            }

            // 先过滤一遍redis 再去查库
            resultUidList = resultUidList.stream()
                    .filter(u -> !orderRedisUtil.hasKey(ORDER_HISTORY + UNSETTLE_ORDER_CACHE + u))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(resultUidList)) {
                printLog("OrderBySToRedisTask--批次：{}无需处理2", batchTimes);
                return;
            }

            Map<String, Object> paramMap2 = Maps.newHashMap();
            paramMap2.put("orderStatus", 0);
            paramMap2.put("orderBy", 2);
            paramMap2.put("startRecord", 0);
            paramMap2.put("endRecord", 300);
            paramMap2.put("userIdList", resultUidList);
            orderList = getOrderList(paramMap2);

            if (CollectionUtils.isEmpty(orderList)) {
                printLog("OrderBySToRedisTask--批次：{}无需处理3", batchTimes);
                return;
            }

            userOrderMap = orderList.stream()
                    .filter(Objects::nonNull)
                    .filter(o -> Objects.nonNull(o.getUid()))
                    .collect(Collectors.groupingBy(OrderVO::getUid));

            if (MapUtils.isEmpty(userOrderMap)) {
                printLog("OrderBySToRedisTask--批次：{}无需处理4", batchTimes);
                return;
            }
            int pendingCount = userOrderMap.size();

            for (Map.Entry<Long, List<OrderVO>> entry : userOrderMap.entrySet()) {

                uid = entry.getKey();

                key = ORDER_HISTORY + UNSETTLE_ORDER_CACHE + uid;
                flag = orderRedisUtil.hasKey(key);
                if (flag) {
                    printLog("OrderBySToRedisTask--批次：{}--uid:{}，已存在未结算注单缓存，忽略处理 --本批剩余待处理{}", batchTimes, uid, pendingCount--);
                    continue;
                }
                orderList = entry.getValue();
                orderCount = CollectionUtils.isNotEmpty(orderList) ? orderList.size() : 0;
                if (orderCount > 1) { // 1单和没有 不需要存缓存
                    Map<String, String> orderMap = new HashMap<>();
                    for (OrderVO vo1 : orderList) {
                        orderMap.put(vo1.getOrderNo(), orderRedisUtil.compress(JSONObject.toJSONString(vo1)));
                    }
                    printLog("OrderBySToRedisTask--批次：{}--uid:{}，存入缓存KEY：{},数量为：{}--本批剩余待处理{}", batchTimes, uid, key, orderCount, pendingCount--);
                    orderRedisUtil.hPutAll(key, orderMap);
                } else {
                    printLog("OrderBySToRedisTask--批次：{}--uid:{}，存入缓存KEY：{}，默认值1 --本批剩余待处理{}", batchTimes, uid, key, pendingCount--);
                    orderRedisUtil.hPut(key, "1", "1");
                }
                orderRedisUtil.expire(key, orderCacheTtl, TimeUnit.SECONDS);
                // 每次结束睡 1s
                Thread.sleep(1_000);
            }*/
        } catch (Exception e) {
            printLog("OrderBySToRedisTask--批次：{}处理失败 ", batchTimes, e);
            log.error("OrderBySToRedisTask--批次：{}处理失败", batchTimes, e);
        }

    }

    private void printLog(Object... str) {
        String logs = str[0].toString();
        int time = 1;
        while (logs.contains("{}")) {
            logs = logs.replaceFirst("\\{}", str[time++].toString());
        }
        XxlJobLogger.log(logs);
        log.info(logs);
    }
}
