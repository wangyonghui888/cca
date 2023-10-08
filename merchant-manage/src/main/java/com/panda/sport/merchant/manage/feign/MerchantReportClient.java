package com.panda.sport.merchant.manage.feign;


import com.panda.sport.merchant.common.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author :  ives
 * @Description :  panda-merchant-report-api 微服务调用
 * @Date: 2022-02-10 14:25
 */
@FeignClient(value = "panda-merchant-report-api", fallbackFactory = RemoteHystrix.class)
public interface MerchantReportClient {

    /**
     * 财务中心/对账工具-开始对账
     * @param queryReqVO
     * @return CheckToolsQueryRespVO
     */
    @PostMapping(value = "/report/check/checkFinance")
    CheckToolsQueryRespVO checkFinance(@RequestBody CheckToolsQueryReqVO queryReqVO);

    /**
     * 财务中心/对账工具-修正对账
     * @param editReqVO
     * @return CheckToolsEditReqVO
     */
    @PostMapping(value = "/report/check/editFinance")
    CheckToolsEditRespVO editFinance(@RequestBody CheckToolsEditReqVO editReqVO);

    /**
     * 用户级别 财务中心/对账工具-开始对账
     * @param queryReqVO
     * @return CheckToolsQueryRespVO
     */
    @PostMapping(value = "/report/check/checkUserFinance")
    CheckToolsQueryRespVO checkUserFinance(@RequestBody CheckToolsQueryReqVO queryReqVO);

    /**
     * 用户级别 财务中心/对账工具-修正对账
     * @param editReqVO
     * @return CheckToolsEditReqVO
     */
    @PostMapping(value = "/report/check/editUserFinance")
    CheckToolsEditRespVO editUserFinance(@RequestBody CheckToolsEditReqVO editReqVO);


    @PostMapping(value = "/report/financeMonth/executeFinanceDay")
    Response executeFinanceDay(@RequestBody CheckToolsQueryReqVO queryReqVO);

    @PostMapping(value = "/report/merchant/updateMerchantName")
    int updateMerchantName(@RequestParam(value = "merchantCode")  String merchantCode, @RequestParam(value = "merchantName")  String merchantName);

}
