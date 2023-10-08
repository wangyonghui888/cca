package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.entity.WSResponse;


/**
 * <p>
 * oss域名表 服务类
 * </p>
 *
 * @author Baylee
 * @since 2021-05-28
 */
public interface IOssDomainService {


    void processWSMessage(WSResponse wsResponse);

    void processOssDomainWSMessage(WSResponse wsResponse);

    String switchOssDomain(Integer i, String domain, String selfTest, String code,Integer domainType, Long groupIdNew);

}
