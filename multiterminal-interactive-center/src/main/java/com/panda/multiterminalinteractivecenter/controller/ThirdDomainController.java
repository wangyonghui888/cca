package com.panda.multiterminalinteractivecenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainReqDTO;
import com.panda.multiterminalinteractivecenter.enums.ResponseEnum;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.service.DomainService;
import com.panda.multiterminalinteractivecenter.service.IDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;



/**
 * 三方调用TY获取域名（新）
 */
@RestController
@RequestMapping("/multi/api")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdDomainController {


    private final IDomainService domainService;
    private final DomainService domainService2;

    @GetMapping("/v1/getDomainByMerchantGroup")
    public APIResponse getDomainByMerchantGroupV1(HttpServletRequest request,
                                                  @RequestParam(value = "account",required = false) String account,
                                                  @RequestParam(value = "code",required = false) String code) {
        log.info("第三方获取商户组 /getMerchantGroup param = {}", account);
        return domainService.getMerchantGroupInfoByThirdCode(code, account);
    }

    @GetMapping("/v2/getDomainByMerchantGroup")
    public APIResponse getDomainByMerchantGroupV2(HttpServletRequest request,
                                                  @SpringQueryMap DomainReqDTO domainReqDTO) {
        log.info("{}部门调用接口获取域名：param:【{}】",domainReqDTO.getTab(), JSONObject.toJSONString(domainReqDTO));

        if(StringUtils.isBlank(domainReqDTO.getTab()) || !"cp,CP,dj,DJ".contains(domainReqDTO.getTab())){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            if(domainReqDTO.getTab().equalsIgnoreCase("cp")){
                if(domainReqDTO.getMerchantAccount() == null){
                    return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
                }
                // CP
                return APIResponse.returnSuccess(domainService.getCPDomainByMerchantGroupV2(domainReqDTO));
            }
            // DJ
            return APIResponse.returnSuccess(domainService.getDJDomainByMerchantGroupV2(domainReqDTO));
        }catch (BusinessException e) {
            log.error("DomainController.getDomainByArea,BusinessException:", e);
            return APIResponse.returnFail(e.getMessage());
        }
        catch (Exception e) {
            log.error("DomainController.getDomainByArea,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/v2/getDomainByMerchantGroup/test")
    public APIResponse getDomainByMerchantGroupV2Test(HttpServletRequest request,
                                                  @SpringQueryMap DomainReqDTO domainReqDTO) {
        log.info("{}部门调用接口获取域名：param:【{}】",domainReqDTO.getTab(), JSONObject.toJSONString(domainReqDTO));

        if(StringUtils.isBlank(domainReqDTO.getTab()) || !"cp,CP,dj,DJ".contains(domainReqDTO.getTab())){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            if(domainReqDTO.getTab().equalsIgnoreCase("cp")){
                if(domainReqDTO.getMerchantAccount() == null){
                    return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
                }
                // CP
                return APIResponse.returnSuccess(domainService.getCPDomainByMerchantGroupV2Test(domainReqDTO));
            }
            // DJ
            return APIResponse.returnSuccess(domainService.getDJDomainByMerchantGroupV2(domainReqDTO));
        }catch (BusinessException e) {
            log.error("DomainController.getDomainByArea,exception:", e);
            return APIResponse.returnFail(e.getMessage());
        }
        catch (Exception e) {
            log.error("DomainController.getDomainByArea,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("test")
    public void test(){
        domainService2.checkMerchantUseByCp();
    }

}
