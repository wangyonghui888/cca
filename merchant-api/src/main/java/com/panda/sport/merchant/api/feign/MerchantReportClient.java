package com.panda.sport.merchant.api.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Repository
@FeignClient(value = "panda-merchant-report-api", fallback = RemoteHystrix.class)
public interface MerchantReportClient {

    @GetMapping(value = "/report/merchant/getMerchantReportApi")
    List<?> getMerchantReportApi(@RequestParam(value = "startTime") Integer startTime, @RequestParam(value = "endTime") Integer endTime,
                                 @RequestParam(value = "dateType") String dateType);

    @GetMapping(value = "/report/merchant/getBetReport")
    Map<?,?> getBetReport(@RequestParam(value = "reportDate") Integer reportDate, @RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping(value = "/report/activity/queryOlympicBetEvery")
    List<Map<String, Object>> queryOlympicBetEvery(@RequestParam(value = "days") int days, @RequestParam(value = "betAmount") Double bet, @RequestParam(value = "startDateL") Long startDateL, @RequestParam(value = "endDateL") Long endDateL);

    @GetMapping(value = "/report/activity/queryOlympicMardiGras")
    List<Map<String, Object>> queryOlympicMardiGras(@RequestParam(value = "betAmount") Double betAmount, @RequestParam(value = "startDateL") Long startDateL,
                                                    @RequestParam(value = "endDateL") Long endDateL);
}
