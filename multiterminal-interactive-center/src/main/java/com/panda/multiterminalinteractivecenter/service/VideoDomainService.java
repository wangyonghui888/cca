package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.dto.FrontDomainDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.VideoMerchantGroup;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroup;

import javax.servlet.http.HttpServletRequest;

public interface VideoDomainService {

    APIResponse<?> queryMerchantDomain(FrontendMerchantDomain merchantDomain);

    APIResponse<Object> changeMerchantDomain(DomainChangeDTO domainChangeDto);

    APIResponse<Object> delMerchantDomainCache(Long id);

    APIResponse<Object> createMerchantGroup(VideoMerchantGroup merchantGroup, HttpServletRequest request);

    APIResponse<Object> updateMerchantGroup(VideoMerchantGroup merchantGroup, HttpServletRequest request);

    APIResponse<Object> createVideoDomain(FrontendMerchantDomain merchantDomain, HttpServletRequest request);

    APIResponse<Object> delVideoDomain(FrontendMerchantDomain merchantDomain, HttpServletRequest request);

    APIResponse<Object> queryVideoMerchantDomain(com.panda.sport.merchant.common.po.bss.VideoMerchantGroupDomainPO frontendMerchantGroup);

    APIResponse<Object> replaceDomain(FrontDomainDTO merchantDomain);

    APIResponse<Object> clearCache();

    APIResponse<Object> queryMerchantGroup();

    APIResponse<Object> queryMerchantList(FrontendMerchantGroup frontendMerchantGroup);

}
