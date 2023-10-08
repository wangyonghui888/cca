package com.panda.multiterminalinteractivecenter.feign;


import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteMerchantApiHystrix implements MerchantApiClient {

    @Override
    public Object kickOutUserMerchant(String merchantCode, String userIds) {
        log.error("kickoutUserInternal踢用户失败,RPC接口异常");
        return null;
    }

    @Override
    public Object updateMaintainCache(Long maintainTime) {
        log.error("updateMaintainCache 设置维护时间异常,RPC接口异常");
        return null;
    }

    @Override
    public Object kickoutMerchant(String merchantCode) {
        log.error("kickoutMerchant踢商户失败,RPC接口异常");
        return null;
    }

    @Override
    public Object kickoutMerchants(List<String> merchantCodes) {
        log.error("kickoutMerchants踢商户失败,RPC接口异常");
        return null;
    }

    @Override
    public void updateApiDomain(String oldDomain, String sinature, String newDomain) {
        log.error("updateApiDomain更新域名失败,RPC接口异常");
    }

    @Override
    public void updateNewDomain(String domainName, Integer domainType, String newDomain) {
        log.error("updateNewDomain更新域名失败,RPC接口异常");
    }

    @Override
    public void replace(JSONObject jsonObject) {
        log.error("replace替换域名失败,RPC接口异常");
    }

    @Override
    public Long queryUidByUserName(String userName) {
        log.error("queryUidByUserName ERROR,RPC接口异常");
        return null;
    }

    @Override
    public APIResponse changeMerchantDomain(String merchantDomainGroup, String merchantDomain) {
        log.error("changeMerchantDomain ERROR,RPC接口异常");
        return null;
    }

    @Override
    public List<SystemConfig> querySystemConfig(SystemConfig po) {
        log.error("querySystemConfig ERROR,RPC接口异常");
        return null;
    }

    @Override
    public int createSystemConfig(SystemConfig systemConfig) {
        log.error("createSystemConfig ERROR,RPC接口异常");
        return 0;
    }

    @Override
    public int updateSystemConfig(SystemConfig systemConfig) {
        log.error("updateSystemConfig ERROR,RPC接口异常");
        return 0;
    }

}

