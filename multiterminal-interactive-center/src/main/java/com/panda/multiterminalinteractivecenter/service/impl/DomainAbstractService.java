package com.panda.multiterminalinteractivecenter.service.impl;

import com.panda.multiterminalinteractivecenter.vo.ThirdMerchantVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class DomainAbstractService {
    public abstract void sendMsg(String merchantCode,Integer domainType,String url);

}
