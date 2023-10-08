package com.panda.sport.order.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.panda.sport.merchant.common.bo.tournament.DataProviderBO;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.user.UserLogHistoryVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import com.panda.sport.merchant.manage.service.RcsUserConfigService;
import com.panda.sport.merchant.manage.service.TournamentService;
import com.panda.sport.order.config.IdempotentUtils;
import com.panda.sport.order.service.UserLogHistoryService;
import com.panda.sport.order.service.UserOrderService;
import com.panda.sport.order.service.UserTransferService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/order/user")
public class UserController {

    @Autowired
    DataProviderBO dataProviderBO;
    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private UserTransferService userTransferService;

    @Autowired
    private UserLogHistoryService userLogHistoryService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private RcsUserConfigService rcsUserConfigService;


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
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryTicketList")
    @AuthRequiredPermission("Merchant:Order:user:list")
    public Response<Object> queryTicketList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("order/user/queryTicketList:" + vo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(vo.getFilter()).append("queryTicketList").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                    append(vo.getDate()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getPage()).append(vo.getMatchId()).
                    append(vo.getSize()).append(vo.getPageSize()).append(vo.getPageNum()).append(vo.getMaxBetAmount()).append(vo.getMaxProfit()).append(vo.getUserVip())
                    .append(vo.getMinBetAmount()).append(vo.getMinProfit()).append(vo.getOrderStatus()).append(vo.getMatchType()).append(vo.getCurrency()).append(vo.getMerchantTag())
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
            Response response = userOrderService.queryTicketList(vo, language);
            IdempotentUtils.remove(sb.toString(), this.getClass());
            return response;
        } finally {
            IdempotentUtils.remove(sb.toString(), this.getClass());
        }
    }


    /**
     * 查询用户注单按自然日 UTC8
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryTicketListES")
    @AuthRequiredPermission("Merchant:Order:user:list")
    public Response<Object> queryTicketListES(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("order/user/queryTicketListES:" + vo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(vo.getFilter()).append("queryTicketListES").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                    append(vo.getDate()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getPage()).append(vo.getMatchId()).
                    append(vo.getSize()).append(vo.getPageSize()).append(vo.getPageNum()).append(vo.getOrderStatus()).append(vo.getMatchType())
                    .append(vo.getSeriesType()).append(vo.getSportId()).append(vo.getTournamentId());
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
            Response response = userOrderService.queryTicketListES(vo, language);
            IdempotentUtils.remove(sb.toString(), this.getClass());
            return response;
        } finally {
            IdempotentUtils.remove(sb.toString(), this.getClass());
        }
    }

    /**
     * 查询用户注单按自然日 UTC8
     *
     * @param request
     * @param userOrderVO
     * @return
     */
    @RequestMapping(value = "/queryPreSettleOrder")
    public Response<Object> queryPreSettleOrder(HttpServletRequest request, @RequestBody UserOrderVO userOrderVO) {
        if (CollectionUtils.isEmpty(userOrderVO.getOrderNos())) {
            return Response.returnSuccess(new HashMap<>());
        }
        return userOrderService.queryPreSettleOrder(userOrderVO);
    }

    /**
     * 注单统计
     *
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 12:03
     */
    @PostMapping(value = "/getStatistics")
    //@AuthRequiredPermission("Merchant:Order:user:statistics")
    public Response<Object> getStatistics(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        try {
            log.info("order/user/getStatistics:" + vo);
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(vo.getFilter()).append("getStatistics").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                        append(vo.getMaxBetAmount()).append(vo.getMaxProfit()).append(vo.getMinBetAmount()).append(vo.getMinProfit()).append(vo.getMerchantTag()).
                        append(vo.getDate()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getMatchId()).append(vo.getCurrency()).append(vo.getUserVip()).
                        append(vo.getOrderStatus()).append(vo.getSeriesType()).append(vo.getSportId()).append(vo.getTournamentId()).append(vo.getMatchType()).append(vo.getSettleType()).append(vo.getIp());
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
                Response response = userOrderService.getStatistics(vo);
                IdempotentUtils.remove(sb.toString(), this.getClass());
                return response;
            } finally {
                IdempotentUtils.remove(sb.toString(), this.getClass());
            }

        } catch (Exception e) {
            log.error("UserController.getStatistics,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 注单统计
     *
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 12:03
     */
    @PostMapping(value = "/getStatisticsES")
    //@AuthRequiredPermission("Merchant:Order:user:statistics")
    public Response<Object> getStatisticsES(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        try {
            log.info("order/user/getStatisticsES:" + vo);
            StringBuilder sb = new StringBuilder();
                sb.append(vo.getFilter()).append("getStatisticsES").append(vo.getUserId()).append(vo.getUserName()).append(vo.getOrderNo()).append(vo.getStartTime()).append(vo.getEndTime()).append(vo.getUser()).
                        append(vo.getMaxBetAmount()).append(vo.getMaxProfit()).append(vo.getMinBetAmount()).append(vo.getMinProfit()).append(vo.getMerchantTag()).
                        append(vo.getDate()).append(vo.getMerchantCode()).append(vo.getMerchantName()).append(vo.getMatchId()).append(vo.getCurrency()).append(vo.getUserVip()).
                        append(vo.getOrderStatus()).append(vo.getSeriesType()).append(vo.getSportId()).append(vo.getTournamentId()).append(vo.getMatchType()).append(vo.getSettleType()).append(vo.getIp());
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
                if (getStatisticsESCacheMap != null && getStatisticsESCacheMap.getIfPresent(sb.toString()) != null) {
                    log.info("getStatisticsESCacheMap--cache" + sb);
                    return JSONObject.parseObject(getStatisticsESCacheMap.getIfPresent(sb.toString()), Response.class);
                }
                Response response = userOrderService.getStatisticsES(vo);
                getStatisticsESCacheMap.put(sb.toString(), JSON.toJSONString(response));
                return response;

        } catch (Exception e) {
            log.error("UserController.getStatistics,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 导出注单
     *
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 12:03|
     */
    @RequestMapping(value = "/exportTicketList")
    public Response exportTicketList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("order/user/exportTicketList:" + vo);
        String username = request.getHeader("merchantName");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return Response.returnSuccess(userOrderService.exportTicketList(username, vo, language));
    }


    /**
     * 导出注单
     *
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 12:03|
     */
    @RequestMapping(value = "/exportTicketListES")
    public Response exportTicketListES(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("order/user/exportTicketListES:" + vo);
        String username = request.getHeader("merchantName");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return Response.returnSuccess(userOrderService.exportTicketListES(username, vo, language));
    }

    /**
     * 导出注单账变信息
     *
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 12:03
     */
    @RequestMapping(value = "/exportTicketAccountHistoryList")
    public Response exportTicketAccountHistoryList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("order/user/exportTicketAccountHistoryList:" + vo);
        String username = request.getHeader("merchantName");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return Response.returnSuccess(userOrderService.exportTicketAccountHistoryList(username, vo, language));
    }

    /**
     * 获取用户详情
     *
     * @Param: [request, userId]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 12:01
     */
    @GetMapping(value = "/detail")
    @AuthRequiredPermission("Merchant:Order:user:detail")
    public Response detail(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        log.info("/order/user/detail:" + userId);
        try {
            return StringUtil.isBlankOrNull(userId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    userOrderService.userOrderAll(userId);
        } catch (Exception e) {
            log.error("UserController.detail,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 获取用户盈利
     *
     * @Param: [request, userId]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 12:01
     */
    @GetMapping(value = "/profit")
    @AuthRequiredPermission("Merchant:Report:user:profit")
    public Response profit(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        log.info("/order/user/profit:" + userId);
        try {
            return StringUtil.isBlankOrNull(userId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    userOrderService.userProfitAll(userId);
        } catch (Exception e) {
            log.error("UserController.profit,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 获取用户月报表
     *
     * @Param: [request, userId]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 12:01
     */
    @GetMapping(value = "/orderMonth")
    @AuthRequiredPermission("Merchant:Report:user:orderMonth")
    public Response orderMonth(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        log.info("/order/user/orderMonth:" + userId);
        try {
            return StringUtil.isBlankOrNull(userId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    userOrderService.userOrderMonth(userId);
        } catch (Exception e) {
            log.error("UserController.orderMonth,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 获取用户30日报表
     *
     * @Param: [request, userId]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 12:01
     */
    @GetMapping(value = "/orderMonth/days")
    @AuthRequiredPermission("Merchant:Report:user:orderMonth:days")
    public Response orderMonth(HttpServletRequest request, @RequestParam(value = "userId") String userId, @RequestParam(value = "time") int time) {
        log.info("/order/user/orderMonth/days:" + userId);
        try {
            return StringUtil.isBlankOrNull(userId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                    userOrderService.userOrderOneMonthDays(userId, time);
        } catch (Exception e) {
            log.error("UserController.orderMonth,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 获取用户全生命周期列表
     *
     * @Param: [request, userId]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 12:01
     */
    @PostMapping(value = "/queryUserBetList")
    @AuthRequiredPermission("Merchant:Order:user:bet:list")
    public Response<?> queryUserBetList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("/order/user/queryUserBetList:{}", vo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        Integer isExport = 0;
        return userOrderService.queryUserBetList(vo, language,isExport);
    }

    /**
     * 投注用户管理导出
     *
     * @Param: [request, userId]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 12:01
     */
    @PostMapping(value = "/queryUserBetListExport")
    @AuthRequiredPermission("Merchant:Order:user:bet:export")
    public Response queryUserBetListExport(HttpServletResponse response, HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("/order/user/queryUserBetListExport:{}", vo);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            try {
                resultMap.put("code", "0000");
                resultMap.put("msg", "投注用户管理导出,请在文件列表等候下载！");
                String language = request.getHeader("language");
                if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
                vo.setLanguage(language);
                userOrderService.queryUserBetListExport(request.getHeader("merchantName"), request, vo);
            } catch (RuntimeException e) {
                resultMap.put("code", "0002");
                resultMap.put("msg", e.getMessage());
            }
        } catch (Exception e) {
            log.error("queryUserBetListExport.export error!", e);
        }
        return Response.returnSuccess(resultMap);
    }

    @GetMapping(value = "/updateIsVipDomain")
    public Response<?> updateIsVipDomain(HttpServletRequest request, @RequestParam(value = "userId") String userId,
                                         @RequestParam(value = "isVipDomain") Integer isVipDomain) {
        log.info("/order/user/updateIsVipDomain:" + userId + "," + isVipDomain);
        return userOrderService.updateIsVipDomain(userId, isVipDomain);
    }

    /**
     * api地址：http://lan-yapi.sportxxxr1pub.com/project/178/interface/api/cat_660
     *
     * @param userId 用户id
     *               todo 获取用户限额详情
     */
    @GetMapping(value = "/queryUserBetLimitDetail")
    public Response<?> queryUserBetLimitDetail(String userId) {
        log.info("/order/user/queryUserBetLimitDetail:{}", userId);
        if (StringUtil.isBlankOrNull(userId)) {
            return Response.returnFail("用户id不能为空");
        }
        UserPO userLimit = rcsUserConfigService.getUserLimit(userId);
        if (null == userLimit) {
            return Response.returnFail("用户信息没有找到");
        }
        return Response.returnSuccess(rcsUserConfigService.detail(userLimit));
    }

    /**
     * 获取用户报表
     *
     * @Param: [request, userId]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @date: 2020/8/23 12:01
     */
    @PostMapping(value = "/queryUserBetListByTime")
    @AuthRequiredPermission("Merchant:Report:user:bet:list")
    public Response<?> queryUserBetListByTime(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("/order/user/queryUserBetListByTime:" + vo);
        return userOrderService.listUserBetGroupByUser(vo);
    }

    /**
     * 导出用户报表
     */
    @RequestMapping("/export")
    @AuthRequiredPermission("Merchant:Report:user:bet:list:export")
    public Response export(HttpServletResponse response, HttpServletRequest request, @RequestBody UserOrderVO vo) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            try {
                resultMap.put("code", "0000");
                resultMap.put("msg", "交易记录导出任务创建成功,请在文件列表等候下载！");
                String language = request.getHeader("language");
                if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
                vo.setLanguage(language);
                userOrderService.export(request.getHeader("merchantName"), request, vo);
            } catch (RuntimeException e) {
                resultMap.put("code", "0002");
                resultMap.put("msg", e.getMessage());
            }
        } catch (Exception e) {
            log.error("UserController.export error!", e);
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 用户登录历史查询
     */
    @GetMapping("/queryLoginHistory")
    public Response<PageVO<UserLogHistoryVO>> queryList(RequestPageVO<UserLogHistoryVO> pageVO) {
        log.info("/order/user/queryLoginHistory:" + pageVO);
        try {
            Page<Object> page = PageHelper.startPage(pageVO.getCurrPage(), pageVO.getSize());
            List<UserLogHistoryVO> returnData = userLogHistoryService.queryHistory(pageVO.getParam());
            return Response.returnSuccess(new PageVO(page, returnData));
        } catch (Exception e) {
            log.error("UserController.queryLoginHistory error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询用户注单按账户日期 ET
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryUserOrderList")
    @AuthRequiredPermission("Merchant:Order:user:list")
    public Response<Object> queryUserOrderList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("order/user/queryUserOrderList:" + vo);
        return userOrderService.queryUserOrderList(vo);
    }

    /**
     * 导出用户报表
     *
     * @Param: [response, request]
     * @return: void
     * @date: 2020/8/23 12:04
     */
    @RequestMapping(value = "/exportUserOrder")
    @AuthRequiredPermission("Merchant:Order:user:list:export")
    public void exportUserOrder(HttpServletResponse response, HttpServletRequest request
            , @RequestParam(value = "date", required = false) String date
            , @RequestParam(value = "dateType", required = false) String dateType
            , @RequestParam(value = "filter", required = false) String filter
            , @RequestParam(value = "userId", required = false) String userId) {
        try {
            UserOrderVO vo = new UserOrderVO();
            vo.setUserId(userId);
            vo.setDate(date);
            vo.setDateType(dateType);
            vo.setFilter(filter);
            log.info("order/user/exportUserOrder导出用户注单详情:" + vo);
            userOrderService.exportUserOrder(response, request, vo);
        } catch (Exception e) {
            log.error("UserController.exportUserOrder,exception:", e);
        }
    }

    /**
     * 查询玩家列表
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<?>
     * @date: 2020/8/23 12:04
     */
    @PostMapping(value = "/queryUserList")
    public Response<?> queryUserList(@RequestBody UserOrderVO vo) {
        try {
            return userOrderService.queryUserList(vo);
        } catch (Exception e) {
            log.error("UserReportController.queryUserList异常!" + e);
            return Response.returnFail(-1, "获取数据异常!");
        }
    }

    /**
     * 获取玩家详情
     *
     * @Param: [uid]
     * @return: com.panda.sport.merchant.common.vo.Response<?>
     * @date: 2020/8/23 12:05
     */
    @GetMapping(value = "/getUserDetail")
    @AuthRequiredPermission("Merchant:Order:user:userDetail")
    public Response<?> getUserDetail(@RequestParam(value = "uid") String uid) {
        try {
            return userOrderService.getUserDetail(uid);
        } catch (Exception e) {
            log.error("UserReportController.queryUserList异常!" + e);
            return Response.returnFail(-1, "获取数据异常!");
        }
    }

    /**
     * 获取玩家交易记录
     *
     * @Param: [uid]
     * @return: com.panda.sport.merchant.common.vo.Response<?>
     * @date: 2020/8/23 12:05
     */
    @PostMapping(value = "/queryUserTransferList")
    @AuthRequiredPermission("Merchant:Order:user:transferList")
    public Response<Object> queryUserTransferList(HttpServletRequest request, @RequestBody UserTransferVO vo) {
        log.info("order/user/queryUserTransferList:" + vo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return (vo == null || vo.getUserId() == null) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                userTransferService.queryUserTransferList(vo, language);
    }

    /**
     * 获取玩家热门玩法
     *
     * @Param: [uid]
     * @return: com.panda.sport.merchant.common.vo.Response<?>
     * @date: 2020/8/23 12:05
     */
    @PostMapping(value = "/queryHotPlayName")
    @AuthRequiredPermission("Merchant:Order:user:queryHotPlayName")
    public Response<Object> queryHotPlayName(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        log.info("order/user/queryHotPlayName");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return userOrderService.queryHotPlayName(vo.getSportId(), language);
    }

    /**
     * 联赛名称列表查询
     */
    @GetMapping(value = "/queryTournamentList")
    public Response queryTournamentList(HttpServletRequest request, @RequestParam(value = "args", required = false) String args, @RequestParam(value = "level", required = false) Integer level, @RequestParam(value = "sportId", required = false) Long sportId) {
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return Response.returnSuccess(tournamentService.findTournamentListBySportsLevel(args, language, level, sportId));
        } catch (Exception e) {
            log.error("联赛等级下拉查询失败！", e);
            return Response.returnFail("联赛等级下拉查询失败！");
        }
    }

    /**
     * 联赛名称列表查询
     */
    @GetMapping(value = "/queryFilterTournamentList")
    public Response queryFilterTournamentList(HttpServletRequest request,
                                              @RequestParam(value = "args", required = false) String args,
                                              @RequestParam(value = "level", required = false) Integer level,
                                              @RequestParam(value = "sportId", required = false) Long sportId,
                                              @RequestParam(value = "dataSourceCode", required = false) String dataSourceCode,
                                              @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) {
                language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            }
            return Response.returnSuccess(tournamentService.findFilterTournamentListBySportsLevel(args, language, level, sportId, dataSourceCode, merchantCode));
        } catch (Exception e) {
            log.error("联赛名称列表查询失败！", e);
            return Response.returnFail("联赛名称列表查询失败！");
        }
    }

    @GetMapping("/queryFakeNameByCondition")
    public Response<Object> queryFakeNameByCondition(@Param(value = "fakeName") String fakeName) {
        return userOrderService.queryFakeNameByCondition(fakeName);
    }

    @GetMapping("/queryOneOrderOverLimitInfo")
    public Response<OrderOverLimitVO> queryOrderOverLimitRemark(@RequestParam(value = "orderNo") String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            return Response.returnFail("请输入注单号");
        }
        Response<OrderOverLimitVO> result = userOrderService.queryOneOrderOverLimitInfo(orderNo);

        return result;
    }

    @PostMapping("/queryOrderOverLimitInfos")
    public Response<List<OrderOverLimitVO>> queryOrderOverLimitRemarks(@RequestBody List<String> orderNos) {
        if (orderNos == null || orderNos.size() == 0) {
            return Response.returnFail("请输入注单号");
        }
        Response<List<OrderOverLimitVO>> result = userOrderService.queryOrderOverLimitInfos(orderNos);

        return result;
    }

    /**
     * 增加帐变记录信息
     *
     * @param request
     * @param findVO
     * @return
     */
    /*@AuthSeaMoonKey("投注用户管理/手动加扣款")
    @PostMapping("/addChangeRecordHistory")
    public Response addChangeRecordHistory(String tokenCode, HttpServletRequest request, @RequestBody AccountChangeHistoryFindVO findVO) {
        return Response.returnSuccess(userTransferService.addChangeRecordHistory(findVO, request));
    }*/

    /**
     * 查询预约模式
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryAppointmentList")
    // @AuthRequiredPermission("Merchant:Order:user:list")
    public Response<Object> queryAppointmentList(HttpServletRequest request, @RequestBody UserOrderVO vo) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        Response response = userOrderService.queryAppointmentList(vo, language);
        return response;
    }

    /**
     * 导入VIP用户
     */
    @PostMapping(value = "/importVipUser")
    public Response importVipUser(HttpServletResponse response, HttpServletRequest request, @RequestPart(value = "multipartFile") MultipartFile multipartFile) {
        log.info("order/user/importVipUser，导入VIP用户:" + multipartFile.getOriginalFilename());
        return userOrderService.importVipUser(response, request, multipartFile);
    }

    /**
     * 导入VIP用户
     */
    @PostMapping(value = "/uploadVipUser")
    public Response uploadVipUser(HttpServletResponse response, HttpServletRequest request, @RequestBody UserVipVO uv) {
        log.info("order/user/uploadVipUser，导入VIP用户:" + uv.getVipuserls() + ",type:" + uv.getImporttype());
        return userOrderService.uploadVipUser(response, request, uv.getVipuserls(), uv.getImporttype());
    }

    /**
     * 批量启&禁用
     */
    @PostMapping(value = "/importToDisabled")
    @AuthRequiredPermission("Merchant:user:importToDisabled")
    public Response importToDisabled(HttpServletResponse response, HttpServletRequest request, @RequestBody UserVipVO uv) {
        log.info("order/user/importToDisabled，导入禁用用户:" + uv.getUserIds() + ",type:" + uv.getDisabled()  + ",remark:" + uv.getRemark() );
        return userOrderService.importToDisabled(response, request, uv.getUserIds(), uv.getDisabled(),uv.getRemark());
    }

    /**
     * 启&禁用
     */
    @PostMapping(value = "/updateDisabled")
    @AuthRequiredPermission("Merchant:user:updateDisabled")
    public Response updateDisabled(HttpServletResponse response, HttpServletRequest request, @RequestBody UserVipVO uv) {
        log.info("order/user/updateDisabled，修改启/禁用状态:{},{}" + uv.getUserIds() + ",type:" + uv.getDisabled());
        return userOrderService.updateDisabled(response, request, uv.getUserIds(), uv.getDisabled());
    }

    /**
     * 通过用户id查询用户名称
     */
    @PostMapping(value = "/findUserInfo")
    public Response findUserInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody UserVipVO uv) {
        log.info("order/user/findUserInfo，通过用户id查询用户名称:" + uv.getUserIds() + ",type:" + uv.getDisabled());
        return userOrderService.findUserInfo(uv);
    }

    /**
     * 获取联赛数据商
     *
     * @param request
     * @param args
     * @param level
     * @param sportId
     * @param merchantCode
     * @return
     */
    @GetMapping(value = "/getDataProviderTournamentList")
    public Response getDataProviderTournamentList(HttpServletRequest request, @RequestParam(value = "args", required = false) String args, @RequestParam(value = "level", required = false) Integer level, @RequestParam(value = "sportId", required = false) Long sportId, @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        try {
            String strNickDataProvider = dataProviderBO.getStrNickDataProvider();
            return Response.returnSuccess(dataProviderBO.getDataProviderSet());
        } catch (Exception e) {
            log.error("获取联赛数据商列表错误！", e);
            return Response.returnFail("获取联赛数据商列表失败！");
        }
    }
}