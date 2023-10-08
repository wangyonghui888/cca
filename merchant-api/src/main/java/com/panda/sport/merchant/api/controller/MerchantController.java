package com.panda.sport.merchant.api.controller;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.api.aop.RedisAPILimit;
import com.panda.sport.merchant.api.service.MerchantService;
import com.panda.sport.merchant.common.dto.CreditConfigApiDto;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import com.panda.sport.merchant.common.utils.UUIDUtils;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
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


    @PostMapping("/createAgent")
    public APIResponse createAgent(HttpServletRequest request, @RequestParam(value = "creditId") String agentId,
                                   @RequestParam(value = "agentName") String agentName,
                                   @RequestParam(value = "merchantCode") String merchantCode,
                                   @RequestParam(value = "timestamp") Long timestamp,
                                   @RequestParam(value = "signature") String signature) {
        log.info("api/merchant/createAgent,merchantCode:" + merchantCode + ",agentId:" + agentId + ",agentName" + agentName +
                ",merchantCode" + merchantCode + ",agentId" + agentId + ",timestamp" + timestamp + ",signature" + signature);
        if (StringUtils.isAnyEmpty(agentId, merchantCode, agentName, signature) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.createAgent(request, agentId, agentName, merchantCode, timestamp, signature);
        } catch (Exception e) {
            log.error("MerchantController.createAgent,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/changeUserAgent")
    public APIResponse changeUserAgent(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                       @RequestParam(value = "merchantCode") String merchantCode,
                                       @RequestParam(value = "agentId") String agentId,
                                       @RequestParam(value = "timestamp") Long timestamp,
                                       @RequestParam(value = "signature") String signature) {
        log.info("api/merchant/createAgent,merchantCode:" + merchantCode + ",agentId:" + agentId + ",userName" + userName +
                ",timestamp" + timestamp + ",signature" + signature);
        if (StringUtils.isAnyEmpty(agentId, merchantCode, userName, signature) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.changeUserAgent(request, agentId, userName, merchantCode, timestamp, signature);
        } catch (Exception e) {
            log.error("MerchantController.createAgent,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @PostMapping("/queryCreditLimitConfig")
    public APIResponse queryCreditLimitConfig(HttpServletRequest request, @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                              @RequestParam(value = "timestamp") Long timestamp,
                                              @RequestParam(value = "creditId", required = false) String creditId,
                                              @RequestParam(value = "signature") String signature) {
        String globalId = UUIDUtils.createId();
        log.info("/api/merchant/queryCreditLimitConfig  merchantCode:" + merchantCode + ",creditId:" + creditId + ",timestamp:" + timestamp + ",signature:" + signature + ",globalId:" + globalId);
        if (StringUtils.isAnyEmpty(signature) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.queryCreditLimitConfig(request, merchantCode, creditId, timestamp, signature, globalId);
        } catch (Exception e) {
            log.error("MerchantController.queryCreditLimitConfig,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/saveOrUpdateCreditLimitConfig")
    public APIResponse saveOrUpdateCreditLimitConfig(HttpServletRequest request, @RequestBody CreditConfigApiDto reqData) {
        String globalId = UUIDUtils.createId();
        log.info("api/merchant/saveOrUpdateCreditLimitConfig  reqData:" + reqData.toString() + ",globalId:" + globalId);
        if (StringUtils.isAnyEmpty(reqData.getSignature()) || reqData.getTimestamp() == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.saveOrUpdateCreditLimitConfig(request, reqData, globalId);
        } catch (Exception e) {
            log.error("MerchantController.saveOrUpdateCreditLimitConfig,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/kickoutMerchant")
    public APIResponse kickoutMerchant(HttpServletRequest request, @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("/api/merchant/kickoutMerchant  merchantCode:" + merchantCode);
        return merchantService.kickoutMerchant(merchantCode);
    }

    @GetMapping("/kickoutMerchants")
    public APIResponse kickoutMerchants(HttpServletRequest request, @RequestParam(value = "merchantCodes", required = false) List<String> merchantCodes) {
        log.info("/api/merchant/kickoutMerchants  merchantCode.size:" + (CollectionUtils.isEmpty(merchantCodes)?0:merchantCodes.size()));
        return merchantService.kickoutMerchants(merchantCodes);
    }


    @GetMapping("/clearMerchantActivityCache")
    public APIResponse clearMerchantActivityCache(HttpServletRequest request, @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("/api/merchant/clearMerchantActivityCache  merchantCode:" + merchantCode);
        return merchantService.clearMerchantActivityCache(merchantCode);
    }

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

    @RedisAPILimit(apiKey = "getAllApiDomain", limit = 20, sec = 60)
    @GetMapping("/getAllApiDomain")
    public APIResponse<Object> getAllApiDomain(@RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("api/merchant/getAllApiDomain:merchantCode=" + merchantCode);
        return merchantService.getAllApiDomain(merchantCode);
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
                                               @RequestParam(value = "domainType") Integer domainType,
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

    @PostMapping("/replaceDomain")
    public void replaceDomain(@RequestBody JSONObject param){
        String source = param.getString("source");
        String target = param.getString("target");
        Integer domainType = param.getInteger("domainType");
        log.info("api/merchant/replaceDomain,source={},target={},domainType={}",
                source, target, domainType);
        if (StringUtils.isBlank(source)||StringUtils.isBlank(target)||null == domainType) {
            log.error(ApiResponseEnum.PARAMETER_INVALID.getLabel());
        }
        merchantService.replaceDomain(source,target,domainType);
    }

    @RedisAPILimit(apiKey = "changeDomain", limit = 20, sec = 60)
    @GetMapping("/changeDomain")
    public APIResponse<Object> changeDomain(@RequestParam(value = "oldDomain") String oldDomain, @RequestParam(value = "merchantCode") String merchantCode,
                                            @RequestParam(value = "signature") String signature) {
        log.info("api/merchant/changeDomain,oldDomain=" + oldDomain + ",merchantCode=" + merchantCode);
        if (StringUtils.isAnyEmpty(oldDomain, merchantCode, signature)) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return merchantService.changeDomain(oldDomain, merchantCode, signature);
        } catch (Exception e) {
            log.error("MerchantController.changeDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @GetMapping("/getH5PcDomain")
    public APIResponse getH5PcDomain() {
        log.info("api/merchant/getH5PcDomain");
        try {
            return merchantService.getH5PcDomain();
        } catch (Exception e) {
            log.error("MerchantController.getH5PcDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/updateMerchantChatSwitch")
    public void updateMerchantChatSwitch(@RequestBody SystemSwitchVO systemSwitchVO){
        merchantService.updateMerchantChatSwitch(systemSwitchVO);
    }

    @GetMapping("/updateVideoDomain")
    public APIResponse<Object> updateVideoDomain(@RequestParam(value = "newDomain") String newDomain, @RequestParam(value = "oldDomain") String oldDomain,
                                                 @RequestParam(value = "merchantCode") String merchantCode){
        log.info("api/merchant/updateVideoDomain, newDomain={}, oldDomain={}, merchantCode={}", newDomain, oldDomain, merchantCode);
        try {
            return merchantService.updateVideoDomain(newDomain, oldDomain, merchantCode);
        }catch (Exception e){
            log.error("MerchantController.updateVideoDomain,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/clearStressTestData")
    public void clearStressTestData(@RequestParam(value = "merchantCodeList") List<String> merchantCodeList, @RequestParam(value = "num") Integer num){
        for (String merchantCode : merchantCodeList){
            merchantService.clearStressTestData(merchantCode, num);
        }
    }

    @PostMapping("/updateMerchantCache")
    public APIResponse updateMerchantCache(@RequestBody MerchantConfig merchantConfig){
        return merchantService.updateMerchantCache(merchantConfig);
    }

    @GetMapping("/changeMerchantDomain")
    public APIResponse changeMerchantDomain(@RequestParam("merchantDomainGroup") String merchantDomainGroup, @RequestParam("merchantDomain") String merchantDomain){
        log.info("api/merchant/changeMerchantDomain");
        try {
            merchantService.changeMerchantDomain(merchantDomainGroup, merchantDomain);
        }catch (Exception e){
            log.error("merchantController.changeMerchantDomain,exception:",e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "/querySystemConfig")
    List<SystemConfig> querySystemConfig(@RequestBody SystemConfig po){
        log.info("api/merchant/querySystemConfig");
        try {
            return merchantService.querySystemConfig(po);
        }catch (Exception e){
            log.error("merchantController.changeMerchantDomain,exception:",e);
            return null;
        }
    }

    @PostMapping(value = "/createSystemConfig")
    int createSystemConfig(@RequestBody SystemConfig po){
        log.info("api/merchant/createSystemConfig");
        try {
            return merchantService.createSystemConfig(po);
        }catch (Exception e){
            log.error("merchantController.changeMerchantDomain,exception:",e);
            return 0;
        }
    }

    @PostMapping(value = "/updateSystemConfig")
    int updateSystemConfig(@RequestBody SystemConfig po){
        log.info("api/merchant/updateSystemConfig");
        try {
            return merchantService.updateSystemConfig(po);
        }catch (Exception e){
            log.error("merchantController.changeMerchantDomain,exception:",e);
            return 0;
        }
    }
}