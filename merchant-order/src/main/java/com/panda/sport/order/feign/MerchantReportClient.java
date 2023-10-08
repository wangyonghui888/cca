package com.panda.sport.order.feign;


import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.*;

import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.merchant.common.vo.finance.FinanceDayVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = "panda-merchant-report-api", fallbackFactory =  MerchantReportFactory.class)
public interface MerchantReportClient {

    @GetMapping(value = "/report/user/orderMonth")
    Response<?> userOrderMonth(@RequestParam(value = "userId") String userId);

    @GetMapping(value = "/report/user/orderMonth/days")
    Response<?> userOrderMonthDays(@RequestParam(value = "userId") String userId, @RequestParam(value = "time") Integer time);

    @GetMapping(value = "/report/user/profit")
    Response<?> getPlayerProfit(@RequestParam(value = "userId") String userId);

    @PostMapping(value = "/report/user/queryUserBetListByTime")
    Response<?> queryUserBetListByTime(@RequestBody UserOrderVO vo);

    @GetMapping(value = "/report/user/listUserBetGroupByUser")
    Response<?> listUserBetGroupByUser(UserOrderVO vo);

    @PostMapping(value = "/report/user/queryUserReport")
    List<?> queryUserReport(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/user/queryUserOrderAllList")
    List<UserOrderAllPO> queryUserOrderAllList(@RequestBody UserOrderVO vo);

    @GetMapping(value = "/report/user/selectByUser")
    UserOrderAllPO selectByUser(@RequestParam(value = "userId") String userId);

    @GetMapping(value = "/report/merchant/getMerchantAvgBetAmount")
    List<Map<String,Object>> getMerchantAvgBetAmount();

    @PostMapping(value = "/report/merchant/merchantList")
    Response<?> merchantList(@RequestBody MerchantOrderVO requestVO);

    @PostMapping(value = "/report/merchant/listGroupByMerchant")
    Response<Map<String, Object>> listGroupByMerchant(@RequestBody MerchantOrderVO requestVO);

    @PostMapping(value = "/report/merchant/listGroupByMerchantRepeatUser")
    Response<Map<String, Object>> listGroupByMerchantRepeatUser(@RequestBody MerchantOrderVO requestVO);

    @PostMapping(value = "/report/merchant/merchantDataQuery")
    List<?> merchantDataQuery(@RequestBody MerchantOrderVO requestVO);


    //--------赛事统计列表

    @PostMapping(value = "/report/match/queryMatchStatisticList")
    Map<String, Object> queryMatchStatisticList(@RequestBody SportVO requestVO);

    @PostMapping(value = "/report/match/queryMatchStatisticListNew")
    Map<String, Object> queryMatchStatisticListNew(@RequestBody MerchantMatchBetVo merchantMatchBetVo);

    @PostMapping(value = "/report/match/queryMerchantMatchStatisticList")
    Map<String, Object> queryMerchantMatchStatisticList(@RequestBody SportVO merchantOrderRequestVO);

    @PostMapping(value = "/report/match/queryPlayStatisticList")
    Response queryPlayStatisticList(@RequestBody SportVO merchantOrderRequestVO);

    @PostMapping(value = "/report/match/queryMerchantPlayStatisticList")
    Response<?> queryMerchantPlayStatisticList(SportVO merchantOrderRequestVO);

    @PostMapping(value = "/report/match/exportMerchantMatchStatistic")
    List<?> queryMatchStatistic(@RequestBody Map<String, Object> map);

    @PostMapping(value = "/report/match/exportMerchantPlayStatisticList")
    List<?> exportMerchantPlayStatisticList(@RequestBody Map<String, Object> map);

    @PostMapping(value = "/report/match/queryMatchStatistic")
    List<?> exportMatchStatistic(@RequestBody Map<String, Object> map);

    @PostMapping(value = "/report/match/exportMatchPlayStatisticList")
    List<?> exportMatchPlayStatisticList(@RequestBody Map<String, Object> ma);


    @PostMapping(value = "/report/home/queryUserToday")
    Map<String, Object> queryUserToday(@RequestBody MerchantOrderVO vo);


    @PostMapping(value = "/report/home/merchantOrderDay")
    Response<?> merchantOrderDay(@RequestBody MerchantOrderVO vo);


    @PostMapping(value = "/report/home/merchantUser")
    Response merchantUser(MerchantOrderVO merchantOrderVO);


    @PostMapping(value = "/report/home/queryBetToday")
    Response<?> queryBetToday(@RequestBody MerchantOrderVO vo);

    @PostMapping(value = "/report/home/userOrderDay14")
    Response<?> userOrderDay14(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/home/userOrderDay30")
    Response<?> userOrderDay30(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/home/userDaySpread14")
    Response<?> userDaySpread14(@RequestBody UserOrderVO vo);

    @PostMapping(value = "/report/home/merchantOrderTop10")
    Response<?> queryMerchantTop10(@RequestBody MerchantOrderVO vo);

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthList")
    Response<?> queryFinanceMonthList(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthTotal")
    Response<?> queryFinanceMonthTotal(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthDetail")
    Response<?> queryFinanceMonthDetail(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/financeMonth/updateFinanceMonthDetail")
    Response<?> updateFinanceMonthDetail(@RequestBody MerchantFinanceMonthVo monthVo);

    @PostMapping(value = "/report/financeMonth/getFinanceOperateRecordList")
    Response<?> getFinanceOperateRecordList(@RequestParam(value = "financeId") String financeId);


    @PostMapping(value = "/report/financeMonth/queryFinanceayTotalList")
    Response<?> queryFinanceayTotalList(@RequestBody MerchantFinanceDayVo dayVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceDayListV2")
    Response<?> queryFinanceDayListV2(@RequestBody MerchantFinanceDayVo dayVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceDayList")
    Response<?> queryFinanceDayList(@RequestBody MerchantFinanceDayVo dayVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceDayTotal")
    Response queryFinanceDayTotal(@RequestBody MerchantFinanceDayVo dayVo);

    @PostMapping(value = "/report/financeMonth/queryFinanceMonthCount")
    Response<?> queryFinanceMonthCount(@RequestBody MerchantFinanceMonthVo monthVo);

    @GetMapping(value = "/report/financeMonth/financeDayExportQuery")
    List<?> financeDayExportQuery(@RequestParam(value = "financeDayId") String financeDayId);

    @GetMapping(value = "/report/financeMonth/financeDayExportListQuery")
    List<?> financeDayExportListQuery(@RequestParam(value = "financeDayId") String financeDayId);

    @GetMapping(value = "/report/financeMonth/financeMonthExportQuery")
    MerchantFinanceMonthVo financeMonthExportQuery(@RequestParam(value = "id") String id);

    //活动相关
    @GetMapping(value = "/report/user/queryUserTop100")
    List<UserOrderDayPO> queryUserTop100(@RequestParam(value = "startDateL") Long startDateL, @RequestParam(value = "endDateL") Long endDateL);

    @GetMapping(value = "/report/user/queryUserAcBetList")
    List<UserOrderDayPO> queryUserAcBetList(@RequestParam(value = "userIdList") List<Long> userIdList, @RequestParam(value = "startDateL") Long startDateL,
                                            @RequestParam(value = "endDateL") long endDateL);

    @GetMapping(value = "/report/activity/queryOlympicTimesUser")
    List<Map<String, Object>> queryOlympicTimesUser(@RequestParam(value = "times") int times, @RequestParam(value = "timeL") Long timeL);

    @GetMapping(value = "/report/activity/queryOlympicSeriesTimesUser")
    List<Map<String, Object>> queryOlympicSeriesTimesUser(@RequestParam(value = "times") int times, @RequestParam(value = "timeL") Long timeL,
                                                          @RequestParam(value = "betAmount") Double betAmount);

    @GetMapping(value = "/report/activity/queryOlympicBetAmountUser")
    List<Map<String, Object>> queryOlympicBetAmountUser(@RequestParam(value = "betAmount") BigDecimal betAmount, @RequestParam(value = "timeL") Long timeL);

    @GetMapping(value = "/report/activity/queryOlympicPlayTimesUser")
    List<Map<String, Object>> queryOlympicPlayTimesUser(@RequestParam(value = "playId") String playId, @RequestParam(value = "times") int times, @RequestParam(value = "timeL") Long timeL);

    @GetMapping(value = "/report/activity/queryOlympicMardiGras")
    List<Map<String, Object>> queryOlympicMardiGras(@RequestParam(value = "betAmount") Double betAmount, @RequestParam(value = "startDateL") Long startDateL,
                                                    @RequestParam(value = "endDateL") Long endDateL);

    @GetMapping(value = "/report/activity/queryOlympicBetEvery")
    List<Map<String, Object>> queryOlympicBetEvery(@RequestParam(value = "days") int days, @RequestParam(value = "betAmount") Double bet, @RequestParam(value = "startDateL") Long startDateL, @RequestParam(value = "endDateL") Long endDateL);


    @PostMapping(value = "/report/activity/getActivityBetStatList")
    PageInfo<ActivityBetStatVO> getActivityBetStatList(ActivityBetStatDTO dto);

    @PostMapping(value = "/report/activity/exportExcel")
    List<ActivityBetStatVO> exportExcel(ActivityBetStatDTO dto);

    @PostMapping(value = "")
    Map<String, Object> queryAbnormalList(AbnormalVo abnormalVo);


    @PostMapping(value = "/report/financeMonth/rebootFinanceDay")
    Response<?> rebootFinanceDay(@RequestBody FinanceDayVO financeDayVO);

    @PostMapping(value = "/report/financeMonth/closeFinanceDay")
    Response<?> closeFinanceDay(@RequestBody FinanceDayVO financeDayVO);

    @GetMapping(value = "/report/clean/clearPressureReportDailyData")
    Response<?> clearPressureReportDailyData(@RequestParam(value = "size")  Integer size,@RequestParam(value = "startSize") Integer startSize);

    @GetMapping(value = "/report/clean/clearPressureUserReportDailyData")
    Response<?> clearPressureUserReportDailyData(@RequestParam(value = "size")  Integer size,@RequestParam(value = "startSize")  Integer startSize);

    @PostMapping(value = "/report/match/queryMatchStatisticById")
    Map<String, Object> queryMatchStatisticById(@RequestBody MerchantMatchBetVo merchantMatchBetVo);


    @PostMapping(value = "/report/ticket/queryTicketList")
    TicketResponseVO queryTicketList(@RequestBody BetOrderVO betOrderVO);


    @PostMapping(value = "/report/ticket/getStatistics")
    TicketResponseVO getStatistics(@RequestBody BetOrderVO betOrderVO);

    @PostMapping(value = "/report/ticket/deleteByQuery")
    String deleteByQuery(@RequestParam(value = "date")  String date);
    @PostMapping(value = "/report/activity/queryAcTaskUserInfo")
    List<Map<String, Object>>  queryAcTaskUserInfo(@RequestParam(value = "startL")  Long startL,@RequestParam(value = "endL")  Long endL,
                                                   @RequestParam(value = "datStartL")  Long datStartL, @RequestParam(value = "startHourTimeL")  Long datEndL,@RequestBody AcTaskParamVO acTaskParamVOs);

    @GetMapping(value = "/report/clean/userOrderAllClearDataTask")
    void userOrderAllClearDataTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "num") Integer num);

    @GetMapping(value = "/report/clean/acUserOrderDateClearDataTask")
    void acUserOrderDateClearDataTask(@RequestParam(value = "curTime") Long nowL);

    @PostMapping(value = "/report/activity/queryAcSevenTaskUserInfo")
    List<Map<String, Object>> queryAcSevenTaskUserInfo(@RequestParam(value = "startL")  Long startL,@RequestParam(value = "endL")  Long endL,
                                                       @RequestParam(value = "datStartL")  Long datStartL, @RequestParam(value = "datEndL")  Long datEndL,@RequestBody AcTaskParamVO acTaskParamVOs);

    @GetMapping(value = "/report/clean/userOrderDayClearDataTask")
    void userOrderDayClearDataTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "num") Integer num);

    @GetMapping(value = "/report/clean/userOrderDayUtfClearDataTask")
    void userOrderDayUtfClearDataTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "num") Integer num);

    @GetMapping(value = "/report/clean/userOrderMonthClearDataTask")
    void userOrderMonthClearDataTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "num") Integer num);

    @GetMapping(value = "/report/clean/userOrderMonthUtfClearDataTask")
    void userOrderMonthUtfClearDataTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "num") Integer num);
}

