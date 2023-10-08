package com.panda.sport.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.panda.sport.admin.feign.PandaRcsTradeClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.*;
import com.panda.sport.admin.utils.IdempotentUtils;
import com.panda.sport.admin.utils.JwtTokenUtil;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.dto.RcsTradeRestrictMerchantSettingDto;
import com.panda.sport.merchant.common.dto.Request;
import com.panda.sport.merchant.common.dto.UserSpecialLimitDto;
import com.panda.sport.merchant.common.dto.UserTradeRestrictDto;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.RcsUserSpecialBetLimitConfigPO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.utils.UUIDUtils;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.user.UserCheckLogVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@RestController
@RequestMapping("/admin/userReport")
public class PlayerReportController {

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private OutMerchantService merchantService;

    @Autowired
    private PlayerOrderService playerOrderService;

    @Autowired
    private PlayerTransferService playerTransferService;

    @Autowired
    private RcsUserConfigLimitService rcsUserConfigService;

    @Autowired
    private PandaRcsTradeClient userLimitApiService;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private UserCheckLogService userCheckLogService;



    public static Cache<String, String> getStatisticsESCacheMap = null;

    @PostConstruct
    private void init() {
        getStatisticsESCacheMap = Caffeine.newBuilder()
                .maximumSize(5_000) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();
    }

