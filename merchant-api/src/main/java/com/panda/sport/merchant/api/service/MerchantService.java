package com.panda.sport.merchant.api.service;


import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.dto.CreditConfigApiDto;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MerchantService {

    APIResponse createAgent(HttpServletRequest request, String agentId, String agentName, String merchantCode, Long timestamp, String signature) throws Exception;

    APIResponse changeUserAgent(HttpServletRequest request, String agentId, String userName, String merchantCode, Long timestamp, String signature) throws Exception;

    APIResponse queryCreditLimitConfig(HttpServletRequest request, String merchantCode, String creditId, Long timestamp, String signature, String globalId);

    APIResponse saveOrUpdateCreditLimitConfig(HttpServletRequest request, @RequestBody CreditConfigApiDto reqData, String globalId);

    APIResponse kickoutMerchant(String merchantCode);

    APIResponse getAPIDomain(HttpServletRequest request, String merchantCode, Long timestamp, String signature);

    APIResponse getAllApiDomain(String merchantCode);

    APIResponse updateApiDomain(String oldDomain, String newDomain, String signature);

    APIResponse<Object> changeDomain(String oldDomain, String merchantCode, String signature);

    APIResponse clearMerchantActivityCache(String merchantCode);

    APIResponse<Object> updateNewDomain(String oldDomain, Integer domainType, String newDomain);

    void replaceDomain(String source, String target, Integer domainType);

    APIResponse getH5PcDomain();

    void updateMerchantChatSwitch(SystemSwitchVO systemSwitchVO);

    APIResponse<Object> updateVideoDomain(String newDomain, String oldDomain, String merchantCode);

    void clearStressTestData(String merchantCode, Integer num);

    APIResponse kickoutMerchants(List<String> merchantCodes);

    APIResponse updateMerchantCache(MerchantConfig merchantConfig);

    APIResponse changeMerchantDomain(String merchantDomainGroup, String merchantDomain);

    List<SystemConfig> querySystemConfig(SystemConfig po);

    int createSystemConfig(SystemConfig po);

    int updateSystemConfig(SystemConfig po);
}
