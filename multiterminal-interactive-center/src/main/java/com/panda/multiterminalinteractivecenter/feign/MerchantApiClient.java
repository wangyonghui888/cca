package com.panda.multiterminalinteractivecenter.feign;


import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
@FeignClient(value = "merchant-api", fallback = RemoteMerchantApiHystrix.class)
public interface MerchantApiClient {


    @GetMapping("/api/user/kickOutUserMerchant")
    Object kickOutUserMerchant(@RequestParam(value = "merchantCode") String merchantCode,@RequestParam(value = "userIds") String userIds);

    @GetMapping("/api/user/updateMaintainCache")
    Object updateMaintainCache(@RequestParam(value = "maintainTime") Long maintainTime);

    @GetMapping(value = "/api/merchant/kickoutMerchant")
    Object kickoutMerchant(@RequestParam(value = "merchantCode") String merchantCode);
    @GetMapping(value = "/api/merchant/kickoutMerchants")
    Object kickoutMerchants(@RequestParam(value = "merchantCodes") List<String> merchantCodes);


    @GetMapping(value = "/api/merchant/updateApiDomain")
    void updateApiDomain(@RequestParam(value = "oldDomain") String  oldDomain,@RequestParam(value = "sinature") String sinature, @RequestParam(value = "newDomain") String newDomain);

    @GetMapping(value = "/api/merchant/updateNewDomain")
    void updateNewDomain(@RequestParam(value = "oldDomain") String domainName,
                         @RequestParam(value = "domainType") Integer domainType,
                         @RequestParam(value = "newDomain") String newDomain);

    @PostMapping(value = "/api/merchant/replaceDomain")
    void replace(@RequestBody JSONObject jsonObject);

    @GetMapping(value = "/api/user/queryUidByUserName")
    Long queryUidByUserName(@RequestParam(value = "userName") String userName);

    @GetMapping(value = "/api/merchant/changeMerchantDomain")
    APIResponse changeMerchantDomain(@RequestParam(value = "merchantDomainGroup") String merchantDomainGroup, @RequestParam("merchantDomain") String merchantDomain);

    @PostMapping(value = "/api/merchant/querySystemConfig")
    List<SystemConfig> querySystemConfig(SystemConfig po);

    @PostMapping(value = "/api/merchant/createSystemConfig")
    int createSystemConfig(SystemConfig systemConfig);

    @PostMapping(value = "/api/merchant/updateSystemConfig")
    int updateSystemConfig(SystemConfig systemConfig);
}
