package com.panda.sport.order.feign;

import com.panda.sport.merchant.common.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "panda-rcs-order-limit", fallbackFactory = RcsUserLimitHystrix.class)
public interface PandaRcsUserLimitClient {

    @PostMapping(value = "/limit/api/userSeriesAvailableLimit")
    Response<UserSeriesAvailableLimitResDto> userSeriesAvailableLimit(@RequestBody Request<UserSeriesAvailableLimitReqDto> request);

    @PostMapping(value = "/limit/api/userSingleAvailableLimit")
    Response<UserSingleAvailableLimitResDto> userSingleAvailableLimit(@RequestBody Request<UserSingleAvailableLimitReqDto> request);

}
