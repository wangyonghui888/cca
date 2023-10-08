package com.panda.sport.merchant.api.service;


import com.panda.sport.merchant.common.dto.CreditConfigApiDto;
import com.panda.sport.merchant.common.dto.FrontDomainMerchantDTO;
import com.panda.sport.merchant.common.dto.VideoDomainMerchantDTO;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MerchantService {


    APIResponse getAPIDomain(HttpServletRequest request, String merchantCode, Long timestamp, String signature);


    APIResponse updateApiDomain(String oldDomain, String newDomain, String signature);


    APIResponse<Object> updateNewDomain(String oldDomain, Integer domainType, String newDomain);

    void replaceDomain(String source, String target, Integer domainType);


    APIResponse<Object> updateVideoDomain(String newDomain, String oldDomain, String merchantCode);


    List<SystemConfig> querySystemConfig(SystemConfig po);

    int createSystemConfig(SystemConfig po);

    int updateSystemConfig(SystemConfig po);

    APIResponse queryMerchantDomain(String oldDomain);

    APIResponse<Object> updateNewDomainAndChangeSystemConfig(String oldDomain, Integer domainType, String newDomain);

    void replaceDomainByMerchant(FrontDomainMerchantDTO domainDTO);

    APIResponse queryMerchantVideoDomain(String oldDomain);

    APIResponse<Object> updateNewVideoDomainAndChangeSystemConfig(String oldDomain, Integer domainType, String newDomain);

    void replaceVideoDomainByMerchant(VideoDomainMerchantDTO domainDTO);
}
