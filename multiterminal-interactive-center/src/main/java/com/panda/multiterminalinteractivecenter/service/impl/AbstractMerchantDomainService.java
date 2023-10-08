package com.panda.multiterminalinteractivecenter.service.impl;

import com.panda.multiterminalinteractivecenter.mapper.ThirdDomainMapper;
import com.panda.multiterminalinteractivecenter.mapper.TyDomainMapper;
import com.panda.multiterminalinteractivecenter.service.IDomainGroupService;
import com.panda.multiterminalinteractivecenter.service.MerchantDomainService;
import com.panda.multiterminalinteractivecenter.utils.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AbstractMerchantDomainService {

    public MerchantDomainService getDomainServiceBean(String tab){
        if(tab.equalsIgnoreCase("ty")){
            return (MerchantDomainService)SpringUtil.getBean("tyMerchantDomainServiceImpl");
        }
        return (MerchantDomainService)SpringUtil.getBean("thirdMerchantDomainServiceImpl");
    }

}
