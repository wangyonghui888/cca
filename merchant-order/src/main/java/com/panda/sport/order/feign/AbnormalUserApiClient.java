package com.panda.sport.order.feign;

import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "panda-rcs-trade", fallback = AbnormalUserHystrix.class)
public interface AbnormalUserApiClient {
    @PostMapping(value = "/api/trade/queryUserExceptionByOnline")
    Map<String, Object>  queryAbnormalUserList(@RequestBody AbnormalUserVo request);

}
