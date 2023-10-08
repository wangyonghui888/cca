package com.panda.sport.merchant.api.feign;

import com.panda.sport.merchant.common.dto.CreditConfigDto;
import com.panda.sport.merchant.common.dto.CreditConfigSaveDto;
import com.panda.sport.merchant.common.dto.Request;
import com.panda.sport.merchant.common.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "panda-rcs-credit", fallbackFactory = RcsRemoteHystrix.class)
public interface PandaRcsCreditClient {

    @PostMapping(value = "/credit/config/queryCreditLimitConfig")
    Response<CreditConfigDto> queryCreditLimitConfig(@RequestBody Request<CreditConfigDto> request);

    @PostMapping(value = "/credit/config/saveOrUpdateCreditLimitConfig")
    Response<Boolean> saveOrUpdateCreditLimitConfig(@RequestBody Request<CreditConfigSaveDto> request);


}
