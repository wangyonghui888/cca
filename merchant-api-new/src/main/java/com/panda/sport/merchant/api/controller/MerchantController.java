package com.panda.sport.merchant.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.api.aop.RedisAPILimit;

import com.panda.sport.merchant.api.service.MerchantService;
import com.panda.sport.merchant.api.service.UserService;
import com.panda.sport.merchant.common.dto.FrontDomainMerchantDTO;
import com.panda.sport.merchant.common.dto.VideoDomainMerchantDTO;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/merchant")
@Slf4j
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;


    @PostMapping("/getAPIDomain")
    public APIResponse getAPIDomain(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                    @RequestParam(value = "timestamp") Long timestamp,
                                    @RequestParam(value = "signature") String signature) {
        log.info("api/merchant/getAPIDomain  merchantCode:" + merchantCode + ",timestamp:" + timestamp + ",signature=" + signature);
        if (StringUtils.isAnyEmpty(merchantCode, signature) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.getAPIDomain(request, merchantCode, timestamp, signature);
        } catch (Exception e) {
            log.error("MerchantController.getAPIDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @RedisAPILimit(apiKey = "updateApiDomain", limit = 20, sec = 60)
    @GetMapping("/updateApiDomain")
    public APIResponse<Object> updateApiDomain(@RequestParam(value = "oldDomain") String oldDomain, @RequestParam(value = "newDomain") String newDomain,
                                               @RequestParam(value = "signature") String signature) {
        log.info("api/merchant/updateApiDomain,oldDomain=" + oldDomain + ",newDomain=" + newDomain);
        if (StringUtils.isAnyEmpty(oldDomain, newDomain, signature)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.updateApiDomain(oldDomain, newDomain, signature);
        } catch (Exception e) {
            log.error("MerchantController.updateApiDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/updateNewDomain")
    public APIResponse<Object> updateNewDomain(@RequestParam(value = "oldDomain") String oldDomain,
                                               @RequestParam(value = "domainType",required = false) Integer domainType,
                                               @RequestParam(value = "newDomain") String newDomain) {
        log.info("api/merchant/updateNewDomain,oldDomain={},domainType={},newDomain={}", oldDomain, domainType, newDomain);
        if (StringUtils.isAnyEmpty(oldDomain, newDomain)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            merchantService.updateNewDomain(oldDomain, domainType, newDomain);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("MerchantController.updateNewDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/updateNewDomainAndChangeSystemConfig")
    public APIResponse<Object> updateNewDomainAndChangeSystemConfig(@RequestParam(value = "oldDomain") String oldDomain,
                                               @RequestParam(value = "domainType",required = false) Integer domainType,
                                               @RequestParam(value = "newDomain") String newDomain) {
        log.info("api/merchant/updateNewDomainAndChangeSystemConfig,oldDomain={},domainType={},newDomain={}", oldDomain, domainType, newDomain);
        if (StringUtils.isAnyEmpty(oldDomain, newDomain)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            merchantService.updateNewDomainAndChangeSystemConfig(oldDomain, domainType, newDomain);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("MerchantController.updateNewDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/updateNewVideoDomainAndChangeSystemConfig")
    public APIResponse<Object> updateNewVideoDomainAndChangeSystemConfig(@RequestParam(value = "oldDomain") String oldDomain,
                                                                    @RequestParam(value = "domainType",required = false) Integer domainType,
                                                                    @RequestParam(value = "newDomain") String newDomain) {
        log.info("api/merchant/updateNewVideoDomainAndChangeSystemConfig,oldDomain={},domainType={},newDomain={}", oldDomain, domainType, newDomain);
        if (StringUtils.isAnyEmpty(oldDomain, newDomain)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            merchantService.updateNewVideoDomainAndChangeSystemConfig(oldDomain, domainType, newDomain);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("MerchantController.updateNewVideoDomainAndChangeSystemConfig,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/queryMerchantDomain")
    public APIResponse queryMerchantDomain(@RequestBody JSONObject param) {
        String oldDomain = param.getString("oldDomain");
        log.info("api/merchant/queryMerchantDomain,oldDomain={}",oldDomain);
        if (StringUtils.isBlank(oldDomain) ) {
           return APIResponse.returnFail("旧域名不可为空！");
        }
        return merchantService.queryMerchantDomain(oldDomain);
    }

    @PostMapping("/queryMerchantVideoDomain")
    public APIResponse queryMerchantVideoDomain(@RequestBody JSONObject param) {
        String oldDomain = param.getString("oldDomain");
        log.info("api/merchant/queryMerchantVideoDomain,oldDomain={}",oldDomain);
        if (StringUtils.isBlank(oldDomain) ) {
            return APIResponse.returnFail("旧域名不可为空！");
        }
        return merchantService.queryMerchantVideoDomain(oldDomain);
    }

    @PostMapping("/replaceDomain")
    public void replaceDomain(@RequestBody JSONObject param) {
        String source = param.getString("source");
        String target = param.getString("target");
        Integer domainType = param.getInteger("domainType");
        log.info("api/merchant/replaceDomain,source={},target={},domainType={}",
                source, target, domainType);
        if (StringUtils.isBlank(source) || StringUtils.isBlank(target) || null == domainType) {
            log.error(ApiResponseEnum.PARAMETER_INVALID.getLabel());
        }
        merchantService.replaceDomain(source, target, domainType);
    }



    @GetMapping("/updateVideoDomain")
    public APIResponse<Object> updateVideoDomain(@RequestParam(value = "newDomain") String newDomain, @RequestParam(value = "oldDomain") String oldDomain,
                                                 @RequestParam(value = "merchantCode") String merchantCode) {
        log.info("api/merchant/updateVideoDomain, newDomain={}, oldDomain={}, merchantCode={}", newDomain, oldDomain, merchantCode);
        try {
            return merchantService.updateVideoDomain(newDomain, oldDomain, merchantCode);
        } catch (Exception e) {
            log.error("MerchantController.updateVideoDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @PostMapping(value = "/querySystemConfig")
    List<SystemConfig> querySystemConfig(@RequestBody SystemConfig po) {
        log.info("api/merchant/querySystemConfig");
        try {
            return merchantService.querySystemConfig(po);
        } catch (Exception e) {
            log.error("merchantController.changeMerchantDomain,exception:", e);
            return null;
        }
    }

    @PostMapping(value = "/createSystemConfig")
    int createSystemConfig(@RequestBody SystemConfig po) {
        log.info("api/merchant/createSystemConfig");
        try {
            return merchantService.createSystemConfig(po);
        } catch (Exception e) {
            log.error("merchantController.changeMerchantDomain,exception:", e);
            return 0;
        }
    }

    @PostMapping(value = "/updateSystemConfig")
    int updateSystemConfig(@RequestBody SystemConfig po) {
        log.info("api/merchant/updateSystemConfig");
        try {
            return merchantService.updateSystemConfig(po);
        } catch (Exception e) {
            log.error("merchantController.changeMerchantDomain,exception:", e);
            return 0;
        }
    }

    @PostMapping(value = "/updateSystemConfigByDomain")
    int updateSystemConfigByDomain(@RequestBody SystemConfig po) {
        log.info("api/merchant/updateSystemConfigByDomain");
        try {
            int result =  merchantService.updateSystemConfig(po);
            userService.refreshFrontDomain();
            return result;
        } catch (Exception e) {
            log.error("merchantController.changeMerchantDomain,exception:", e);
            return 0;
        }
    }

    @PostMapping(value = "/replaceDomainByMerchant")
    void replaceDomainByMerchant(@RequestBody FrontDomainMerchantDTO domainDTO){
        log.info("api/merchant/replaceDomainByMerchant");
        try {
            merchantService.replaceDomainByMerchant(domainDTO);
            userService.refreshFrontDomain();
        } catch (Exception e) {
            log.error("merchantController.replaceDomainByMerchant,exception:", e);
        }
    }

    @PostMapping(value = "/replaceVideoDomainByMerchant")
    void replaceVideoDomainByMerchant(@RequestBody VideoDomainMerchantDTO domainDTO){
        log.info("api/merchant/replaceVideoDomainByMerchant");
        try {
            merchantService.replaceVideoDomainByMerchant(domainDTO);
        } catch (Exception e) {
            log.error("merchantController.replaceVideoDomainByMerchant,exception:", e);
        }
    }

}