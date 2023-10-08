package com.panda.multiterminalinteractivecenter.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroup;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroupDomainPO;
import com.panda.multiterminalinteractivecenter.enums.ResponseEnum;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.MerchantGroupMapper;
import com.panda.multiterminalinteractivecenter.service.FrontendDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RefreshScope
public class FrontendDomainServiceImpl implements FrontendDomainService {

    public static Cache<String, Object> merchantDomainMap = null;

    @PostConstruct
    private void init(){
        merchantDomainMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();
    }

    @Autowired
    private MerchantGroupMapper merchantGroupMapper;

    @Autowired
    private MerchantManageClient merchantManageClient;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Override
    public APIResponse<Object> queryMerchantDomain(FrontendMerchantDomain merchantDomain) {
        log.info("frontend/domain/queryMerchantDomain, merchantDomain:", merchantDomain);
        try {
            String key = "merchantDomain";
            APIResponse result = (APIResponse) merchantDomainMap.getIfPresent(key);
            if(null != result && null != result.getData()){
                log.info("本地缓存获取到数据直接返回" + result.getData());
                return result;
            }
            result = merchantManageClient.queryMerchantDomain(merchantDomain);
            if(null != result.getData()){
                log.info("更新本地缓存");
                merchantDomainMap.put(key, result);
            }
            return result;
        }catch (Exception e){
            log.error("queryMerchantDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse<Object> changeMerchantDomain(DomainChangeDTO domainChangeDto) {
        log.info("frontend/domain/changeMerchantDomain, domainChangeDto:", domainChangeDto);
        try {
            return merchantManageClient.changeMerchantDomain(domainChangeDto);
        }catch (Exception e){
            log.error("changeMerchantDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse<Object> delMerchantDomainCache() {
        try {
            merchantApiClient.kickoutMerchant(null);
        }catch (Exception e){
            log.error("changeMerchantDomain, exception:", e);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public APIResponse<Object> createFrontendMerchantGroup(FrontendMerchantGroup merchantGroup) {
        FrontendMerchantGroup group = new FrontendMerchantGroup();
        BeanUtils.copyProperties(merchantGroup, group);
        try {
            return APIResponse.returnSuccess(merchantManageClient.createFrontendMerchantGroup(group));
        }catch (Exception e){
            log.error("createFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse updateFrontendMerchantGroup(FrontendMerchantGroup merchantGroup) {
        FrontendMerchantGroup group = new FrontendMerchantGroup();
        BeanUtils.copyProperties(merchantGroup, group);
        try {
            return APIResponse.returnSuccess(merchantManageClient.updateFrontendMerchantGroup(group));
        }catch (Exception e){
            log.error("updateFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse queryFrontendMerchantGroup(FrontendMerchantGroupDomainPO groupDomainPo) {
        FrontendMerchantGroupDomainPO po = new FrontendMerchantGroupDomainPO();
        BeanUtils.copyProperties(groupDomainPo, po);
        try {
            return APIResponse.returnSuccess(merchantManageClient.queryFrontendMerchantGroup(po).getData());
        }catch (Exception e){
            log.error("queryFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse createFrontendDomain(FrontendMerchantDomain merchantDomain) {
        FrontendMerchantDomain frontendDomain = new FrontendMerchantDomain();
        BeanUtils.copyProperties(merchantDomain, frontendDomain);
        try {
            return APIResponse.returnSuccess(merchantManageClient.createFrontendDomain(frontendDomain));
        }catch (Exception e){
            log.error("createFrontendDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse delFrontendDomain(FrontendMerchantDomain merchantDomain) {
        FrontendMerchantDomain frontendDomain = new FrontendMerchantDomain();
        BeanUtils.copyProperties(merchantDomain, frontendDomain);
        try {
            return APIResponse.returnSuccess(merchantManageClient.delFrontendDomain(frontendDomain));
        }catch (Exception e){
            log.error("delFrontendDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }
}
