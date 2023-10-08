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

    @GetMapping(value = "/report/user/queryUserSecondLevelList")
    List<String> queryUserSecondLevelList();

}