    /**
     * 查询用户注单按自然日 UTC8
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryPreSettleOrder")
    public Response<Object> queryPreSettleOrder(HttpServletRequest request, @RequestBody UserOrderVO userOrderVO) {
        if(CollectionUtils.isEmpty(userOrderVO.getOrderNos())) {
            return Response.returnSuccess(new HashMap<>());
        }
        return  playerOrderService.queryPreSettleOrder(userOrderVO);
    }

    /**
     * 查询用户全生命周期的投注数据
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryAllUserStatisticList")
    @PreAuthorize("hasAnyRole('betting')")
    public Response<?> queryAllUserStatisticList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("/admin/userReport/queryAllUserStatisticList:" + vo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return playerOrderService.queryPlayerBetReportList(vo, language);
    }

    @GetMapping(value = "/queryUserBetLimitDetail")
    public Response<?> queryUserBetLimitDetail(String userId) {
        log.info("/AbstractAbnormalFileExportService/queryUserBetLimitDetail:{}", userId);
        if (StringUtil.isBlankOrNull(userId)) {
            return Response.returnFail("用户id不能为空");
        }
        UserPO userLimit = rcsUserConfigService.getUserLimit(userId);
        log.info("/AbstractAbnormalFileExportService/UserPO:{}", userLimit);
        if (null == userLimit) {
            return Response.returnFail("用户信息没有找到");
        }
        RcsUserConfigDetailVO configDetailVO= rcsUserConfigService.detail(userLimit);
        log.info("/AbstractAbnormalFileExportService/configDetailVO:{}", configDetailVO);
        return Response.returnSuccess(configDetailVO);
    }

    /**
     * 查询用户注单 统计数据
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/getStatistics")
    public Response<Object> getStatistics(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        JwtUser user = SecurityUtils.getUser();
        log.info(user.getUsername() + ",admin/userReport/getStatistics:" + vo);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("getStatistics").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                    append(vo.getMaxBetAmount()).append(vo.getMaxProfit()).append(vo.getMinBetAmount()).append(vo.getMinProfit()).append(vo.getMerchantTag()).
                    append(vo.getDate()).append(vo.getFilter()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getMatchId()).append(vo.getCurrency()).append(vo.getUserVip()).
                    append(user.getId()).append(vo.getOrderStatus()).append(vo.getSeriesType()).append(vo.getSportId()).append(vo.getTournamentId()).append(vo.getMatchType());
            List<String> merchantCodeList = vo.getMerchantCodeList();
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                for (String code : merchantCodeList) {
                    sb.append(code);
                }
            }
            List<Integer> playList = vo.getPlayIdList();
            if (CollectionUtils.isNotEmpty(playList)) {
                for (Integer code : playList) {
                    sb.append(code);
                }
            }
            List<Integer> orderStatusList = vo.getOrderStatusList();
            if (CollectionUtils.isNotEmpty(orderStatusList)) {
                for (Integer code : orderStatusList) {
                    sb.append(code);
                }
            }
            if (IdempotentUtils.repeat(sb.toString(), this.getClass())) {
                return Response.returnFail("请勿重复提交!");
            }
            Response response = playerOrderService.getOrderStatistics(vo);
            IdempotentUtils.remove(sb.toString(), this.getClass());
            return response;
        } finally {
            IdempotentUtils.remove(sb.toString(), this.getClass());
        }
    }

    /**
     * 查询用户注单列表(自然日)
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryTicketList")
    @PreAuthorize("hasAnyRole('bet_slip')")
    public Response<Object> queryTicketList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        JwtUser user = SecurityUtils.getUser();
        log.info(user.getUsername() + ",admin/userReport/queryTicketList:" + vo + language);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("queryTicketList").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                    append(vo.getDate()).append(vo.getFilter()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getPage()).append(vo.getMatchId()).
                    append(vo.getSize()).append(vo.getPageSize()).append(vo.getPageNum()).append(vo.getMaxBetAmount()).append(vo.getMaxProfit()).append(vo.getCurrency()).append(vo.getMerchantTag())
                    .append(vo.getMinBetAmount()).append(vo.getMinProfit()).append(user.getId()).append(vo.getOrderStatus()).append(vo.getMatchType()).append(vo.getUserVip())
                    .append(vo.getSeriesType()).append(vo.getSportId()).append(vo.getTournamentId()).append(vo.getIp());
            List<String> merchantCodeList = vo.getMerchantCodeList();
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                for (String code : merchantCodeList) {
                    sb.append(code);
                }
            }

            List<Integer> playList = vo.getPlayIdList();
            if (CollectionUtils.isNotEmpty(playList)) {
                for (Integer code : playList) {
                    sb.append(code);
                }
            }
            List<Integer> orderStatusList = vo.getOrderStatusList();
            if (CollectionUtils.isNotEmpty(orderStatusList)) {
                for (Integer code : orderStatusList) {
                    sb.append(code);
                }
            }

            if (getStatisticsESCacheMap != null && getStatisticsESCacheMap.getIfPresent(sb.toString()) != null) {
                log.info("getStatisticsESCacheMap--cache" + sb);
                return JSONObject.parseObject(getStatisticsESCacheMap.getIfPresent(sb.toString()), Response.class);
            }
            Response response = playerOrderService.queryTicketList(vo, language);
            getStatisticsESCacheMap.put(sb.toString(), JSON.toJSONString(response));
            return response;
        } finally {
        }
    }



    /**
     * 查询用户注单 统计数据
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/getStatisticsES")
    public Response<Object> getStatisticsES(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        JwtUser user = SecurityUtils.getUser();
        log.info(user.getUsername() + ",admin/userReport/getStatisticsES:" + vo);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("getStatisticsES").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                    append(vo.getMaxBetAmount()).append(vo.getMaxProfit()).append(vo.getMinBetAmount()).append(vo.getMinProfit()).append(vo.getMerchantTag()).
                    append(vo.getDate()).append(vo.getFilter()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getMatchId()).append(vo.getCurrency()).append(vo.getUserVip()).
                    append(user.getId()).append(vo.getOrderStatus()).append(vo.getSeriesType()).append(vo.getSportId()).append(vo.getTournamentId()).append(vo.getMatchType());
            List<String> merchantCodeList = vo.getMerchantCodeList();
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                for (String code : merchantCodeList) {
                    sb.append(code);
                }
            }
            List<Integer> playList = vo.getPlayIdList();
            if (CollectionUtils.isNotEmpty(playList)) {
                for (Integer code : playList) {
                    sb.append(code);
                }
            }
            List<Integer> orderStatusList = vo.getOrderStatusList();
            if (CollectionUtils.isNotEmpty(orderStatusList)) {
                for (Integer code : orderStatusList) {
                    sb.append(code);
                }
            }
            if (getStatisticsESCacheMap != null && getStatisticsESCacheMap.getIfPresent(sb.toString()) != null) {
                log.info("getStatisticsESCacheMap--cache" + sb);
                return JSONObject.parseObject(getStatisticsESCacheMap.getIfPresent(sb.toString()), Response.class);
            }
            Response response = playerOrderService.getStatisticsES(vo);
            getStatisticsESCacheMap.put(sb.toString(), JSON.toJSONString(response));
            return response;
        } finally {
        }
    }

    /**
     * 查询用户注单列表(自然日)
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryTicketListES")
    @PreAuthorize("hasAnyRole('bet_slip')")
    public Response<Object> queryTicketListES(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        JwtUser user = SecurityUtils.getUser();
        log.info(user.getUsername() + ",admin/userReport/queryTicketListES:" + vo + language);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("queryTicketListES").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                    append(vo.getDate()).append(vo.getFilter()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getPage()).append(vo.getMatchId()).
                    append(vo.getSize()).append(vo.getPageSize()).append(vo.getPageNum()).append(vo.getMerchantTag())
                    .append(user.getId()).append(vo.getOrderStatus()).append(vo.getMatchType())
                    .append(vo.getSeriesType()).append(vo.getSportId()).append(vo.getTournamentId()).append(vo.getIp());
            List<String> merchantCodeList = vo.getMerchantCodeList();
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                for (String code : merchantCodeList) {
                    sb.append(code);
                }
            }
            List<Integer> playList = vo.getPlayIdList();
            if (CollectionUtils.isNotEmpty(playList)) {
                for (Integer code : playList) {
                    sb.append(code);
                }
            }
            List<Integer> orderStatusList = vo.getOrderStatusList();
            if (CollectionUtils.isNotEmpty(orderStatusList)) {
                for (Integer code : orderStatusList) {
                    sb.append(code);
                }
            }
            if (IdempotentUtils.repeat(sb.toString(), this.getClass())) {
                return Response.returnFail("请勿重复提交!");
            }
            Response response = playerOrderService.queryTicketListES(vo, language);
            IdempotentUtils.remove(sb.toString(), this.getClass());
            return response;
        } finally {
            IdempotentUtils.remove(sb.toString(), this.getClass());
        }
    }




    /**
     * 查询预约模式
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryAppointmentList")
    // @AuthRequiredPermission("Merchant:Order:user:list")
    public Response<Object> queryAppointmentList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        List<String> codes = new ArrayList<>();
        JwtUser user = SecurityUtils.getUser();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel())) {
            codes = merchantService.queryMerchantList();
            if (CollectionUtils.isNotEmpty(codes)) {
                codes.add(user.getMerchantCode());
            } else {
                codes.add(user.getMerchantCode());
            }
        } else {
            codes.add(user.getMerchantCode());
        }
        vo.setMerchantCodeList(codes);
        Response response = playerOrderService.queryAppointmentList(vo, language);
        return response;
    }
    /**
     * 查询用户 统计数据
     *
     * @param request
     * @param uid
     * @return
     */
    @GetMapping(value = "/getUserInfo")
    public Response<Object> getUserInfo(HttpServletRequest request, @RequestParam(value = "uid") String uid) {
        log.info("查询用户信息:" + uid);
        return playerOrderService.getUserInfo(uid);
    }


