package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.entity.TyDomain;

import java.util.List;

public interface DomainService {

    void checkDomainValid();

    void  checkMerchantDomainToggle();

    void checkDomainThreshold();

    void checkMerchantUseByCp();

    Boolean switchByParamV2(TyDomain domain, Integer domainType, Long domainGroupId);

    Boolean switchByParamV2(TyDomain domain, Integer domainType, Long domainGroupId,Long targetId,int changeType);

}
