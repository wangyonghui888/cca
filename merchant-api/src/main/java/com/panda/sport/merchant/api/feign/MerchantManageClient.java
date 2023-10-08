package com.panda.sport.merchant.api.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Repository
@FeignClient(value = "merchant-manage", fallback = RemoteHystrix.class)
public interface MerchantManageClient {

    @GetMapping("/checkSingleDomain")
    Object checkSingleDomain(@RequestParam(value = "domain") String domain);
}
