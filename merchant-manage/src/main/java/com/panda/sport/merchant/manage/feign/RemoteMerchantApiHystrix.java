package com.panda.sport.merchant.manage.feign;


import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteMerchantApiHystrix implements MerchantApiClient {

    @Override
    public Object kickoutMerchant(String merchantCode) {
        log.error("踢出商户失败,RPC接口异常");
        return null;
    }

    @Override
    public APIResponse<Object> findMerchantInfo(MerchantInfoVo merchantInfoVo) {
        log.error("查询商户信息失败,RPC接口异常");
        return null;
    }
    @Override
    public Object kickoutUserInternal(Long uid, String merchantCode) {
        log.error("kickoutUserInternal踢用户失败,RPC接口异常");
        return null;
    }

    @Override
    public Object clearMerchantActivityCache(String merchantCode) {
        log.error("clearMerchantActivityCache踢商户活动失败,RPC接口异常");
        return null;
    }

    @Override
    public Object kickoutMerchantUser(String merchantCode) {
        log.error("kickoutMerchantUser踢商户用户失败,RPC接口异常");
        return null;
    }

    @Override
    public void updateMerchantUserCache(String merchantCode) {
        log.error("updateMerchantUserCache error,RPC接口异常");
    }

    @Override
    public void clearTicketsOfMardigraTask(String merchantCode, Integer conditionId) {
        log.error("clearTicketsOfMardigraTask error,RPC接口异常");
    }

    @Override
    public Object kickoutMerchants(List<String> merchantCodes) {
        log.error("踢出下级商户失败,RPC接口异常");
        return null;
    }

    @Override
    public void updateMerchantChatSwitch(SystemSwitchVO systemSwitchVO) {
        log.error("updateMerchantChatSwitch error,RPC接口异常");
    }

    @Override
    public APIResponse<Object> updateVideoDomain(String newDomain, String oldDomain, String merchantCode) {
        log.error("updateVideoDomain error,RPC接口异常");
        return null;
    }

    @Override
    public APIResponse updateMerchantCache(MerchantConfig merchantConfig) {
        log.error("updateMerchantCache error,RPC接口异常");
        return null;
    }
}

