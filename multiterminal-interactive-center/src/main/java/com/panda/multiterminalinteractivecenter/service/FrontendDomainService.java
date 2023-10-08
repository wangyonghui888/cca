package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroup;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroupDomainPO;

public interface FrontendDomainService {

    APIResponse<Object> queryMerchantDomain(FrontendMerchantDomain merchantDomain);

    APIResponse<Object> changeMerchantDomain(DomainChangeDTO domainChangeDto);

    APIResponse<Object> delMerchantDomainCache();

    APIResponse createFrontendMerchantGroup(FrontendMerchantGroup merchantGroup);

    APIResponse updateFrontendMerchantGroup(FrontendMerchantGroup merchantGroup);

    APIResponse queryFrontendMerchantGroup(FrontendMerchantGroupDomainPO groupDomainPo);

    APIResponse createFrontendDomain(FrontendMerchantDomain merchantDomain);

    APIResponse delFrontendDomain(FrontendMerchantDomain merchantDomain);
}
