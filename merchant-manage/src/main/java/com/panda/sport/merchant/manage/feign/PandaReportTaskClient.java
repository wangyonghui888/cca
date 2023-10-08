package com.panda.sport.merchant.manage.feign;


import com.panda.sport.merchant.common.vo.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "panda-report-task", fallback = RemoteHystrix.class)
public interface PandaReportTaskClient {

    /**
     * 对账单数据同步接口
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/task/schedule/executeFinance")
    Response<?> executeFinance(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate);


    /**
     * 用户投注统计数据同步接口
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/task/schedule/execute")
    Response<?> executeUserBet(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate);

    /**
     * 赛事投注统计数据同步接口
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping(value = "/task/schedule/executeMatchBet")
    Response<?> executeMatchBet(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate);


}
