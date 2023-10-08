package com.panda.sport.merchant.manage.feign;


import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.vo.*;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author :  ives
 * @Description :  微服务调用异常处理类
 * @Date: 2022-01-24 17:59
 */
@Slf4j
@Component
public class RemoteHystrix implements FallbackFactory<MerchantReportClient>,PandaReportTaskClient {

    @Override
    public MerchantReportClient create(Throwable cause) {

        return new PandBssFallBack() {

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
            public Response executeFinanceDay(CheckToolsQueryReqVO queryReqVO) {
                log.error("editFinance 调用 panda-merchant-report-api微服务" + cause);
                return null;
            }

            @Override
            public int updateMerchantName(String merchantCode, String merchantName) {
                log.error("updateMerchantName 调用 panda-merchant-report-api微服务" + cause);
                return 0;
            }
        };
    }

    @Override
    public Response<?> executeFinance(String startDate, String endDate) {
        log.error("executeFinance:" + startDate + "," + endDate);
        return Response.returnFail("请求超时了");
    }

    @Override
    public Response<?> executeUserBet(String startDate, String endDate) {
        log.error("executeUserBet:" + startDate + "," + endDate);
        return Response.returnFail("请求超时了");
    }

    @Override
    public Response<?> executeMatchBet(String startDate, String endDate) {
        log.error("executeMatchBet:" + startDate + "," + endDate);
        return Response.returnFail("请求超时了");
    }
}

