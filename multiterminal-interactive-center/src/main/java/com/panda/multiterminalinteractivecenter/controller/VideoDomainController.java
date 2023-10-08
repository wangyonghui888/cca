package com.panda.multiterminalinteractivecenter.controller;


import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.dto.FrontDomainDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.VideoMerchantGroup;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.enums.ResponseEnum;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.VideoDomainService;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroup;
import com.panda.sport.merchant.common.po.bss.VideoMerchantGroupDomainPO;
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
@RequestMapping("/v1/video/domain")
@Slf4j
@Validated
@RefreshScope
public class VideoDomainController {

    @Autowired
    private VideoDomainService videoDomainService;
    @Autowired
    private MerchantLogService merchantLogService;

    private static final String pattern = "^(http(s)?://)\\w+[^\\s]+(\\.[^\\s]+)$";

    @PostMapping("/replaceDomain")
    public APIResponse<Object> replaceDomain(HttpServletRequest request,
                                             @RequestBody FrontDomainDTO merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("video/domain/replaceDomain, userName:{}, param:{}", userName, merchantDomain);
        try {
            if (StringUtils.isAnyBlank(merchantDomain.getOldDomain(), merchantDomain.getNewDomain())) {
                return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            merchantDomain.setUserName(JWTUtil.getUsername(request.getHeader("token")));
            return videoDomainService.replaceDomain(merchantDomain);
        } catch (Exception e) {
            log.error("VideoDomainController.replaceDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/clearCache")
    public APIResponse<Object> clearCache(HttpServletRequest request) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("video/domain/clearCache, userName:{}", userName);
        try {
            MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("clearCache"), null, null);
            merchantLogService.saveLog(MerchantLogPageEnum.VIDEO_DOMAIN_SET, MerchantLogTypeEnum.CLEAR_CACHE, filedVO,  null,
                    "", request);
            return videoDomainService.clearCache();
        } catch (Exception e) {
            log.error("VideoDomainController.clearCache,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }


    @PostMapping("/queryMerchantGroup")
    public APIResponse<Object> queryVideoMerchantGroup() {
        try {
            return videoDomainService.queryMerchantGroup();
        } catch (Exception e) {
            log.error("VideoDomainController.queryVideoMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/createMerchantGroup")
    public APIResponse<Object> createVideoMerchantGroup(HttpServletRequest request, @RequestBody VideoMerchantGroup merchantGroup) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        if (StringUtils.isEmpty(merchantGroup.getGroupName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (StringUtils.isAnyBlank(merchantGroup.getVideoAll(),merchantGroup.getVideoExcitingEditing())) {
            return APIResponse.returnFail("请将全量域名,精彩剪辑域名填写！");
        }
        Long currentTime = System.currentTimeMillis();
        merchantGroup.setCreateTime(currentTime);
        merchantGroup.setCreateUser(userName);
        merchantGroup.setUpdateTime(currentTime);
        merchantGroup.setUpdateUser(userName);
        try {
            return videoDomainService.createMerchantGroup(merchantGroup, request);
        } catch (Exception e) {
            log.error("VideoDomainController.createMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/updateMerchantGroup")
    public APIResponse<Object> updateMerchantGroup(HttpServletRequest request, @RequestBody VideoMerchantGroup merchantGroup) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("VideoDomainController.updateMerchantGroup,userName:{}", userName);
        if (StringUtils.isAnyEmpty(merchantGroup.getGroupName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        Long currentTime = System.currentTimeMillis();
        merchantGroup.setUpdateTime(currentTime);
        merchantGroup.setUpdateUser(userName);
        try {
            return videoDomainService.updateMerchantGroup(merchantGroup, request);
        } catch (Exception e) {
            log.error("VideoDomainController.updateMerchantGroup,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 切换商户域名（手动却换）
     */
    @PostMapping("/changeMerchantDomain")
    public APIResponse<Object> changeMerchantDomain(HttpServletRequest request, @RequestBody DomainChangeDTO domainChangeDto) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("video/domain/changeMerchantDomain,newMerchantDomain=" +
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
            return videoDomainService.changeMerchantDomain(domainChangeDto);
        } catch (Exception e) {
            log.error("VideoDomainController.changeMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 清除缓存
     */
    @GetMapping("/delMerchantDomainCache")
    public APIResponse<Object> delMerchantDomainCache(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("video/domain/delMerchantDomainCache,id = {},userName :{}", id, userName);
        try {
            return videoDomainService.delMerchantDomainCache(id);
        } catch (Exception e) {
            log.error("VideoDomainController.changeMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 域名池设置，域名筛选
     */
    @PostMapping("/queryMerchantDomain")
    public APIResponse<?> queryMerchantDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("video/domain/queryMerchantDomain,userName:{}", userName);
        try {
            return videoDomainService.queryMerchantDomain(merchantDomain);
        } catch (Exception e) {
            log.error("VideoDomainController.queryMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }


    /**
     * 导入域名
     */
    @PostMapping(value = "/createVideoDomain")
    public APIResponse<Object> createFrontendDomain(HttpServletRequest request,
                                                    @RequestBody FrontendMerchantDomain merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("VideoDomainController.createFrontendDomain，Param:{},user:{}", merchantDomain, userName);
        if (StringUtils.isEmpty(merchantDomain.getDomainName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        Long currentTime = System.currentTimeMillis();
        merchantDomain.setCreateTime(currentTime);
        merchantDomain.setCreateUser(userName);
        merchantDomain.setUpdateTime(currentTime);
        merchantDomain.setUpdateUser(userName);
        try {
            return videoDomainService.createVideoDomain(merchantDomain, request);
        } catch (Exception e) {
            log.error("VideoDomainController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 删除域名
     */
    @PostMapping(value = "/delVideoDomain")
    public APIResponse<Object> delFrontendDomain(HttpServletRequest request, @RequestBody FrontendMerchantDomain merchantDomain) {
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("VideoDomainController.delFrontendDomain,param:{},userName:{}", merchantDomain, userName);
        if (merchantDomain.getId() == null) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        Long currentTime = System.currentTimeMillis();
        merchantDomain.setUpdateTime(currentTime);
        merchantDomain.setUpdateUser(userName);
        try {
            return videoDomainService.delVideoDomain(merchantDomain, request);
        } catch (Exception e) {
            log.error("VideoDomainController.createFrontendDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryVideoMerchantDomain")
    public APIResponse<Object> queryVideoMerchantDomain(@RequestBody VideoMerchantGroupDomainPO frontendMerchantGroup) {
        try {
            return videoDomainService.queryVideoMerchantDomain(frontendMerchantGroup);
        } catch (Exception e) {
            log.error("VideoDomainController.queryVideoMerchantDomain,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/queryMerchantList")
    public APIResponse<Object> queryMerchantList(@RequestBody FrontendMerchantGroup frontendMerchantGroup){
        try {
            return videoDomainService.queryMerchantList(frontendMerchantGroup);
        }catch (Exception e){
            log.error("VideoDomainController.queryMerchantList,exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }
}
