package com.panda.sport.admin.controller;


import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.PlayerOrderService;

import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.MerchantFileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/player")
@Slf4j
public class PlayerController {

    @Autowired
    private LocalCacheService localCacheService;


    @Autowired
    private PlayerOrderService playerOrderService;


    @GetMapping(value = "/getSportList")
    public Response getSportList(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return playerOrderService.getSportList(language);
    }

    /**
     * 用户投注详情
     *
     * @param request
     * @param userId
     * @return
     */
    @GetMapping(value = "/getPlayerOrderDetail")
    public Response getPlayerOrderDetail(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        return StringUtil.isBlankOrNull(userId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                playerOrderService.getPlayerAllOrder(userId);
    }

    /**
     * 用户投注月统计
     *
     * @param request
     * @param userId
     * @return
     */
    @GetMapping(value = "/orderMonth")
    public Response orderMonth(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        return StringUtil.isBlankOrNull(userId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                playerOrderService.userOrderMonth(userId);
    }

    /**
     * 用户投注月统计--某天的详情
     *
     * @param request
     * @param userId
     * @param time
     * @return
     */
    @GetMapping(value = "/orderMonthDays")
    public Response orderMonthDays(HttpServletRequest request, @RequestParam(value = "userId") String userId, @RequestParam(value = "time") Integer time) {
        return StringUtil.isBlankOrNull(userId) || time == null ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                playerOrderService.userOrderMonthDays(userId, time);
    }

    /**
     * 用户投注月盈利
     *
     * @param request
     * @param userId
     * @return
     */
    @GetMapping(value = "/profit")
    public Response profit(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        return StringUtil.isBlankOrNull(userId) ?
                Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                playerOrderService.getPlayerProfit(userId);

    }

    /**
     * 根据用户名查询用户ID列表
     * @param userName
     * @return Response
     */
    @GetMapping(value = "/queryUserIdListByUserName")
    @ApiOperation(value = "/admin/player/queryUserIdListByUserName", notes = "根据用户名查询用户ID列表")
    public Response queryUserIdListByUserName(HttpServletRequest request,@RequestParam(value = "userName") String userName) {
        UserOrderVO vo = new UserOrderVO();
        vo.setUserName(userName);
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
        return playerOrderService.queryUserIdListByUserName(vo);
    }
}
