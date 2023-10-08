package com.panda.sport.merchant.manage.feign;

import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO;
import com.panda.sport.merchant.common.po.bss.VideoMerchantGroupDomainPO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.manage.entity.vo.MerchantGroupDomainVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteMultiterminalHystrix implements MultiterminalClient{
    @Override
    public APIResponse getMerchantGroupDomain(MerchantGroupDomainVO req) {
        log.error("getMerchantGroupDomain error,RPC接口异常");
        return null;
    }

    @Override
    public APIResponse queryAnimation() {
        log.error("queryAnimation error,RPC接口异常");
        return null;
    }

    @Override
    public APIResponse<Object> queryFrontendMerchantDomain(FrontendMerchantGroupDomainPO frontendMerchantGroup) {
        log.error("queryFrontendMerchantDomain error,RPC接口异常");
        return null;
    }

    @Override
    public APIResponse<Object> queryVideoMerchantDomain(VideoMerchantGroupDomainPO frontendMerchantGroup) {
        log.error("queryVideoMerchantDomain error,RPC接口异常");
        return null;
    }
}
