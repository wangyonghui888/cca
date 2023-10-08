package com.panda.sport.admin.feign;


import com.panda.sport.admin.vo.RateVo;
import com.panda.sport.merchant.common.vo.OrderOperationVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;

@FeignClient(value = "panda-bss-backend-order", fallbackFactory = BssRemoteHystrix.class)
public interface BssBackendClient {

    @PostMapping(value = "/v1/betOrder/orderOperation")
    Object orderOperation(@RequestBody @Validated OrderOperationVO orderOperation);

    @PostMapping(value = "/v1/rate/queryRateList")
    Object queryRateList(@RequestBody RateVo rateVo);

    @PostMapping("/v1/rate/queryRateLogList")
    Object queryRateLogList(@RequestBody RateVo rateVo);
}
