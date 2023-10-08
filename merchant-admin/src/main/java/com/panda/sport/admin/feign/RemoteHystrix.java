package com.panda.sport.admin.feign;


import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author :  ives
 * @Description :  微服务调用异常处理类
 * @Date: 2022-01-24 17:59
 */
@Slf4j
@Component
public class RemoteHystrix implements FallbackFactory<MerchantReportClient> {

    @Override
    public MerchantReportClient create(Throwable cause) {

        log.error("BssRemoteHystrix error",cause);

        return new PandBssFallBack() {
            @Override
            public Response createAdmin(String userId) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response queryMerchantReportList(MerchantOrderVO merchantOrderRequestVO) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public List<?> merchantDataQuery(MerchantOrderVO requestVO) {
                log.error("RPC查询报表服务异常!" + requestVO);
                return null;
            }

            @Override
            public Response listGroupByMerchant(MerchantOrderVO merchantOrderRequestVO) {
                log.error("RPC查询报表服务异常! listGroupByMerchant" + merchantOrderRequestVO);
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response<Map<String, Object>> listGroupByMerchantRepeatUser(MerchantOrderVO vo) {
                log.error("RPC查询报表服务异常! listGroupByMerchantRepeatUser" + vo);
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response<?> listUserBetGroupByUser(UserOrderVO vo) {
                log.error("RPC查询报表服务异常! listUserBetGroupByUser" + vo);
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response queryUserBetListByTime(UserOrderVO vo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public List<?> queryUserReport(UserOrderVO vo) {
                log.error("RPC查询报表服务异常!");
                return null;
            }

            @Override
            public List<?> queryUserOrderAllList(UserOrderVO vo) {
                log.error("RPC查询报表服务异常!queryUserOrderAllList:" + vo);
                return null;
            }

            @Override
            public UserOrderAllPO selectByUser(String userId) {
                log.error("RPC查询报表服务异常!selectByUser" + userId);
                return null;
            }

            @Override
            public Response queryBetToday(MerchantOrderVO vo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response userOrderTop10(UserOrderVO vo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Map<String, Object> queryUserToday(MerchantOrderVO vo) {
                log.error("RPC请求异常:" + vo);
                return null;
            }

            @Override
            public Map<String, Object> queryMatchStatisticList(SportVO requestVO) {
                return null;
            }

            @Override
            public Response userOrderDay14(UserOrderVO vo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response userDaySpread14(UserOrderVO vo) {
                return Response.returnFail("请求超时了");
            }


            @Override
            public Response queryMerchantTop10(MerchantOrderVO requestVO) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Map<String, Object> merchantMatchTop10(SportVO vo) {
                log.error("merchantMatchTop10RPC请求异常:" + vo);
                return null;
            }

            @Override
            public Response merchantChannelOrderTop10(MerchantOrderVO vo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response userOrderMonth(String userId) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response userOrderMonthDays(String userId, Integer time) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response getPlayerProfit(String userId) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response merchantOrderDay(MerchantOrderVO vo) {
                log.error("RPC请求异常:" + vo);
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response userDaySpread30(UserOrderVO vo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Map<String, Object> queryMatchStatisticListNew(MerchantMatchBetVo merchantMatchBetVo) {
                log.error("RPC请求异常:" + merchantMatchBetVo);
                return null;
            }

            @Override
            public Map<String, Object> queryMerchantMatchStatisticList(SportVO vo) {
                log.error("queryMerchantMatchStatisticList,RPC请求异常:" + vo);
                return null;
            }

            @Override
            public Response queryMerchantPlayStatisticList(SportVO merchantOrderRequestVO) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public List<MatchBetInfoVO> exportMerchantMatchStatistic(Map<String, Object> map) {
                log.error("RPC查询报表服务异常!");
                return null;
            }

            @Override
            public List<MatchBetInfoVO> exportMerchantPlayStatisticList(Map<String, Object> map) {
                log.error("RPC查询报表服务异常!");
                return null;
            }

            @Override
            public Response queryFinanceMonthList(MerchantFinanceMonthVo monthVo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response queryFinanceMonthDetail(MerchantFinanceMonthVo monthVo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response queryFinanceDayList(MerchantFinanceDayVo dayVo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response queryFinanceDayTotal(MerchantFinanceDayVo dayVo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public List<?> financeDayExportQuery(String dayVo) {
                log.error("RPC调用账单中心超时!" + dayVo);
                return null;
            }

            @Override
            public List<?> financeDayExportListQuery(String financeDayId) {
                log.error("RPC调用账单中心超时!" + financeDayId);
                return null;
            }

            @Override
            public Response queryFinanceMonthCount(MerchantFinanceMonthVo monthVo) {
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response queryFinanceMonthTotal(MerchantFinanceMonthVo monthVo) {
                // TODO Auto-generated method stub
                return Response.returnFail("请求超时了");
            }

            @Override
            public List<?> exportMatchPlayStatisticList(Map<String, Object> ma) {
                log.error("exportMatchPlayStatisticList 超时!" + ma);
                return null;
            }

            @Override
            public Response queryFinanceDayListV2(MerchantFinanceDayVo monthVo) {
                log.error("queryFinanceDayListV2 超时!" + monthVo);
                return Response.returnFail("请求超时了");
            }

            @Override
            public Response queryFinanceayTotalList(MerchantFinanceDayVo monthVo) {
                log.error("queryFinanceayTotalList 超时!" + monthVo);
                return Response.returnFail("请求超时了");
            }

            @Override
            public List<ActivityBetStatVO> exportExcel(ActivityBetStatDTO dto) {
                log.error("经营管理-活动投注统计 超时!" + dto);
                return null;
            }

            @Override
            public CheckToolsQueryRespVO checkFinance(CheckToolsQueryReqVO queryReqVO) {
                CheckToolsQueryRespVO checkToolsQueryRespVO = new CheckToolsQueryRespVO();
                checkToolsQueryRespVO.setCheckResult(CommonDefaultValue.ResultStatus.ERROR);
                checkToolsQueryRespVO.setCheckList(new ArrayList<>());
                checkToolsQueryRespVO.setDateType(queryReqVO.getDateType());
                checkToolsQueryRespVO.setErrMessage("checkFinance 调用报表微服务 " + cause.getMessage());
                log.error("checkFinance 调用 panda-merchant-report-api微服务" + cause);
                return checkToolsQueryRespVO;
            }

            @Override
            public CheckToolsEditRespVO editFinance(CheckToolsEditReqVO editReqVO) {
                CheckToolsEditRespVO checkToolsEditRespVO = new CheckToolsEditRespVO();
                checkToolsEditRespVO.setCheckResult(CommonDefaultValue.ResultStatus.ERROR);
                checkToolsEditRespVO.setErrMessage("editFinance 调用报表微服务 " + cause.getMessage());
                log.error("editFinance 调用 panda-merchant-report-api微服务" + cause);
                return checkToolsEditRespVO;
            }

            @Override
            public CheckToolsQueryRespVO checkUserFinance(CheckToolsQueryReqVO queryReqVO) {
                CheckToolsQueryRespVO checkToolsQueryRespVO = new CheckToolsQueryRespVO();
                checkToolsQueryRespVO.setCheckResult(CommonDefaultValue.ResultStatus.ERROR);
                checkToolsQueryRespVO.setCheckList(new ArrayList<>());
                checkToolsQueryRespVO.setDateType(queryReqVO.getDateType());
                checkToolsQueryRespVO.setErrMessage("checkUserFinance 调用报表微服务 " + cause.getMessage());
                log.error("checkUserFinance 调用 panda-merchant-report-api微服务" + cause);
                return checkToolsQueryRespVO;
            }


            @Override
            public CheckToolsEditRespVO editUserFinance(CheckToolsEditReqVO editReqVO) {
                CheckToolsEditRespVO checkToolsEditRespVO = new CheckToolsEditRespVO();
                checkToolsEditRespVO.setCheckResult(CommonDefaultValue.ResultStatus.ERROR);
                checkToolsEditRespVO.setErrMessage("editUserFinance 调用报表微服务 " + cause.getMessage());
                log.error("editUserFinance 调用 panda-merchant-report-api微服务" + cause);
                return checkToolsEditRespVO;
            }

            @Override
            public Map<String, Object> queryMatchStatisticById(MerchantMatchBetVo merchantMatchBetVo) {
                log.error("queryMatchStatisticById:" + merchantMatchBetVo);
                return null;
            }


            @Override
            public TicketResponseVO queryTicketList(BetOrderVO betOrderVO) {
                log.error("queryTicketList:" + betOrderVO);
                return null;
            }

            @Override
            public TicketResponseVO getStatistics(BetOrderVO betOrderVO) {
                log.error("getStatistics:" + betOrderVO);
                return null;
            }
        };
    }
}

