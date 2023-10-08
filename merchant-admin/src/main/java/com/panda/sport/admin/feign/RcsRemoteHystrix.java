package com.panda.sport.admin.feign;
/**
 * @author Administrator
 * @date 2021/5/9
 * @TIME 12:02
 */

import com.panda.sport.merchant.common.dto.*;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *@ClassName RcsRemoteHystrix
 *@Description TODO
 *@Author Administrator
 *@Date 2021/5/9 12:02
 */
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

            @Override
            public Response<CreditConfigHttpQueryDto> getMerchantMatchCreditConfig(CreditConfigHttpQueryDto configDto) {
                return null;
            }

            @Override
            public Response<String> saveCreditLimitTemplate(CreditConfigHttpQueryDto configDto) {
                return null;
            }
        };
    }
}