    /**
     * 查询用户注单列表(ET账务日期)
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryUserOrderList")
    public Response<Object> queryUserOrderList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("admin/userReport/queryUserOrderList:" + vo);
        return playerOrderService.queryUserOrderList(vo);
    }

    /**
     * 查询用户账变列表
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryUserTransferList")
    public Response<Object> queryUserTransferList(HttpServletRequest request, @RequestBody UserTransferVO vo) {
        log.info("admin/userReport/queryUserTransferList:" + vo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return (vo == null || vo.getUserId() == null) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                playerTransferService.queryUserTransferList(vo, language);
    }

    /**
     * 用户投注统计
     */
    @PostMapping(value = "/queryUserBetListByTime")
    @PreAuthorize("hasAnyRole('users')")
    public Response<?> queryUserBetListByTime(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("admin/userReport/queryUserBetListByTime:" + vo);
        return playerOrderService.listUserBetGroupByUser(vo);
    }
    /**
     * 导出最近注单统计报表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("/exportUserOrderStatisticList")
    public void export(HttpServletResponse response, HttpServletRequest request) {
        playerOrderService.export(request, response);
    }

    /**
     * 本月用户投注量排行
     */
    @GetMapping(value = "/queryPlayerTop10")
    public Response<?> queryPlayerTop10(HttpServletRequest request) {
        return playerOrderService.queryPlayerTop10();
    }

