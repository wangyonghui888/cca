package com.panda.multiterminalinteractivecenter.feign;


import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.sport.merchant.common.dto.FrontDomainMerchantDTO;
import com.panda.sport.merchant.common.dto.VideoDomainMerchantDTO;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
@FeignClient(value = "merchant-api-new", fallback = MerchantApiNewHystrix.class)
public interface MerchantApiNewClient {
    @GetMapping("/api/user/refresh/front/domain")
    void refreshFrontDomain();


    @PostMapping(value = "/api/merchant/queryMerchantDomain")
    APIResponse queryMerchantDomain(@RequestBody JSONObject param);

    @PostMapping(value = "/api/merchant/queryMerchantVideoDomain")
    APIResponse queryMerchantVideoDomain(@RequestBody JSONObject param);


    @GetMapping(value = "/api/merchant/updateNewDomain")
    void updateNewDomain(@RequestParam(value = "oldDomain") String domainName,
                         @RequestParam(value = "domainType",required = false) Integer domainType,
                         @RequestParam(value = "newDomain") String newDomain);

    @GetMapping(value = "/api/merchant/updateNewDomainAndChangeSystemConfig")
    void updateNewDomainAndChangeSystemConfig(@RequestParam(value = "oldDomain") String domainName,
                         @RequestParam(value = "domainType",required = false) Integer domainType,
                         @RequestParam(value = "newDomain") String newDomain);


    @PostMapping(value = "/api/merchant/querySystemConfig")
    List<SystemConfig> querySystemConfig(@RequestBody SystemConfig po);

    @PostMapping(value = "/api/merchant/createSystemConfig")
    int createSystemConfig(@RequestBody SystemConfig systemConfig);

    @PostMapping(value = "/api/merchant/updateSystemConfig")
    int updateSystemConfig(@RequestBody SystemConfig systemConfig);

    @PostMapping(value = "/api/merchant/updateSystemConfigByDomain")
    int updateSystemConfigByDomain(@RequestBody SystemConfig systemConfig);

    @PostMapping(value = "/api/merchant/replaceDomainByMerchant")
    void replaceDomainByMerchant(@RequestBody FrontDomainMerchantDTO domainDTO);


    /**视频域名切换*/
    @GetMapping(value = "/api/merchant/updateNewVideoDomainAndChangeSystemConfig")
    void updateNewVideoDomainAndChangeSystemConfig(@RequestParam(value = "oldDomain") String domainName,
                                              @RequestParam(value = "domainType",required = false) Integer domainType,
                                              @RequestParam(value = "newDomain") String newDomain);

    @PostMapping(value = "/api/merchant/replaceVideoDomainByMerchant")
    void replaceVideoDomainByMerchant(@RequestBody VideoDomainMerchantDTO domainDTO);
}
