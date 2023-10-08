package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.dto.FrontDomainDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroup;

import javax.servlet.http.HttpServletRequest;

public interface FrontendDomainV2Service {

    APIResponse<?> queryMerchantDomain(FrontendMerchantDomain merchantDomain);

    APIResponse<Object> changeMerchantDomain(DomainChangeDTO domainChangeDto);

    APIResponse<Object> delMerchantDomainCache(Long id);

    APIResponse<Object> createFrontendMerchantGroup(FrontendMerchantGroup merchantGroup, HttpServletRequest request);

    APIResponse<Object> updateFrontendMerchantGroup(FrontendMerchantGroup merchantGroup, HttpServletRequest request);

    APIResponse<Object> createFrontendDomain(FrontendMerchantDomain merchantDomain, HttpServletRequest request);

    APIResponse<Object> delFrontendDomain(FrontendMerchantDomain merchantDomain, HttpServletRequest request);

    APIResponse<Object> queryFrontendMerchantDomain(com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO frontendMerchantGroup);

    APIResponse<Object> replaceDomain(FrontDomainDTO merchantDomain);

    APIResponse<Object> clearCache();

    APIResponse<Object> queryFrontendMerchantGroup();


    String getFrontDomainByTerminal(String terminal, String merchantCode);

    APIResponse<Object> queryMerchantList(com.panda.sport.merchant.common.po.bss.FrontendMerchantGroup frontendMerchantGroup);

}
