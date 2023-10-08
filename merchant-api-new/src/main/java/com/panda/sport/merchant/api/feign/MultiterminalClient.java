package com.panda.sport.merchant.api.feign;


import com.panda.sport.merchant.common.vo.api.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
@FeignClient(value = "multiterminal-interactive-center", fallbackFactory = MultitermialRemoteHystrix.class)
public interface MultiterminalClient {

    @GetMapping(value = "/merchant/domain/getDomainByMerchantAndArea")
    List<?> getDomainByMerchantAndArea(@RequestParam(value = "merchantGroupId") Long merchantGroupId, @RequestParam(value = "domainGroupCode") String domainGroupCode);

    @GetMapping(value = "/merchant/domain/getFrontDomainByTerminal")
    String getFrontDomainByTerminal(@RequestParam(value = "terminal") String terminal, @RequestParam(value = "merchantCode") String merchantCode);

}
