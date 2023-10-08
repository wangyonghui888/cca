package com.panda.sport.admin.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.PlayerOrderService;
import com.panda.sport.admin.utils.JwtTokenUtil;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.backup.mapper.SportMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.utils.OddsValuesUtils;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.AbstractOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.REALTIME_TABLE;


@Slf4j
@Service("playOrderService")
@RefreshScope
public class PlayerOrderServiceImpl extends AbstractOrderService implements PlayerOrderService {

    @Autowired
    private MerchantReportClient merchantReportClient;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private SportMapper sportMapper;

    @Resource
    private BackupTUserMapper backupTUserMapper;

    @Override
    public Response<Object> queryPreSettleOrder(UserOrderVO userOrderVO) {
        return Response.returnSuccess(this.assemblyPreSettleOrder(userOrderVO));
    }

    /**
     * 用户投注详情
     *
     * @param userId
     * @return
     */
    @Override
    public Response getPlayerAllOrder(String userId) {
        try {
            return Response.returnSuccess(this.getUserAllOrder(userId));
        } catch (Exception e) {
            log.error("UserController.getUserOrderAll,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 用户投注月统计
     *
     * @param userId
     * @return
     */
    @Override
    public Response userOrderMonth(String userId) {
        return merchantReportClient.userOrderMonth(userId);
    }

    /**
     * 用户投注月统计--某月的详情
     *
     * @param userId
     * @param time
     * @return
     */
    @Override
    public Response userOrderMonthDays(String userId, Integer time) {
        return merchantReportClient.userOrderMonthDays(userId, time);
    }

    /**
     * 用户投注月盈利
     *
     * @param userId
     * @return
     */
    @Override
    public Response getPlayerProfit(String userId) {
        return merchantReportClient.getPlayerProfit(userId);
    }

    /**
     * 查询用户全生命周期的投注数据
     *
     * @param vo
     * @param language
     * @return
     */
    @Override
    public Response<?> queryPlayerBetReportList(UserOrderVO vo, String language) {
        try {
            if (vo.getCurrency() != null &&
                    !vo.getCurrency().equals(CurrencyTypeEnum.RMB.getId())) {
                return new Response();
            }
            return assemblyQueryParam(vo, "") ? Response.returnSuccess(this.queryUserAllOrderList(vo, language,0)) : Response.returnSuccess();
        } catch (Exception e) {
            log.error("UserController.queryPlayerBetReportList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询用户注单 统计数据
     *
     * @param vo
     * @param vo
     * @return
     */
    @Override
    public Response<Object> getOrderStatistics(UserOrderVO vo) {
        try {
            if (!assemblyQueryParam(vo, "")) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            BetOrderVO betOrderVO = new BetOrderVO();
            BeanUtils.copyProperties(vo, betOrderVO);
            if (vo.getMatchId() != null) {
                betOrderVO.setMatchId(Long.parseLong(vo.getMatchId()));
            }
            if (vo.getUserId() != null) {
                betOrderVO.setUserId(Long.parseLong(vo.getUserId()));
            }
            assemblyQueryTime(betOrderVO, StringUtils.isEmpty(vo.getDatabaseSwitch()) ? REALTIME_TABLE : vo.getDatabaseSwitch());
            Map<String, Object> reslut = (Map<String, Object>) this.abstractGetStatistics(betOrderVO, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    REALTIME_TABLE : vo.getDatabaseSwitch());
            return Response.returnSuccess(reslut);
        } catch (Exception e) {
            log.error("getOrderStatistics获取订单统计异常", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 查询用户注单 统计数据
     *
     * @param vo
     * @param vo
     * @return
     */
    @Override
    public Response<Object> getStatisticsES(UserOrderVO vo) {
        try {
            String merchantCode, merchantId;
            Integer agentLevel;
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            merchantId = user.getMerchantId();
            agentLevel = user.getAgentLevel();
            if (agentLevel == 1 || agentLevel == 10) {
                List<String> resultList = new ArrayList<>();
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                resultList.addAll(merchantCodeList);
                List<String> tempList = vo.getMerchantCodeList();
                if (CollectionUtils.isNotEmpty(tempList)) {
                    resultList.retainAll(tempList);
                    vo.setMerchantCodeList(resultList);
                } else if (CollectionUtils.isEmpty(tempList) && StringUtils.isEmpty(vo.getMerchantCode())) {
                    vo.setMerchantCodeList(resultList);
                }
            } else {
                vo.setMerchantCode(merchantCode);
            }

            BetOrderVO betOrderVO = new BetOrderVO();
            BeanUtils.copyProperties(vo, betOrderVO);
            vo.setMerchantName(null);
            vo.setUserName(null);
            if(vo.getFakeName()!=null) {
                String uid = backupTUserMapper.getUserIdByFakeName(vo.getFakeName());
                vo.setUserId(uid);
            }
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

            TicketResponseVO result = merchantReportClient.getStatistics(betOrderVO);
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("getOrderStatistics获取订单统计异常", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询用户注单列表(ET账务日期)
     *
     * @param vo
     * @return
     */
    @Override
    public Response<Object> queryUserOrderList(UserOrderVO vo) {
        try {
            return assemblyQueryParam(vo, "") ? Response.returnSuccess(this.abstractQueryUserOrderList(assemblyQueryOrderParamET(vo), StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    REALTIME_TABLE : vo.getDatabaseSwitch()))
                    : Response.returnSuccess();
        } catch (Exception e) {
            log.error("queryUserOrderList获取ET订单列表异常", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询用户注单列表(自然日)
     *
     * @param vo
     * @param language
     * @return
     */
    @Override
    public Response<Object> queryTicketList(UserOrderVO vo, String language) {
        try {
            return assemblyQueryParam(vo, "") ? Response.returnSuccess(this.abstractQueryUserOrderList(assemblyQueryOrderParamUTC8(vo, language), StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    REALTIME_TABLE : vo.getDatabaseSwitch())) :
                    Response.returnSuccess();
        } catch (Exception e) {
            log.error("获取UTC8订单列表异常", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 导出最近注单统计报表数据
     *
     * @param request
     * @param response
     */
    @Override
    public void export(HttpServletRequest request, HttpServletResponse response) {
        try {
            String dateType = request.getParameter("dateType");
            String date = request.getParameter("date");
            String userId = request.getParameter("userId");
            String filter = StringUtils.isEmpty(request.getParameter("filter")) ? "1" : request.getParameter("filter");
            String merchantName = request.getParameter("merchantName");
            String sportId = request.getParameter("sportId");
            dateType = StringUtils.isEmpty(dateType) ? Constant.DAY : dateType;
            date = StringUtils.isEmpty(date) ? DateFormatUtils.format(new Date(), "yyyyMMdd") : date;
            Long time = Long.valueOf(date);
            PageHelper.startPage(1, Constant.MAX_EXPORT_NUM, true);
            UserOrderVO vo = new UserOrderVO();
            vo.setUserId(userId);
            vo.setTime(time);
            vo.setMerchantName(merchantName);
            vo.setSportId(Integer.valueOf(sportId));
            vo.setDateType(dateType);
            if (assemblyQueryParam(vo, "")) {
                this.abstractUserOrderExport(vo, filter, response);
            }
        } catch (Exception e) {
            log.error("导出用户订单报表异常!", e);
        }
    }

    /**
     * 导出最近一段时间的注单数据
     *
     * @param user
     * @param response
     */
    @Override
    public void exportUserOrder(UserOrderVO user, HttpServletResponse response) {
        try {
            this.abstractUserOrder(user, response);
        } catch (Exception e) {
            log.error("导出用户注单异常!", e);
        }
    }

    /**
     * 查询用户 统计数据
     *
     * @param uid
     * @return
     */
    @Override
    public Response<Object> getUserInfo(String uid) {
        try {
            UserOrderVO vo = new UserOrderVO();
            vo.setUserId(uid);
            if (!assemblyQueryParam(vo, "")) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return Response.returnSuccess(this.getAbstractUserDetail(vo));
        } catch (Exception e) {
            log.error("查询用户异常!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 本月用户投注量排行
     */
    @Override
    public Response<?> queryPlayerTop10() {
        UserOrderVO vo = new UserOrderVO();
        if (!assemblyQueryParam(vo, "")) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        return merchantReportClient.userOrderTop10(vo);
    }

    /**
     * 查询用户玩法列表
     *
     * @return
     */
    @Override
    public Response<Object> queryHotPlayName(Integer sportId, String language) {
        try {
            return Response.returnSuccess(this.abstractQueryHotPlayList(sportId, language));
        } catch (Exception e) {
            log.error("查询热门玩法列表异常!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 导出最近一段时间的注单数据
     *
     * @param vo
     * @param token
     * @param language
     */
    @Override
    public Response exportTicketList(UserOrderVO vo, String token, String language) {
        if (assemblyQueryParam(vo, token)) {
            JwtUser user = SecurityUtils.getUser();
            return Response.returnSuccess(abstractExportOrder(vo, user.getUsername(), user.getMerchantCode(), language, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    "1" : vo.getDatabaseSwitch()));
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }


    /**
     * 导出最近一段时间的注单数据
     *
     * @param vo
     * @param token
     * @param language
     */
    @Override
    public Response exportTicketListES(UserOrderVO vo, String token, String language) {
        if (assemblyQueryParam(vo, token)) {
            JwtUser user = SecurityUtils.getUser();
            BetOrderVO betOrderVO = assemblyQueryOrderParamUTC8(vo, language);
            betOrderVO.setPageNum(1);
            betOrderVO.setPageSize(1);
            TicketResponseVO responseVO = merchantReportClient.queryTicketList(betOrderVO);
            return Response.returnSuccess(abstractExportOrderES(betOrderVO, user.getUsername(), user.getMerchantCode(), language, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                    "1" : vo.getDatabaseSwitch(),responseVO.getTotal().intValue()));
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }

    /**
     * 导出最近一段时间的注单数据
     *
     * @param vo
     * @param token
     * @param language
     */
    @Override
    public Response<Object> exportTicketAccountHistoryList(UserOrderVO vo, String token, String language) {
        try {
            if (assemblyQueryParam(vo, token)) {
                JwtUser user = SecurityUtils.getUser();
                return Response.returnSuccess(abstractExportOrderAccountHistory(user.getUsername(), vo, user.getMerchantCode(), language, StringUtils.isEmpty(vo.getDatabaseSwitch()) ?
                        "1" : vo.getDatabaseSwitch()));
            }
        } catch (Exception e) {
            log.error("导出注单异常", e);
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }

    @Override
    public Response queryBetToday() {
        try {
            JwtUser user = SecurityUtils.getUser();
            String merchantCode = user.getMerchantCode();
            MerchantOrderDayPO po = (MerchantOrderDayPO) LocalCacheService.betMap.getIfPresent("queryBetToday:" + merchantCode);
            if (po == null) {
                MerchantOrderVO vo = new MerchantOrderVO();
                if (user.getAgentLevel() == 1 || user.getAgentLevel() == 10) {
                    List<String> merchantCodeList = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
                    if (CollectionUtils.isEmpty(merchantCodeList)) {
                        return Response.returnSuccess("无数据!");
                    }
                    vo.setMerchantCodeList(localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel()));
                    vo.setAgentLevel(2);
                } else {
                    vo.setMerchantCode(merchantCode);
                }
                Response response = merchantReportClient.queryBetToday(vo);
                log.info("RPC查询每日投注结果:" + response.getData());
                if (response.getData() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    po = mapper.convertValue(response.getData(), new TypeReference<MerchantOrderDayPO>() {
                    });
                    LocalCacheService.betMap.put("queryBetToday:" + merchantCode, po);
                }
            }
            return Response.returnSuccess(geBetTodaytMap(po));
        } catch (Exception e) {
            log.error("queryBetToday:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response queryUserToday() {
        try {
            JwtUser user = SecurityUtils.getUser();
            Integer agentLevel = user.getAgentLevel();
            String merchantCode = user.getMerchantCode();
            Map<String, Object> cacheMap = (Map<String, Object>) LocalCacheService.betMap.getIfPresent("queryUserToday:" + merchantCode);
            if (cacheMap == null) {
                MerchantOrderVO vo = new MerchantOrderVO();
                List<String> merchantCodeList = null;
                if (user.getAgentLevel() == 1 || user.getAgentLevel() == 10) {
                    merchantCodeList = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
                    if (CollectionUtils.isEmpty(merchantCodeList)) {
                        return Response.returnSuccess("无数据!");
                    }
                }
                if (agentLevel == 10) {
                    vo.setAgentLevel(1);
                    vo.setMerchantCodeList(merchantCodeList);
                } else if (agentLevel == 1) {
                    vo.setAgentLevel(2);
                    vo.setMerchantCodeList(merchantCodeList);
                } else {
                    vo.setAgentLevel(agentLevel);
                    vo.setMerchantCode(user.getMerchantCode());
                }
                Map<String, Object> response = merchantReportClient.queryUserToday(vo);
                log.info("RPC查询每日投注结果:" + response);
                if (response != null) {
                    cacheMap = (Map<String, Object>) response;
                    LocalCacheService.betMap.put("queryUserToday:" + merchantCode, cacheMap);
                }
            }
/*
            List<String> merchantCodeList = null;
            if (agentLevel == 10 || agentLevel == 1) {
                merchantCodeList = localCacheService.getMerchantCodeList(user.getMerchantId(), agentLevel);
                if (CollectionUtils.isEmpty(merchantCodeList)) {
                    return Response.returnSuccess("无数据!");
                }
            }*/

            return Response.returnSuccess(cacheMap);
        } catch (Exception e) {
            log.error("HomeReportController.queryUserToday,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response merchantMatchTop10() {
        try {
            SportVO vo = new SportVO();
            String merchantCode;
            JwtUser user = SecurityUtils.getUser();
            Integer agentLevel = user.getAgentLevel();
            String merchantId = user.getMerchantId();
            merchantCode = user.getMerchantCode();
            List<String> merchantCodeList = null;
            if (agentLevel == 10 || agentLevel == 1) {
                merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                if (CollectionUtils.isEmpty(merchantCodeList)) {
                    return Response.returnSuccess("无数据!");
                }
            }
            if (agentLevel == 10) {
                vo.setMerchantCodeList(merchantCodeList);
                vo.setAgentLevel(1);
            } else if (agentLevel == 1) {
                vo.setMerchantCodeList(merchantCodeList);
                vo.setAgentLevel(2);
            } else {
                vo.setAgentLevel(agentLevel);
                vo.setMerchantCode(merchantCode);
            }
            return Response.returnSuccess(merchantReportClient.merchantMatchTop10(vo));

        } catch (Exception e) {
            log.error("HomeReportController.matchTop10,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }

    }

    @Override
    public Response merchantOrder() {
        try {
            MerchantOrderVO vo = new MerchantOrderVO();
            JwtUser user = SecurityUtils.getUser();
            Integer agentLevel = user.getAgentLevel();
            String merchantId = user.getMerchantId();
            String merchantCode = user.getMerchantCode();
            List<String> merchantCodeList = null;
            if (agentLevel == 10 || agentLevel == 1) {
                merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                if (CollectionUtils.isEmpty(merchantCodeList)) {
                    return Response.returnSuccess("无数据!");
                }
            }
            if (agentLevel == 10) {
                vo.setMerchantCodeList(merchantCodeList);
                vo.setAgentLevel(1);
            } else if (agentLevel == 1) {
                vo.setMerchantCodeList(merchantCodeList);
                vo.setAgentLevel(2);
            } else {
                vo.setAgentLevel(agentLevel);
                vo.setMerchantCode(merchantCode);
            }
            return merchantReportClient.merchantOrderDay(vo);
        } catch (Exception e) {
            log.error("HomeReportController.userOrderDay14,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response userDaySpread30() {
        try {
            UserOrderVO vo = new UserOrderVO();
            vo.setMerchantCode(SecurityUtils.getUser().getMerchantCode());
            return merchantReportClient.userDaySpread30(vo);
        } catch (Exception e) {
            log.error("HomeReportController.userDay14,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response<?> queryUserBetListByTime(UserOrderVO vo) {
        try {
            if (StringUtils.isNoneEmpty(vo.getUserId()) && !StringUtils.isNumeric(vo.getUserId())) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            if (vo.getCurrency() != null && !vo.getCurrency().equals(CurrencyTypeEnum.RMB.getId())) {
                return new Response();
            }
            if (!assemblyQueryParam(vo, "")) {
                return Response.returnSuccess();
            }
            log.info("/admin/userReport/queryUserBetListByTime:**************" + vo);
            return merchantReportClient.queryUserBetListByTime(vo);
        } catch (Exception e) {
            log.error("UserReportController.queryUserBetListByTime,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response getSportList(String language) {
        try {
            JwtUser user = SecurityUtils.getUser();
            List<Map<String, Object>> sportList;
            MerchantPO m = merchantMapper.getMerchant(user.getMerchantCode());
            if (m.getOpenVrSport() == 1 || user.getAgentLevel() == 1) {
                sportList = sportMapper.getAllSportList(StringUtils.isEmpty(language) ? "zs" : language);
            } else {
                sportList = sportMapper.getNormalSportList(StringUtils.isEmpty(language) ? "zs" : language);
            }
            return Response.returnSuccess(sportList);
        } catch (Exception e) {
            log.error("getSportList:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response<?> listUserBetGroupByUser(UserOrderVO vo) {
        try {
            if (StringUtils.isNoneEmpty(vo.getUserId()) && !StringUtils.isNumeric(vo.getUserId())) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            if (vo.getCurrency() != null && !vo.getCurrency().equals(CurrencyTypeEnum.RMB.getId())) {
                return new Response();
            }
            if (!assemblyQueryParam(vo, "")) {
                return Response.returnSuccess();
            }
            return merchantReportClient.listUserBetGroupByUser(vo);
        } catch (Exception e) {
            log.error("PlayerOrderServiceImpl.listUserBetGroupByUser,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
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
            String merchantCode, merchantId;
            Integer agentLevel;
                JwtUser user = SecurityUtils.getUser();
                merchantCode = user.getMerchantCode();
                merchantId = user.getMerchantId();
                agentLevel = user.getAgentLevel();
                if (agentLevel == 1 || agentLevel == 10) {
                    List<String> resultList = new ArrayList<>();
                    List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                    resultList.addAll(merchantCodeList);
                    List<String> tempList = vo.getMerchantCodeList();
                    if (CollectionUtils.isNotEmpty(tempList)) {
                        resultList.retainAll(tempList);
                        vo.setMerchantCodeList(resultList);
                    } else if (CollectionUtils.isEmpty(tempList) && StringUtils.isEmpty(vo.getMerchantCode())) {
                        vo.setMerchantCodeList(resultList);
                    }
                } else {
                    vo.setMerchantCode(merchantCode);
                }


            BetOrderVO betOrderVO = new BetOrderVO();
            BeanUtils.copyProperties(vo, betOrderVO);
            int pageSize = vo.getPageSize() == null || vo.getPageSize() > 200 ? 20 : vo.getPageSize();
            int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
            betOrderVO.setPageNo(pageNum);
            betOrderVO.setSize(pageSize);
            betOrderVO.setEnd(pageNum * pageSize);
            betOrderVO.setLanguage(language);
            vo.setMerchantName(null);
            vo.setUserName(null);
            if(vo.getFakeName()!=null) {
                String uid = backupTUserMapper.getUserIdByFakeName(vo.getFakeName());
                vo.setUserId(uid);
            }
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

            TicketResponseVO result = merchantReportClient.queryTicketList(betOrderVO);
            List<TicketOrderSettle> betOrderPOList = result.getList();
            log.info("assemblyQueryTime.betOrderVO:" + betOrderPOList);
            List<OrderSettle> userMerchantPOList = null;
            if(  CollectionUtils.isNotEmpty(betOrderPOList)){
                Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
                userMerchantPOList = mergeOrderMixMapper.queryUserMerchantPOList(uidList);
            }

            //组装betOrderPOList
            if(CollectionUtils.isNotEmpty(userMerchantPOList)){
                betOrderPOList= this.assembleBetOrderListES(betOrderPOList,userMerchantPOList);
            }

            if (result != null && result.getList() != null) {
                Set<String>  matchIdList =result.getMatchIdList();
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
                            odds.add(Double.valueOf(str)/100000D);
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
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("查询queryTicketListES自然日期注单列表异常!", e);
            return Response.returnFail("查询queryTicketListES自然日期注单列表异常");
        }
    }

    private boolean assemblyQueryParam(UserOrderVO vo, String token) {
        String merchantCode, merchantId;
        Integer agentLevel;
        if (StringUtils.isNotEmpty(token)) {
            Map<String, String> claims = JwtTokenUtil.verifyToken(token);
            merchantCode = claims.get("merchantCode");
            merchantId = claims.get("merchantId");
            agentLevel = Integer.valueOf(claims.get("agentLevel"));
        } else {
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            merchantId = user.getMerchantId();
            agentLevel = user.getAgentLevel();
        }
        if (agentLevel == 1 || agentLevel == 10) {
            List<String> resultList = new ArrayList<>();
            List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
            if (CollectionUtils.isEmpty(merchantCodeList) || (StringUtils.isNotEmpty(vo.getMerchantCode()) && !merchantCodeList.contains(vo.getMerchantCode()))) {
                return false;
            }
            resultList.addAll(merchantCodeList);
            List<String> tempList = vo.getMerchantCodeList();
            if (CollectionUtils.isNotEmpty(tempList)) {
                resultList.retainAll(tempList);
                vo.setMerchantCodeList(resultList);
            } else if (CollectionUtils.isEmpty(tempList) && StringUtils.isEmpty(vo.getMerchantCode())) {
                vo.setMerchantCodeList(resultList);
            }
        } else {
            vo.setMerchantCode(merchantCode);
        }
        return vo.getMerchantCode() != null || CollectionUtils.isNotEmpty(vo.getMerchantCodeList());
    }

    @Override
    protected List<UserOrderAllPO> callUserReportService(UserOrderVO vo) {
        List<?> resultList = merchantReportClient.queryUserOrderAllList(vo);
        return CollectionUtils.isNotEmpty(resultList) ? new ObjectMapper().convertValue(resultList, new TypeReference<List<UserOrderAllPO>>() {
        }) : null;
    }

    @Override
    protected UserOrderAllPO callReportSelectByUser(String userId) {
        return merchantReportClient.selectByUser(userId);
    }

    @Override
    public Response queryUserIdListByUserName(UserOrderVO vo) {

        List<String>  list = backupTUserMapper.queryUserIdListByUserName(vo);

        return Response.returnSuccess(list);
    }

    /**
     * 查询预约模式
     * @param vo
     * @param language
     * @return
     */
    @Override
    public Response queryAppointmentList(UserOrderVO vo, String language) {
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
                    settle.setUserName(StringUtils.isNotEmpty(settle.getFakeName()) ? settle.getFakeName() : settle.getUserName());
                }
            }
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("商户查询注单预约模式列表异常!", e);
            return Response.returnFail("商户查询注单预约模式列表异常");
        }
    }
}
