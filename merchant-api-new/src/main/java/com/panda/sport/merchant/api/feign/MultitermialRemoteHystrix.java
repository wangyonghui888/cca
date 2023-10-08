package com.panda.sport.merchant.api.feign;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MultitermialRemoteHystrix implements FallbackFactory<MultiterminalClient> {


    @Override
    public MultiterminalClient create(Throwable cause) {
        return new MultitermialFallBack() {
            @Override
            public List<?> getDomainByMerchantAndArea(Long merchantgroupid, String domaingroupcode) {
                log.error("getDomainByMerchantAndArea RPC调用失败!",cause);
                return null;
            }

            @Override
            public String getFrontDomainByTerminal(String terminal, String merchantCode) {
                log.error("getFrontDomainByTerminal RPC调用失败!",cause);
                return null;
            }
        };
    }
}
