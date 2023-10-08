package com.panda.multiterminalinteractivecenter.controller;


import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroup;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroupDomainPO;
import com.panda.multiterminalinteractivecenter.enums.ResponseEnum;
import com.panda.multiterminalinteractivecenter.service.FrontendDomainService;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @Deprecated 代码已经不适用了
 * @see com.panda.multiterminalinteractivecenter.controller.FrontendDomainV2Controller
 */
@RestController
@RequestMapping("/frontend/domain")
@Slf4j
@Validated
@RefreshScope
@Deprecated
public class FrontendDomainController {

    @Autowired
    private FrontendDomainService frontendDomainService;

    private static final String pattern = "^(http(s)?:\\/\\/)\\w+[^\\s]+(\\.[^\\s]+)$";

    @PostMapping("/queryMerchantDomain")
    public APIResponse<Object> queryMerchantDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain){
        log.info("frontend/domain/queryMerchantDomain");
        try {
            return frontendDomainService.queryMerchantDomain(merchantDomain);
        }catch (Exception e){
            log.error("FrontendDomainController.queryMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/changeMerchantDomain")
    public APIResponse<Object> changeMerchantDomain(HttpServletRequest request, @RequestBody DomainChangeDTO domainChangeDto){
        log.info("frontend/domain/changeMerchantDomain,newMerchantDomain=" +
                domainChangeDto.getNewDomain() + ",oldMerchantDomain=" + domainChangeDto.getOldDomain() + ",groupId" + domainChangeDto.getGroupId());
        if (StringUtils.isAnyEmpty(domainChangeDto.getNewDomain(), domainChangeDto.getOldDomain())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if(!Pattern.matches(pattern, domainChangeDto.getNewDomain()) || !Pattern.matches(pattern, domainChangeDto.getOldDomain())){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            domainChangeDto.setUpdateUser(JWTUtil.getUsername(request.getHeader("token")));
            return APIResponse.returnSuccess(frontendDomainService.changeMerchantDomain(domainChangeDto));
        }catch (Exception e){
            log.error("FrontendDomainController.changeMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @GetMapping("/delMerchantDomainCache")
    public APIResponse<Object> delMerchantDomainCache(HttpServletRequest request, @RequestParam(value = "merchantDomainGroup") String merchantDomainGroup){
        log.info("frontend/domain/delMerchantDomainCache,merchantDomainGroup = ", merchantDomainGroup);
        try {
            return APIResponse.returnSuccess(frontendDomainService.delMerchantDomainCache());
        }catch (Exception e){
            log.error("FrontendDomainController.changeMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/createFrontendMerchantGroup")
    public APIResponse<Object> createFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroup merchantGroup){
        log.info("FrontendDomainController.createFrontendMerchantGroup");
        if(StringUtils.isAnyEmpty(merchantGroup.getGroupName(),merchantGroup.getGroupType())){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        Long currentTime = System.currentTimeMillis();
        merchantGroup.setCreateTime(currentTime);
        merchantGroup.setCreateUser(userName);
        merchantGroup.setUpdateTime(currentTime);
        merchantGroup.setUpdateUser(userName);
        try {
            return APIResponse.returnSuccess(frontendDomainService.createFrontendMerchantGroup(merchantGroup).getData());
        }catch (Exception e){
            log.error("FrontendDomainController.createFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/updateFrontendMerchantGroup")
    public APIResponse<Object> updateFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroup merchantGroup){
        log.info("FrontendDomainController.updateFrontendMerchantGroup");
        if(StringUtils.isAnyEmpty(merchantGroup.getGroupName(),merchantGroup.getGroupType())){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        Long currentTime = System.currentTimeMillis();
        merchantGroup.setUpdateTime(currentTime);
        merchantGroup.setUpdateUser(userName);
        try {
            return APIResponse.returnSuccess(frontendDomainService.updateFrontendMerchantGroup(merchantGroup).getData());
        }catch (Exception e){
            log.error("FrontendDomainController.updateFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryFrontendMerchantGroup")
    public APIResponse<Object> queryFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroupDomainPO groupDomainPo){
        log.info("frontend/domain/queryFrontendMerchantGroup");
        try {
            return frontendDomainService.queryFrontendMerchantGroup(groupDomainPo);
        }catch (Exception e){
            log.error("FrontendDomainController.queryFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/createFrontendDomain")
    public APIResponse<Object> createFrontendDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain){
        log.info("FrontendDomainController.createFrontendDomain");
        if(StringUtils.isEmpty(merchantDomain.getDomainName())){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        Long currentTime = System.currentTimeMillis();
        merchantDomain.setCreateTime(currentTime);
        merchantDomain.setCreateUser(userName);
        merchantDomain.setUpdateTime(currentTime);
        merchantDomain.setUpdateUser(userName);
        try {
            return APIResponse.returnSuccess(frontendDomainService.createFrontendDomain(merchantDomain).getData());
        }catch (Exception e){
            log.error("FrontendDomainController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/delFrontendDomain")
    public APIResponse<Object> delFrontendDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain){
        log.info("FrontendDomainController.delFrontendDomain");
        if(merchantDomain.getId() == null && CollectionUtils.isEmpty(merchantDomain.getIds())){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        Long currentTime = System.currentTimeMillis();
        merchantDomain.setUpdateTime(currentTime);
        merchantDomain.setUpdateUser(userName);
        try {
            return APIResponse.returnSuccess(frontendDomainService.delFrontendDomain(merchantDomain).getData());
        }catch (Exception e){
            log.error("FrontendDomainController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }
}
