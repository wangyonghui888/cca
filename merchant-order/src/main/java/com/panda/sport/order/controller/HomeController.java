package com.panda.sport.order.controller;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.order.service.MatchService;
import com.panda.sport.order.service.MerchantOrderService;
import com.panda.sport.order.service.UserOrderService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@RestController
@RequestMapping("/order/home")
@Slf4j
public class HomeController {

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private MerchantOrderService merchantOrderService;

    @Autowired
    private MatchService matchOrderService;

    /**
     * 今日投注信息
     */
    @PostMapping(value = "/queryBetToday")
    @AuthRequiredPermission("Merchant:Order:home:queryBetToday")
    public Response queryBetToday(HttpServletRequest request) {
        log.info("queryBetToday!");
        return userOrderService.queryBetToday();
    }

    /**
     * 今日用户信息
     */
    @PostMapping(value = "/queryUserToday")
    @AuthRequiredPermission("Merchant:Order:home:queryUserToday")
    public Response queryUserToday(HttpServletRequest request) {
        log.info("queryUserToday!");
        return userOrderService.queryUserToday();
    }

    /**
     * 30天投注量趋势
     */
    @PostMapping(value = "/userOrderDay14")
    @AuthRequiredPermission("Merchant:Order:home:userOrderDay")
    public Response userOrderDay14(HttpServletRequest request) {
        log.info("userOrderDay14!!!!!");
        return userOrderService.getMerchantOrderDay();
    }

    /**
     * 30天投注量趋势
     */
    @PostMapping(value = "/userOrderDay30")
    @AuthRequiredPermission("Merchant:Order:home:userOrderDay")
    public Response userOrderDay30(HttpServletRequest request) {
        log.info("userOrderDay30");
        return userOrderService.userOrderDay30();
    }

    /**
     * 30天投注用户趋势
     */
    @PostMapping(value = "/userDay30")
    @AuthRequiredPermission("Merchant:Order:home:userDay")
    public Response userDay30(HttpServletRequest request) {
        try {
            log.info("userDay30:");
            return userOrderService.getMerchantOrderDay();
        } catch (Exception e) {
            log.error("HomeController.userDay30,exception:", e);
            return Response.returnFail(e.getMessage());
        }
    }

    /**
     * 30天投注用户趋势
     */
    @PostMapping(value = "/userDay14")
    @AuthRequiredPermission("Merchant:Order:home:userDay")
    public Response userDay14(HttpServletRequest request) {
        log.info("userDay14!");
        return userOrderService.merchantUser();
    }

    /**
     * 14天投注用户分布
     */
    @PostMapping(value = "/userDaySpread14")
    @AuthRequiredPermission("Merchant:Order:home:userDaySpread")
    public Response userDaySpread14(HttpServletRequest request, @RequestBody(required = false) UserOrderVO vo) {
        log.info("userDaySpread14:" + vo);
        return userOrderService.userDaySpread14(vo);
    }

    /**
     * 今日投注量 TOP10赛事
     */
    @PostMapping(value = "/matchTop10")
    @AuthRequiredPermission("Merchant:Order:home:matchTop")
    public Response matchTop10(HttpServletRequest request) {
        return matchOrderService.queryMatchStatistic10();
    }

    /**
     * 本月商户投注量排行
     */
    @PostMapping(value = "/merchantOrderTop10")
    @AuthRequiredPermission("Merchant:Manage:home:merchantOrderTop")
    public Response merchantOrderTop10(@RequestBody(required = false) MerchantOrderVO requestVO) {
        if (requestVO == null) {
            requestVO = new MerchantOrderVO();
        }
        //查询过滤出直营与渠道商户的排行
        requestVO.setAgentLevelList(Arrays.asList(0,1));
        requestVO.setOrderBy("betAmount");
        requestVO.setSort("desc");
        log.info("HomeController.merchantOrderTop10:" + requestVO);
        return merchantOrderService.queryMerchantTop10(requestVO);
    }

    /**
     * 本月商户盈利排行
     */
    @PostMapping(value = "/merchantProfitTop10")
    @AuthRequiredPermission("Merchant:Manage:home:merchantOrderTop")
    public Response merchantProfitTop10(@RequestBody(required = false) MerchantOrderVO requestVO) {
        if (requestVO == null) {
            requestVO = new MerchantOrderVO();
        }
        //查询过滤出直营与渠道商户的排行
        requestVO.setAgentLevelList(Arrays.asList(0,1));
        requestVO.setOrderBy("settleProfit");
        requestVO.setSort("desc");
        log.info("HomeController.merchantProfitTop10:" + requestVO);
        return merchantOrderService.queryMerchantTop10(requestVO);
    }

}