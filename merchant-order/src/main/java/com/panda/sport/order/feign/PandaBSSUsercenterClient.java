package com.panda.sport.order.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(value = "panda-bss-usercenter", fallback = RemoteHystrix.class)
public interface PandaBSSUsercenterClient {

    @GetMapping(value = "/report/userAccountClearDataTask")
    void userAccountClearDataTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "num") Integer num);


}
