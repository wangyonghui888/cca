package com.panda.sport.bc.feign;


import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "panda-merchant-report-api", fallback = RemoteHystrix.class)
public interface MerchantReportClient {


    /**
     * BC赛事统计列表
     * @author: Butr
     * @date: 2020/9/08
     */
    @PostMapping(value = "/report/match/queryBCMatchStatisticList")
    Response queryBCMatchStatisticList(@RequestBody SportVO vo);

    /**
     * BC赛事统计列表汇总
     * @author: Butr
     * @date: 2020/9/08
     */
    @PostMapping(value = "/report/match/getBCMatchStatistics")
    Response getBCMatchStatistics(@RequestBody SportVO vo);


}
