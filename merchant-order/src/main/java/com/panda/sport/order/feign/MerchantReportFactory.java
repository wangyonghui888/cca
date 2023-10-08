package com.panda.sport.order.feign;/**
 * @author Administrator
 * @date 2021/5/9
 * @TIME 12:02
 */

import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.finance.FinanceDayVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *@ClassName RcsRemoteHystrix
 *@Description TODO
 *@Author Administrator
 *@Date 2021/5/9 12:02
 */
@Slf4j
@Component
public class MerchantReportFactory implements FallbackFactory<MerchantReportClient> {


    @Override
    public MerchantReportClient create(Throwable throwable) {
        log.error("MerchantReportClient error",throwable);
        return new PandMerchantReportApiBack(){

            @Override
            public Response<?> userOrderMonth(String userId) {
                return null;
            }

            @Override
            public Response<?> userOrderMonthDays(String userId, Integer time) {
                return null;
            }

            @Override
            public Response<?> getPlayerProfit(String userId) {
                return null;
            }

            @Override
            public Response<?> queryUserBetListByTime(UserOrderVO vo) {
                return null;
            }

            @Override
            public Response<?> listUserBetGroupByUser(UserOrderVO vo) {
                return null;
            }

            @Override
            public List<?> queryUserReport(UserOrderVO vo) {
                return null;
            }

            @Override
            public List<UserOrderAllPO> queryUserOrderAllList(UserOrderVO vo) {
                return null;
            }

            @Override
            public UserOrderAllPO selectByUser(String userId) {
                return null;
            }

            @Override
            public List<Map<String, Object>> getMerchantAvgBetAmount() {
                return null;
            }

            @Override
            public Response<?> merchantList(MerchantOrderVO requestVO) {
                return null;
            }

            @Override
            public Response<Map<String, Object>> listGroupByMerchant(MerchantOrderVO requestVO) {
                return null;
            }

            @Override
            public Response<Map<String, Object>> listGroupByMerchantRepeatUser(MerchantOrderVO requestVO) {
                return null;
            }

            @Override
            public List<?> merchantDataQuery(MerchantOrderVO requestVO) {
                return null;
            }

            @Override
            public Map<String, Object> queryMatchStatisticList(SportVO requestVO) {
                return null;
            }

            @Override
            public Map<String, Object> queryMatchStatisticListNew(MerchantMatchBetVo merchantMatchBetVo) {
                return null;
            }

            @Override
            public Map<String, Object> queryMerchantMatchStatisticList(SportVO merchantOrderRequestVO) {
                return null;
            }

            @Override
            public Response queryPlayStatisticList(SportVO merchantOrderRequestVO) {
                return null;
            }

            @Override
            public Response<?> queryMerchantPlayStatisticList(SportVO merchantOrderRequestVO) {
                return null;
            }

            @Override
            public List<?> queryMatchStatistic(Map<String, Object> map) {
                return null;
            }

            @Override
            public List<?> exportMerchantPlayStatisticList(Map<String, Object> map) {
                return null;
            }

            @Override
            public List<?> exportMatchStatistic(Map<String, Object> map) {
                return null;
            }

            @Override
            public List<?> exportMatchPlayStatisticList(Map<String, Object> ma) {
                return null;
            }

            @Override
            public Map<String, Object> queryUserToday(MerchantOrderVO vo) {
                return null;
            }

            @Override
            public Response<?> merchantOrderDay(MerchantOrderVO vo) {
                return null;
            }

            @Override
            public Response merchantUser(MerchantOrderVO merchantOrderVO) {
                return null;
            }

            @Override
            public Response<?> queryBetToday(MerchantOrderVO vo) {
                return null;
            }

            @Override
            public Response<?> userOrderDay14(UserOrderVO vo) {
                return null;
            }

            @Override
            public Response<?> userOrderDay30(UserOrderVO vo) {
                return null;
            }

            @Override
            public Response<?> userDaySpread14(UserOrderVO vo) {
                return null;
            }

            @Override
            public Response<?> queryMerchantTop10(MerchantOrderVO vo) {
                return null;
            }

            @Override
            public Response<?> queryFinanceMonthList(MerchantFinanceMonthVo monthVo) {
                return null;
            }

            @Override
            public Response<?> queryFinanceMonthTotal(MerchantFinanceMonthVo monthVo) {
                return null;
            }

            @Override
            public Response<?> queryFinanceMonthDetail(MerchantFinanceMonthVo monthVo) {
                return null;
            }

            @Override
            public Response<?> updateFinanceMonthDetail(MerchantFinanceMonthVo monthVo) {
                return null;
            }

            @Override
            public Response<?> getFinanceOperateRecordList(String financeId) {
                return null;
            }

            @Override
            public Response<?> queryFinanceayTotalList(MerchantFinanceDayVo dayVo) {
                return null;
            }

            @Override
            public Response<?> queryFinanceDayListV2(MerchantFinanceDayVo dayVo) {
                return null;
            }

            @Override
            public Response<?> queryFinanceDayList(MerchantFinanceDayVo dayVo) {
                return null;
            }

            @Override
            public Response queryFinanceDayTotal(MerchantFinanceDayVo dayVo) {
                return null;
            }

            @Override
            public Response<?> queryFinanceMonthCount(MerchantFinanceMonthVo monthVo) {
                return null;
            }

            @Override
            public List<?> financeDayExportQuery(String financeDayId) {
                return null;
            }

            @Override
            public List<?> financeDayExportListQuery(String financeDayId) {
                return null;
            }

            @Override
            public MerchantFinanceMonthVo financeMonthExportQuery(String id) {
                return null;
            }

            @Override
            public List<UserOrderDayPO> queryUserTop100(Long startDateL, Long endDateL) {
                return null;
            }

            @Override
            public List<UserOrderDayPO> queryUserAcBetList(List<Long> userIdList, Long startDateL, long endDateL) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryOlympicTimesUser(int times, Long timeL) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryOlympicSeriesTimesUser(int times, Long timeL, Double betAmount) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryOlympicBetAmountUser(BigDecimal betAmount, Long timeL) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryOlympicPlayTimesUser(String playId, int times, Long timeL) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryOlympicMardiGras(Double betAmount, Long startDateL, Long endDateL) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryOlympicBetEvery(int days, Double bet, Long startDateL, Long endDateL) {
                return null;
            }

            @Override
            public PageInfo<ActivityBetStatVO> getActivityBetStatList(ActivityBetStatDTO dto) {
                return null;
            }

            @Override
            public List<ActivityBetStatVO> exportExcel(ActivityBetStatDTO dto) {
                return null;
            }

            @Override
            public Map<String, Object> queryAbnormalList(AbnormalVo abnormalVo) {
                return null;
            }

            @Override
            public Response<?> rebootFinanceDay(FinanceDayVO financeDayVO) {
                return null;
            }

            @Override
            public Response<?> closeFinanceDay(FinanceDayVO financeDayVO) {
                return null;
            }

            @Override
            public Response<?> clearPressureReportDailyData(Integer size, Integer startSize) {
                return null;
            }

            @Override
            public Response<?> clearPressureUserReportDailyData(Integer size, Integer startSize) {
                return null;
            }

            @Override
            public Map<String, Object> queryMatchStatisticById(MerchantMatchBetVo merchantMatchBetVo) {
                return null;
            }

            @Override
            public TicketResponseVO queryTicketList(BetOrderVO betOrderVO) {
                return null;
            }

            @Override
            public TicketResponseVO getStatistics(BetOrderVO betOrderVO) {
                return null;
            }

            @Override
            public String deleteByQuery(String date) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryAcTaskUserInfo(Long startL, Long endL,Long datStartL, Long startHourTimeL, AcTaskParamVO acTaskParamVOs) {
                return null;
            }

            @Override
            public List<Map<String, Object>> queryAcSevenTaskUserInfo(Long startL, Long endL,Long datStartL, Long datEndL, AcTaskParamVO acTaskParamVOs) {
                return null;
            }

            @Override
            public void userOrderDayClearDataTask(String merchantCode, Integer num) {
                log.error(num + ",userOrderDayClearDataTask error,RPC接口异常");
            }

            @Override
            public void userOrderDayUtfClearDataTask(String merchantCode, Integer num) {
                log.error(num + ",userOrderDayUtfClearDataTask error,RPC接口异常");
            }

            @Override
            public void userOrderMonthClearDataTask(String merchantCode, Integer num) {
                log.error(num + ",userOrderMonthClearDataTask error,RPC接口异常");
            }

            @Override
            public void userOrderMonthUtfClearDataTask(String merchantCode, Integer num) {
                log.error(num + ",userOrderMonthClearDataTask error,RPC接口异常");
            }

            @Override
            public void userOrderAllClearDataTask(String merchantCode, Integer num) {
                log.error(merchantCode + ",userOrderAllClearDataTask error,RPC接口异常");
            }

            @Override
            public void acUserOrderDateClearDataTask(Long nowL) {
                log.error(nowL + ",acUserOrderDateClearDataTask error,RPC接口异常");
            }

        };
    }
}
