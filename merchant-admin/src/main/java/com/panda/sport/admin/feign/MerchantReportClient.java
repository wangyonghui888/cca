package com.panda.sport.admin.feign;


import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.common.vo.merchant.TicketResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@FeignClient(value = "panda-merchant-report-api", fallbackFactory = RemoteHystrix.class)
public interface MerchantReportClient {
    @GetMapping(value = "/report/merchant/valarTest")
    Response createAdmin(@RequestParam(value = "userId") String userId);

    @PostMapping(value = "/report/merchant/merchantList")
    Response queryMerchantReportList(@RequestBody MerchantOrderVO merchantOrderRequestVO);

    @PostMapping(value = "/report/merchant/listGroupByMerchant")
    Response listGroupByMerchant(@RequestBody MerchantOrderVO merchantOrderRequestVO);

    @PostMapping(value = "/report/merchant/listGroupByMerchantRepeatUser")
    Response<Map<String, Object>> listGroupByMerchantRepeatUser(MerchantOrderVO vo);

    @PostMapping(value = "/report/user/listUserBetGroupByUser")
    Response<?> listUserBetGroupByUser(UserOrderVO vo);

    @PostMapping(value = "/report/merchant/merchantDataQuery")
    List<?> merchantDataQuery(@RequestBody MerchantOrderVO requestVO);

    @PostMapping(value = "/report/user/queryUserBetListByTime")
    Response<?> queryUserBetListByTime(@RequestBody UserOrderVO vo);


