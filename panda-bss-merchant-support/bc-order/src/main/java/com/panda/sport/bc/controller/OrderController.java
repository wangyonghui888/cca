package com.panda.sport.bc.controller;

import com.panda.sport.bc.service.OrderService;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bc/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 查询体种
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/getSportList")
    public Response getSportList(HttpServletRequest request) {
        String language = request.getHeader("language");
        return orderService.getSportList(StringUtils.isEmpty(language) ? LanguageEnum.EN.getCode() : language);
    }

    /**
     * 查询用户注单按自然日 UTC8
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryTicketList")
    public Response<Object> queryTicketList(HttpServletRequest request, @RequestBody BetOrderVO vo) {
        log.info("/bc/order/queryTicketList:" + vo);
        String language = request.getHeader("language");
        vo.setLanguage(StringUtils.isEmpty(language) ? LanguageEnum.EN.getCode() : language);
        return orderService.queryTicketList(vo);
    }

    /**
     * 注单统计
     *
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 12:03
     */
    @PostMapping(value = "/getStatistics")
    public Response<Object> getStatistics(HttpServletRequest request, @RequestBody BetOrderVO vo) {
        log.info("/bc/order/getStatistics:" + vo);
        String language = request.getHeader("language");
        vo.setLanguage(StringUtils.isEmpty(language) ? LanguageEnum.EN.getCode() : language);
        return orderService.getStatistics(vo);
    }

    /**
     * 导出注单
     *
     * @Param: [request, vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 12:03
     */
    @RequestMapping(value = "/exportTicketList")
    public void exportTicketList(HttpServletRequest request, HttpServletResponse response
            , @RequestParam(value = "userId", required = false) String userId
            , @RequestParam(value = "orderNo", required = false) String orderNo
            , @RequestParam(value = "userName", required = false) String userName
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime
            , @RequestParam(value = "playIdList", required = false) List<Integer> playIdList
            , @RequestParam(value = "minBetAmount", required = false) BigDecimal minBetAmount
            , @RequestParam(value = "maxBetAmount", required = false) BigDecimal maxBetAmount
            , @RequestParam(value = "orderStatus", required = false) Integer orderStatus
            , @RequestParam(value = "matchType", required = false) Integer matchType
            , @RequestParam(value = "matchId", required = false) String matchId
            , @RequestParam(value = "language", required = false) String language
            , @RequestParam(value = "seriesType", required = false) Integer seriesType
            , @RequestParam(value = "settleType", required = false) Integer settleType
            , @RequestParam(value = "orderStatusList", required = false) List<Integer> orderStatusList
            , @RequestParam(value = "outComeList", required = false) List<Integer> outComeList
            , @RequestParam(value = "filter", required = false) String filter
            , @RequestParam(value = "sportId", required = false) Integer sportId, @RequestParam(value = "pageSize", required = false) Integer pageSize
            , @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        try {
            BetOrderVO vo = new BetOrderVO();
            filter = StringUtils.isEmpty(filter) ? "1" : filter;
            vo.setUserId(Long.valueOf(userId));
            vo.setUserName(userName);
            vo.setFilter(filter);
            vo.setSportId(sportId);
            vo.setSize(pageSize);
            vo.setPageNo(pageNum);
            vo.setStartTime(startTime);
            vo.setEndTime(endTime);
            vo.setPlayIdList(playIdList);
            vo.setOrderStatus(orderStatus);
            vo.setOrderStatusList(orderStatusList);
            vo.setMinBetAmount(minBetAmount);
            vo.setMaxBetAmount(maxBetAmount);
            if (matchId != null) {
                vo.setMatchId(Long.parseLong(matchId));
            }
            vo.setMatchType(matchType);
            vo.setOrderNo(orderNo);
            vo.setSeriesType(seriesType);
            vo.setSettleType(settleType);
            vo.setOutComeList(outComeList);
            vo.setLanguage(StringUtils.isEmpty(language) ? LanguageEnum.EN.getCode() : language);
            orderService.exportTicketList(response, vo);
        } catch (Exception e) {
            log.error("UserController.exportUserOrder,exception:", e);
        }
    }

}