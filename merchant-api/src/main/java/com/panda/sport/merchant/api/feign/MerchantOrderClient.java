package com.panda.sport.merchant.api.feign;


import org.springframework.stereotype.Repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Repository
@FeignClient(value = "panda-report-task", fallback = RemoteHystrix.class)
public interface MerchantOrderClient {
    @GetMapping(value = "/reporttask/user/queryUserIdList")
    List<Map<String, Object>> queryUserIdList(@RequestParam(value = "merchantCode") String merchantCode);

}
