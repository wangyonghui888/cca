package com.panda.sport.admin.feign;

import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "panda-rcs-order-statistical", fallback = AbnormalOutApiHystrix.class)
public interface AbnormalOutApiClient {
    @PostMapping(value = "/api/queryUserExceptions")
    Map<String, Object>  queryAbnormalList(@RequestBody AbnormalVo request);

}
