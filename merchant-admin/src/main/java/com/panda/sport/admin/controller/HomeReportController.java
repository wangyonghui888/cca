package com.panda.sport.admin.controller;

import com.panda.sport.admin.service.OutMerchantService;
import com.panda.sport.admin.service.PlayerOrderService;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin/home")
public class HomeReportController {

    @Autowired
    protected OutMerchantService outMerchantService;

    @Autowired
    protected PlayerOrderService playerOrderService;


    /**
     * 今日投注信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/queryBetToday")
    public Response queryBetToday(HttpServletRequest request) {
        return playerOrderService.queryBetToday();
    }

    /**
     * 今日用户信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/queryUserToday")
    public Response queryUserToday(HttpServletRequest request) {
        return playerOrderService.queryUserToday();
    }

    /**
     * 30天投注量趋势
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/userOrderDay14")
    public Response userOrderDay14(HttpServletRequest request) {

        return playerOrderService.merchantOrder();

    }

    /**
     * 30天投注用户趋势
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/userDay14")
    public Response userDay14(HttpServletRequest request) {
        return playerOrderService.userDaySpread30();
    }

    /**
     * 投注赛事top10
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/matchTop10")
    public Response matchTop10(HttpServletRequest request) {

        return playerOrderService.merchantMatchTop10();

    }

    /**
     * 渠道商户首页渠道商户下级投注金额增速排行
     * 直营/二级商户 用户投注金额增速排行
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/merchantChannelOrderTop10")
    public Response merchantChannelOrderTop10(HttpServletRequest request) {
        return outMerchantService.merchantChannelOrderTop10();
    }
}