    /**
     * 玩家报表
     */
    @RequestMapping("/exportUserOrderReport")
    public Response exportUserOrderReport(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String merchantCode, merchantId;
            Integer agentLevel;
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            merchantId = user.getMerchantId();
            agentLevel = user.getAgentLevel();
            if (agentLevel == 1 || agentLevel == 10) {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                List<String> resultList = new ArrayList<>(merchantCodeList);
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
            vo.setPageSize(100000);
            vo.setPageNum(1);
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            vo.setLanguage(language);
            log.info("对外商户后台导出报表:" + vo);
            resultMap.put("code", "0000");
            resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                    : "The exporting task has been created,please click at the Download Task menu to check!");
            try {
                merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "用户投注统计_" : "PlayerReport_", merchantCode, user.getUsername(), JSON.toJSONString(vo),
                        language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-用户投注统计" : "Report Center-PlayerReport", "groupByUserReportExportServiceImpl");

            } catch (RuntimeException e) {
                resultMap.put("code", "0002");
                resultMap.put("msg", e.getMessage());
            }
        } catch (Exception e) {
            log.error("UserController.exportUserOrderReport error!", e);
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 导出最近一段时间的注单数据
     *
     * @param request
     * @param response
     * @param userId
     * @param dateType 自然日/帐务日
     * @param date
     * @param filter   1投注时间 3 结算时间
     * @param sportId
     * @param token
     */
    @RequestMapping(value = "/exportUserOrder")
    public void exportUserOrder(HttpServletRequest request, HttpServletResponse response
            , @RequestParam(value = "userId") String userId, @RequestParam(value = "dateType", required = false) String dateType
            , @RequestParam(value = "date", required = false) String date, @RequestParam(value = "filter", required = false) String filter
            , @RequestParam(value = "sportId", required = false) Integer sportId, @RequestParam(value = "token") String token) {
        try {
            UserOrderVO vo = new UserOrderVO();
            filter = StringUtils.isEmpty(filter) ? "1" : filter;
            vo.setUserId(userId);
            vo.setDate(date);
            vo.setFilter(filter);
            vo.setDateType(dateType);
            vo.setSportId(sportId);
            vo.setPageSize(10000);
            vo.setPageNum(1);
            assemblyQueryParam(vo, token);
            playerOrderService.exportUserOrder(vo, response);
        } catch (Exception e) {
            log.error("UserController.exportUserOrder,exception:", e);
        }
    }

    /**
     * 导出最近一段时间的注单数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportTicketList")
    public Response<Object> exportTicketList(HttpServletRequest request, HttpServletResponse response,
                                             @RequestBody UserOrderVO vo) {
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            vo.setPageSize(1000000);
            vo.setPageNum(1);
            log.info(language + "UserController.exportTicketList>>>>>>>>>" + vo);
            return playerOrderService.exportTicketList(vo, null, language);
        } catch (Exception e) {
            log.error("UserController.exportUserOrder,exception:", e);
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }


    /**
     * 导出最近一段时间的注单数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportTicketListES")
    public Response<Object> exportTicketListES(HttpServletRequest request, HttpServletResponse response,
                                             @RequestBody UserOrderVO vo) {
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            vo.setPageSize(1000000);
            vo.setPageNum(1);
            log.info(language + "UserController.exportTicketListES>>>>>>>>>" + vo);
            return playerOrderService.exportTicketListES(vo, null, language);
        } catch (Exception e) {
            log.error("UserController.exportTicketListES,exception:", e);
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }


    /**
     * 导出最近一段时间的注单数据
     *
     * @param response
     * @param vo
     */
    @RequestMapping(value = "/exportTicketAccountHistoryList")
    public Response exportTicketAccountHistoryList(HttpServletRequest request, HttpServletResponse response, @RequestBody UserOrderVO vo) {
        try {
            vo.setPageSize(100000);
            vo.setPageNum(1);
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return playerOrderService.exportTicketAccountHistoryList(vo, null, language);
        } catch (Exception e) {
            log.error("UserController.exportUserOrder,exception:", e);
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }

    /**
     * 查询用户玩法列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/queryHotPlayName")
    public Response<Object> queryHotPlayName(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return playerOrderService.queryHotPlayName(vo.getSportId(), language);
    }

    /**
     * 根据用户真名获取用户信息
     * @param request
     * @return
     */
    @GetMapping(value = "/getUserByRealName")
    public Response<Object> checkUserLog(HttpServletRequest request,
                                         // 真实用户名
                                         @RequestParam(value = "userName") @NotBlank String userName,
                                         @RequestHeader(value = "language",defaultValue = Constant.LANGUAGE_CHINESE_SIMPLIFIED)  String  language
                                      ){

        return userCheckLogService.getUserByUserName(userName.trim(),language);
    }


    /**
     * 危险用户协查
     * @param request
     * @return
     */
    @PostMapping(value = "/checkUserLog")
    public Response<Object> checkUserLog(HttpServletRequest request, @RequestBody List<UserCheckLogVO> checkLogVOS) {
        return userCheckLogService.checkUserList(checkLogVOS);
    }

    /**
     * 危险用户协查
     * @param request
     * @return
     */
    @PostMapping(value = "/getCheckLogList")
    public Response<Object> getCheckLogList(HttpServletRequest request, @RequestBody UserCheckLogVO userCheckLogVO) {
        return userCheckLogService.getCheckLogList(userCheckLogVO);
    }

    /**
     * 初始化查询参数，对外商户的渠道商户查询包括其下面的二级商户，二级商户和直营只能查本身的数据
     *
     * @param vo
     * @param token
     */
    private void assemblyQueryParam(UserOrderVO vo, String token) {
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
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                resultList.addAll(merchantCodeList);
                if (CollectionUtils.isNotEmpty(vo.getMerchantCodeList())) {
                    List<String> tempList = vo.getMerchantCodeList();
                    resultList.retainAll(tempList);
                    vo.setMerchantCodeList(resultList);
                } else {
                    resultList.add(merchantCode);
                    vo.setMerchantCodeList(resultList);
                }
            } else {
                vo.setMerchantCode(merchantCode);
            }
        } else {
            vo.setMerchantCode(merchantCode);
        }
    }

