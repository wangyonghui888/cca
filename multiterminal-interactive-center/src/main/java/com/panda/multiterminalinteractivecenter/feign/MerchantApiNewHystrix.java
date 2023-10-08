package com.panda.multiterminalinteractivecenter.feign;

import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.sport.merchant.common.dto.FrontDomainMerchantDTO;
import com.panda.sport.merchant.common.dto.VideoDomainMerchantDTO;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MerchantApiNewHystrix implements MerchantApiNewClient{
    @Override
    public void refreshFrontDomain() {
        log.error("refreshFrontDomain ERROR" );
    }

    @Override
    public APIResponse queryMerchantDomain(JSONObject param) {
        log.error("queryMerchantDomain ERROR" );
        return null;
    }

    @Override
    public APIResponse queryMerchantVideoDomain(JSONObject param) {
        log.error("queryMerchantVideoDomain ERROR" );
        return null;
    }

    @Override
    public void updateNewDomain(String domainName, Integer domainType, String newDomain) {
        log.error("updateNewDomain ERROR" );
    }

    @Override
    public void updateNewDomainAndChangeSystemConfig(String domainName, Integer domainType, String newDomain) {
        log.error("updateNewDomainAndChangeSystemConfig ERROR");
    }

    @Override
    public void updateNewVideoDomainAndChangeSystemConfig(String domainName, Integer domainType, String newDomain) {
        log.error("updateNewVideoDomainAndChangeSystemConfig ERROR");
    }

    @Override
    public List<SystemConfig> querySystemConfig(SystemConfig po) {
        log.error("querySystemConfig ERROR" );
        return null;
    }

    @Override
    public int createSystemConfig(SystemConfig systemConfig) {
        log.error("createSystemConfig ERROR" );
        return 0;
    }

    @Override
    public int updateSystemConfig(SystemConfig systemConfig) {
        log.error("updateSystemConfig ERROR" );
        return 0;
    }

    @Override
    public int updateSystemConfigByDomain(SystemConfig systemConfig) {
        log.error("updateSystemConfigByDomain ERROR" );
        return 0;
    }

    @Override
    public void replaceDomainByMerchant(FrontDomainMerchantDTO domainDTO) {
        log.error("replaceDomainByMerchant ERROR" );
    }

    @Override
    public void replaceVideoDomainByMerchant(VideoDomainMerchantDTO domainDTO) {
        log.error("replaceVideoDomainByMerchant ERROR" );
    }


}
