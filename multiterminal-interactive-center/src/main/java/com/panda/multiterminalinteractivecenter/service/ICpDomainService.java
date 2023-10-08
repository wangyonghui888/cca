package com.panda.multiterminalinteractivecenter.service;

import org.springframework.stereotype.Repository;

@Repository
public interface ICpDomainService {

    void sendMsg(String merchantCode, Integer domainType, String url) ;

    void sendMsgV2(String merchantCode,Integer domainType,String url,int isVip,String ipArea);
}