    /**
     * todo 修改用户限额
     */
    @PostMapping(value = "/updateRcsLimit")
    @PreAuthorize("hasAnyRole('special_limit')")
    public Response<?> updateRcsLimit(HttpServletRequest request, @RequestBody @Validated RcsUserConfigParamVO rcsUserConfigParamVo) {
        try {
            log.info("url:/admin/userReport/updateRcsLimit;param:{}", rcsUserConfigParamVo.toString());
            RcsUserConfigVO rcsUserConfigVo = rcsUserConfigParamVo.getRcsUserConfigVo();
            Integer limit = rcsUserConfigVo.getSpecialBettingLimit();
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            UserSpecialLimitDto dto = new UserSpecialLimitDto();
            JwtUser user = SecurityUtils.getUser();
            if(rcsUserConfigVo.getPercentage()!=null) {
                dto.setPercentage(new BigDecimal(rcsUserConfigVo.getPercentage()).divide(new BigDecimal(100)));
            }
            if(rcsUserConfigVo.getBetExtraDelay()!=null) {
                dto.setBetExtraDelay(rcsUserConfigVo.getBetExtraDelay());
            }
            if(rcsUserConfigVo.getSportIdList()!=null) {
                dto.setSportIdList(rcsUserConfigVo.getSportIdList());
            }
            if(rcsUserConfigVo.getMarketLevel()!=null) {
                dto.setTagMarketLevelId(rcsUserConfigVo.getMarketLevel().toString());
            }
            dto.setSpecialLimitType(limit);
            dto.setUserId(Long.valueOf(rcsUserConfigVo.getUserId()));
            dto.setRemark(rcsUserConfigVo.getRemarks());
            dto.setOperatorId(String.valueOf(user.getId()));
            dto.setOperatorName(user.getUsername());

            String merchantCode = user.getMerchantCode();
            MerchantPO merchantPO = merchantMapper.getMerchant(merchantCode);
            if(merchantPO!=null && null!=merchantPO.getMerchantTag()){
                if(merchantPO.getMerchantTag()==1){
                    dto.setOperatorName("信用网-"+user.getUsername());
                }
            }
            Request<UserSpecialLimitDto> var1 = new Request<>();
            var1.setData(dto);
            String globalId = UUIDUtils.createId();
            log.info("admin/userReport/updateRcsLimit:" + var1 + ",globalId:" + globalId);
            var1.setGlobalId(globalId);
            com.panda.sport.merchant.common.dto.Response<Boolean> response = userLimitApiService.updateUserSpecialLimit(var1);
            if(response.getData()!=null && response.getData()) {
                List<RcsUserSpecialBetLimitConfigVO> list = Lists.newArrayList();
                RcsUserSpecialBetLimitConfigVO vo = new RcsUserSpecialBetLimitConfigVO();
                List<RcsUserSpecialBetLimitConfigPO> list1 = Lists.newArrayList();
                RcsUserSpecialBetLimitConfigPO confit1Po = new RcsUserSpecialBetLimitConfigPO();
                confit1Po.setSpecialBettingLimitType(rcsUserConfigVo.getSpecialBettingLimit());
                if(rcsUserConfigVo.getPercentage()!=null) {
                    confit1Po.setPercentageLimit(new BigDecimal(rcsUserConfigVo.getPercentage()).divide(new BigDecimal(100)));
                }
                confit1Po.setStatus(1);
                list1.add(confit1Po);
                vo.setRcsUserSpecialBetLimitConfigList1(list1);
                list.add(vo);
                rcsUserConfigParamVo.setRcsUserSpecialBetLimitConfigDataVoList(list);
                rcsUserConfigService.saveUserLimit(rcsUserConfigParamVo, language,user, IPUtils.getIpAddr(request));
                return Response.returnSuccess(response.getData());
            }
            return Response.returnFail(response.getMsg());
        } catch (Exception e) {
            log.error("admin/userReport/updateRcsLimit:", e);
            return Response.returnFail("失败!");
        }
    }

