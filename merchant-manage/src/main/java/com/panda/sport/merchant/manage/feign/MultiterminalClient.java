package com.panda.sport.merchant.manage.feign;

import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO;
import com.panda.sport.merchant.common.po.bss.VideoMerchantGroupDomainPO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.manage.entity.vo.MerchantGroupDomainVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(value = "multiterminal-interactive-center", fallback = RemoteMultiterminalHystrix.class)
public interface MultiterminalClient {

    @PostMapping("/merchant/group/getMerchantGroupDomain")
    APIResponse getMerchantGroupDomain(@RequestBody MerchantGroupDomainVO req);

    @PostMapping("/merchant/group/queryAnimation")
    APIResponse queryAnimation();

    @PostMapping("/v2/frontend/domain/queryFrontendMerchantDomain")
    APIResponse<Object> queryFrontendMerchantDomain(@RequestBody FrontendMerchantGroupDomainPO frontendMerchantGroup);

    @PostMapping("/v1/video/domain/queryVideoMerchantDomain")
    APIResponse<Object> queryVideoMerchantDomain(VideoMerchantGroupDomainPO frontendMerchantGroup);
}
