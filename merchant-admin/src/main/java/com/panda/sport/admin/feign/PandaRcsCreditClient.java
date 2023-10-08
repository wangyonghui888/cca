package com.panda.sport.admin.feign;

import com.panda.sport.merchant.common.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "panda-rcs-credit", fallbackFactory = RcsRemoteHystrix.class)
public interface PandaRcsCreditClient {

    @PostMapping(value = "/credit/config/queryCreditLimitConfig")
    Response<CreditConfigDto> queryCreditLimitConfig(@RequestBody Request<CreditConfigDto> request);

    @PostMapping(value = "/credit/config/saveOrUpdateCreditLimitConfig")
    Response<Boolean> saveOrUpdateCreditLimitConfig(@RequestBody Request<CreditConfigSaveDto> request);

    @PostMapping(value = "/credit/merchantsCreditConfig/getUserSpecialCreditLimitConfig")
    Response<CreditConfigHttpQueryDto> getMerchantMatchCreditConfig(@RequestBody CreditConfigHttpQueryDto configDto);

    @PostMapping(value = "/credit/merchantsCreditConfig/save")
    Response<String> saveCreditLimitTemplate(@RequestBody CreditConfigHttpQueryDto configDto);
}
