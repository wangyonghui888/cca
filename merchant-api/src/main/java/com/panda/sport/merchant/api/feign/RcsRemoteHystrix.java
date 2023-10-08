package com.panda.sport.merchant.api.feign;

import com.panda.sport.merchant.common.dto.CreditConfigDto;
import com.panda.sport.merchant.common.dto.CreditConfigSaveDto;
import com.panda.sport.merchant.common.dto.Request;
import com.panda.sport.merchant.common.dto.Response;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RcsRemoteHystrix implements FallbackFactory<PandaRcsCreditClient> {


    @Override
    public PandaRcsCreditClient create(Throwable cause) {
        log.error("RcsRemoteHystrix error",cause);
        return new PandRcsFallBack() {
            @Override
            public Response<CreditConfigDto> queryCreditLimitConfig(Request<CreditConfigDto> request) {
                return null;
            }

            @Override
            public Response<Boolean> saveOrUpdateCreditLimitConfig(Request<CreditConfigSaveDto> request) {
                return null;
            }
        };
    }
}