    /**
     * todo 修改用户限额
     */
    @GetMapping(value = "/getUserTradeRestrict")
    @PreAuthorize("hasAnyRole('special_limit')")
    public Response<?> getUserTradeRestrict(HttpServletRequest request,@RequestParam(value = "userId") String userId) {
        try {
            UserTradeRestrictDto dto = new UserTradeRestrictDto();
            dto.setUserId(Long.valueOf(userId));
            log.info("url:/admin/userReport/getUserTradeRestrict;param:{}", dto.toString());
            Request<UserTradeRestrictDto> var1 = new Request<>();
            var1.setData(dto);
            String globalId = UUIDUtils.createId();
            log.info("admin/userReport/getUserTradeRestrict:" + var1 + ",globalId:" + globalId);
            var1.setGlobalId(globalId);
            com.panda.sport.merchant.common.dto.Response<RcsTradeRestrictMerchantSettingDto> response = userLimitApiService.getUserTradeRestrict(var1);
            if(response!=null && response.getData()!=null) {
                return Response.returnSuccess(response.getData());
            }
            return Response.returnFail(response.getMsg());
        } catch (Exception e) {
            log.error("admin/userReport/getUserTradeRestrict:", e);
            return Response.returnFail("失败!");
        }
    }

    /**
     * 不用了
     * 商户对外增加帐变记录信息（账户中心==》帐变记录）
     *
     * @param request
     * @param findVO
     * @return
     */
   // @PostMapping("/addChangeRecordHistory")
    public Response addChangeRecordHistory(HttpServletRequest request, @RequestBody AccountChangeHistoryFindVO findVO) {
        return Response.returnSuccess(rcsUserConfigService.addChangeRecordHistory(findVO,request));
    }
}