    @PostMapping(value = "/report/user/queryUserReport")
    List<?> queryUserReport(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/user/queryUserOrderAllList")
    List<?> queryUserOrderAllList(@RequestBody UserOrderVO vo);

    @GetMapping(value = "/report/user/selectByUser")
    UserOrderAllPO selectByUser(@RequestParam(value = "userId") String userId);

    //------------首页
    @PostMapping(value = "/report/home/queryBetToday")
    Response<?> queryBetToday(@RequestBody MerchantOrderVO vo);

    @PostMapping(value = "/report/home/userOrderTop10")
    Response userOrderTop10(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/home/queryUserToday")
    Map<String,Object> queryUserToday(@RequestBody MerchantOrderVO vo);

    @PostMapping(value = "/report/home/userOrderDay14")
    Response userOrderDay14(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/home/userDaySpread14")
    Response userDaySpread14(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/home/merchantOrderTop10")
    Response queryMerchantTop10(@RequestBody MerchantOrderVO requestVO);

    @PostMapping(value = "/report/home/merchantMatchTop10")
    Map<String, Object> merchantMatchTop10(@RequestBody SportVO vo);

    @PostMapping(value = "/report/home/merchantChannelOrderTop10")
    Response merchantChannelOrderTop10(@RequestBody MerchantOrderVO vo);

    @GetMapping(value = "/report/user/orderMonth")
    Response userOrderMonth(@RequestParam(value = "userId") String userId);

    @GetMapping(value = "/report/user/orderMonth/days")
    Response userOrderMonthDays(@RequestParam(value = "userId") String userId, @RequestParam(value = "time") Integer time);

    @GetMapping(value = "/report/user/profit")
    Response getPlayerProfit(@RequestParam(value = "userId") String userId);

    @PostMapping(value = "/report/home/merchantOrderDay")
    Response merchantOrderDay(@RequestBody MerchantOrderVO vo);

    @PostMapping(value = "/report/home/userDaySpread30")
    Response userDaySpread30(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/match/queryMatchStatisticListNew")
    Map<String,Object> queryMatchStatisticListNew(@RequestBody MerchantMatchBetVo merchantMatchBetVo);


    //--------赛事统计列表

    @PostMapping(value = "/report/match/queryMatchStatisticList")
    Map<String,Object> queryMatchStatisticList(@RequestBody SportVO requestVO);

    @PostMapping(value = "/report/match/queryMerchantMatchStatisticList")
    Map<String, Object> queryMerchantMatchStatisticList(@RequestBody SportVO merchantOrderRequestVO);

    @PostMapping(value = "/report/match/queryMerchantPlayStatisticList")
    Response queryMerchantPlayStatisticList(@RequestBody SportVO merchantOrderRequestVO);

    @PostMapping(value = "/report/match/exportMerchantMatchStatistic")
    List<?> exportMerchantMatchStatistic(@RequestBody Map<String, Object> map);

    @PostMapping(value = "/report/match/exportMerchantPlayStatisticList")
    List<?> exportMerchantPlayStatisticList(@RequestBody Map<String, Object> ma);

    //--------------   财务中心相关 --开始

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthList")
    Response queryFinanceMonthList(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthTotal")
    Response queryFinanceMonthTotal(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthDetail")
    Response queryFinanceMonthDetail(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceDayList")
    Response queryFinanceDayList(@RequestBody MerchantFinanceDayVo dayVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceDayTotal")
    Response queryFinanceDayTotal(@RequestBody MerchantFinanceDayVo dayVo);

    @GetMapping(value = "/report/financeMonth/financeDayExportQuery")
    List<?> financeDayExportQuery(@RequestParam(value = "financeDayId") String financeDayId);

    @GetMapping(value = "/report/financeMonth/financeDayExportListQuery")
    List<?> financeDayExportListQuery(@RequestParam(value = "financeDayId") String financeDayId);

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthCount")
    Response queryFinanceMonthCount(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/match/exportMerchantPlayStatisticList")
    List<?> exportMatchPlayStatisticList(@RequestBody Map<String, Object> ma);



    @PostMapping(value = "/report/financeMonth/queryFinanceDayListV2")
    Response queryFinanceDayListV2(@RequestBody MerchantFinanceDayVo monthVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceayTotalList")
    Response queryFinanceayTotalList(@RequestBody MerchantFinanceDayVo monthVo);
    //--------------   财务中心相关 --结束



    @PostMapping(value = "/report/activity/exportExcel")
    List<ActivityBetStatVO> exportExcel(ActivityBetStatDTO dto);

    /**
     * 财务中心/商户级别对账工具-开始对账
     * @param queryReqVO
     * @return CheckToolsQueryRespVO
     */
    @PostMapping(value = "/report/check/checkFinance")
    CheckToolsQueryRespVO checkFinance(@RequestBody CheckToolsQueryReqVO queryReqVO);

    /**
     * 财务中心/商户级别对账工具-修正对账
     * @param editReqVO
     * @return CheckToolsEditReqVO
     */
    @PostMapping(value = "/report/check/editFinance")
    CheckToolsEditRespVO editFinance(@RequestBody CheckToolsEditReqVO editReqVO);


    /**
     * 财务中心/用户级别对账工具-开始对账
     * @param queryReqVO
     * @return CheckToolsQueryRespVO
     */
    @PostMapping(value = "/report/check/checkUserFinance")
    CheckToolsQueryRespVO checkUserFinance(@RequestBody CheckToolsQueryReqVO queryReqVO);

    /**
     * 财务中心/用户级别对账工具-修正对账
     * @param editReqVO
     * @return CheckToolsEditReqVO
     */
    @PostMapping(value = "/report/check/editUserFinance")
    CheckToolsEditRespVO editUserFinance(@RequestBody CheckToolsEditReqVO editReqVO);

    @PostMapping(value = "/report/match/queryMatchStatisticById")
    Map<String, Object> queryMatchStatisticById(@RequestBody MerchantMatchBetVo merchantMatchBetVo);

    @PostMapping(value = "/report/ticket/queryTicketList")
    TicketResponseVO queryTicketList(@RequestBody BetOrderVO betOrderVO);

    @PostMapping(value = "/report/ticket/getStatistics")
    TicketResponseVO getStatistics(@RequestBody BetOrderVO betOrderVO);
}
