package com.panda.multiterminalinteractivecenter.controller;


import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.dto.FrontDomainDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroup;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.enums.ResponseEnum;
import com.panda.multiterminalinteractivecenter.service.FrontendDomainV2Service;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


/**
 * 前端域名管理
 */
@RestController
@RequestMapping("/v2/frontend/domain")
@Slf4j
@Validated
@RefreshScope
public class FrontendDomainV2Controller {

    @Autowired
    private FrontendDomainV2Service frontendDomainService;
    @Autowired
    private MerchantLogService merchantLogService;

    private static final String pattern = "^(http(s)?://)\\w+[^\\s]+(\\.[^\\s]+)$";

    @PostMapping("/replaceDomain")
    public APIResponse<Object> replaceDomain(HttpServletRequest request,
                                             @RequestBody FrontDomainDTO merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("frontend/domain/replaceDomain, userName:{}, param:{}", userName, merchantDomain);
        try {
            merchantDomain.setUserName(userName);
            merchantDomain.setIp(IPUtils.getIpAddr(request));

            if (StringUtils.isAnyBlank(merchantDomain.getOldDomain(), merchantDomain.getNewDomain())) {
                return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            return frontendDomainService.replaceDomain(merchantDomain);
        } catch (Exception e) {
            log.error("FrontendDomainController.replaceDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/clearCache")
    public APIResponse<Object> clearCache(HttpServletRequest request) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("frontend/domain/clearCache, userName:{}", userName);
        try {
            MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("clearCache"), null, null);
            merchantLogService.saveLog(MerchantLogPageEnum.FRONT_DOMAIN_SET, MerchantLogTypeEnum.CLEAR_CACHE, filedVO,  null,
                    "", request);
            return frontendDomainService.clearCache();
        } catch (Exception e) {
            log.error("FrontendDomainController.clearCache,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryFrontendMerchantGroup")
    public APIResponse<Object> queryFrontendMerchantGroup() {
        try {
            return frontendDomainService.queryFrontendMerchantGroup();
        } catch (Exception e) {
            log.error("FrontendDomainController.queryFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/createFrontendMerchantGroup")
    public APIResponse<Object> createFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroup merchantGroup) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        if (StringUtils.isEmpty(merchantGroup.getGroupName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (StringUtils.isAnyBlank(merchantGroup.getPc(),merchantGroup.getH5())) {
            return APIResponse.returnFail("请将PC,H5域名填写！");
        }
        Long currentTime = System.currentTimeMillis();
        merchantGroup.setCreateTime(currentTime);
        merchantGroup.setCreateUser(userName);
        merchantGroup.setUpdateTime(currentTime);
        merchantGroup.setUpdateUser(userName);
        try {
            return frontendDomainService.createFrontendMerchantGroup(merchantGroup, request);
        } catch (Exception e) {
            log.error("FrontendDomainController.createFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/updateFrontendMerchantGroup")
    public APIResponse<Object> updateFrontendMerchantGroup(HttpServletRequest request, @RequestBody FrontendMerchantGroup merchantGroup) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("FrontendDomainController.updateFrontendMerchantGroup,userName:{}", userName);
        if (StringUtils.isAnyEmpty(merchantGroup.getGroupName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        Long currentTime = System.currentTimeMillis();
        merchantGroup.setUpdateTime(currentTime);
        merchantGroup.setUpdateUser(userName);
        try {
            return frontendDomainService.updateFrontendMerchantGroup(merchantGroup, request);
        } catch (Exception e) {
            log.error("FrontendDomainController.updateFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 切换商户域名（手动却换）
     */
    @PostMapping("/changeMerchantDomain")
    public APIResponse<Object> changeMerchantDomain(HttpServletRequest request, @RequestBody DomainChangeDTO domainChangeDto) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("frontend/domain/changeMerchantDomain,newMerchantDomain=" +
                domainChangeDto.getNewDomain() + ",oldMerchantDomain="
                + domainChangeDto.getOldDomain() + ",groupId" + domainChangeDto.getGroupId()
                + ",userName:" + userName);
        if (StringUtils.isEmpty(domainChangeDto.getNewDomain())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (!Pattern.matches(pattern, domainChangeDto.getNewDomain())) {
            return APIResponse.returnFail("新域名格式异常，正确格式为【http(s)://xxx】");
        }
        try {
            domainChangeDto.setUpdateUser(JWTUtil.getUsername(request.getHeader("token")));
            domainChangeDto.setIp(IPUtils.getIpAddr(request));
            return frontendDomainService.changeMerchantDomain(domainChangeDto);
        } catch (Exception e) {
            log.error("FrontendDomainController.changeMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 清除缓存
     */
    @GetMapping("/delMerchantDomainCache")
    public APIResponse<Object> delMerchantDomainCache(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("frontend/domain/delMerchantDomainCache,id = {},userName :{}", id, userName);
        try {
            return frontendDomainService.delMerchantDomainCache(id);
        } catch (Exception e) {
            log.error("FrontendDomainController.changeMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 域名池设置，域名筛选
     */
    @PostMapping("/queryMerchantDomain")
    public APIResponse<?> queryMerchantDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("frontend/domain/queryMerchantDomain,userName:{}", userName);
        try {
            return frontendDomainService.queryMerchantDomain(merchantDomain);
        } catch (Exception e) {
            log.error("FrontendDomainController.queryMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }


    /**
     * 导入域名
     */
    @PostMapping(value = "/createFrontendDomain")
    public APIResponse<Object> createFrontendDomain(HttpServletRequest request,
                                                    @RequestBody FrontendMerchantDomain merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("FrontendDomainController.createFrontendDomain，Param:{},user:{}", merchantDomain, userName);
        if (StringUtils.isEmpty(merchantDomain.getDomainName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        Long currentTime = System.currentTimeMillis();
        merchantDomain.setCreateTime(currentTime);
        merchantDomain.setCreateUser(userName);
        merchantDomain.setUpdateTime(currentTime);
        merchantDomain.setUpdateUser(userName);
        try {
            return frontendDomainService.createFrontendDomain(merchantDomain, request);
        } catch (Exception e) {
            log.error("FrontendDomainController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 删除域名
     */
    @PostMapping(value = "/delFrontendDomain")
    public APIResponse<Object> delFrontendDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("FrontendDomainController.delFrontendDomain,param:{},userName:{}", merchantDomain, userName);
        if (merchantDomain.getId() == null) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        Long currentTime = System.currentTimeMillis();
        merchantDomain.setUpdateTime(currentTime);
        merchantDomain.setUpdateUser(userName);
        try {
            return frontendDomainService.delFrontendDomain(merchantDomain, request);
        } catch (Exception e) {
            log.error("FrontendDomainController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryFrontendMerchantDomain")
    public APIResponse<Object> queryFrontendMerchantDomain(@RequestBody FrontendMerchantGroupDomainPO frontendMerchantGroup) {
        try {
            return frontendDomainService.queryFrontendMerchantDomain(frontendMerchantGroup);
        } catch (Exception e) {
            log.error("FrontendDomainController.queryFrontendMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryMerchantList")
    public APIResponse<Object> queryMerchantList(@RequestBody com.panda.sport.merchant.common.po.bss.FrontendMerchantGroup frontendMerchantGroup){
        try {
            log.info("queryMerchantSimpleListByParams param = {}", JSON.toJSONString(frontendMerchantGroup));
            return frontendDomainService.queryMerchantList(frontendMerchantGroup);
        }catch (Exception e){
            log.error("FrontendDomainController.queryFront838541endMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }
}
