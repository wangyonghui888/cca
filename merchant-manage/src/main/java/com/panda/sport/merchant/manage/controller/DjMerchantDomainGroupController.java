package com.panda.sport.merchant.manage.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.merchant.TMerchantGroup;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.MerchantLogFindVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.impl.DomainAbstractService;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/5/27 17:34:05
 */
@RestController
@RequestMapping("/manage/merchantDomainGroup/dj")
@Slf4j
@Validated
public class DjMerchantDomainGroupController {

    @Autowired()
    @Qualifier("DjDomainServiceImpl")
    DomainAbstractService djDomainAbstractService;

    @PostMapping("/getMerchantGroup")
    public Response getMerchantGroup(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        log.info("获取商户组 /getMerchantGroup param = {}", JSON.toJSONString(domainVo));
        domainVo.setMerchantGroupCode(1);
        return djDomainAbstractService.getMerchantGroup(domainVo);
    }

    @PostMapping("/saveMerchantGroup")
    public Response saveMerchantGroup(HttpServletRequest request, @RequestBody TMerchantGroup tMerchantGroup) {
        log.info("新增商户组 /saveMerchantGroup param = {}", JSON.toJSONString(tMerchantGroup));
        tMerchantGroup.setGroupCode(1);
        return djDomainAbstractService.saveMerchantGroup(tMerchantGroup);
    }

    @PostMapping("/updateMerchantGroup")
    public Response updateMerchantGroup(HttpServletRequest request, @RequestBody TMerchantGroup tMerchantGroup) {
        log.info("跟新商户组 /updateMerchantGroup param = {}", JSON.toJSONString(tMerchantGroup));
        return djDomainAbstractService.updateMerchantGroup(tMerchantGroup);
    }

    @PostMapping("/deleteMerchantGroup")
    public Response deleteMerchantGroup(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        log.info("删除商户组 /deleteMerchantGroup param = {}", JSON.toJSONString(domainVo));
        return djDomainAbstractService.deleteMerchantGroup(domainVo.getId().intValue());
    }

    @PostMapping("/setMerchantGroupInfo")
    public Response setMerchantGroupInfo(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        log.info("设置商户组 /setMerchantGroupInfo param = {}", JSON.toJSONString(domainVo));
        djDomainAbstractService.setMerchantGroupInfo(domainVo.getThirdMerchantVos(),domainVo.getMerchantGroupId().intValue());
        return Response.returnSuccess();
    }

    @GetMapping("/getMerchantList")
    public Response getMerchantList(HttpServletRequest request,@RequestParam(value = "id",required = false) Long merchantGroupId) {
        return Response.returnSuccess(djDomainAbstractService.getMerchantList(1));
    }

    @GetMapping("/getMerchantListAll")
    public Response getMerchantListAll(HttpServletRequest request) {
        return Response.returnSuccess(djDomainAbstractService.getMerchantList());
    }

    @PostMapping(value = "/saveDomain")
    public Response saveDomain(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        Integer userId = SsoUtil.getUserId(request);
        return djDomainAbstractService.saveDomain(userId, domainVo);
    }

    @PostMapping("/deleteDomain")
    public Response deleteDomain(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        log.info("删除 /deleteDomain param = {}", id);
        Integer userId = SsoUtil.getUserId(request);
        String ip = IPUtils.getIpAddr(request);
        djDomainAbstractService.deleteDomain(userId, id.intValue(),ip);
        return Response.returnSuccess("删除成功");
    }

    @PostMapping("/deleteDomainAll")
    public Response deleteDomain(HttpServletRequest request,@RequestBody DomainVo domainVo) {
        log.info("域名查询 /deleteDomain param = {}", JSON.toJSONString(domainVo));
        djDomainAbstractService.deleteDomainList(domainVo.getMerchantGroupId().intValue());
        return Response.returnSuccess("删除成功");
    }

    @PostMapping("/queryDomain")
    public Response queryDomain(HttpServletRequest request, @RequestBody DomainVo domainVo) {
        log.info("域名查询 /queryDomain param = {}", JSON.toJSONString(domainVo));
        Response response = djDomainAbstractService.getDomainList(domainVo);
        return response;
    }


    @PostMapping("/updateMerchantDomain")
    public Response updateMerchantDomain(HttpServletRequest request,@RequestBody DomainVo domainVo) {
        if (domainVo.getMerchantGroupId() == null || StringUtils.isEmpty(domainVo.getDomainName())){
            return Response.returnFail("参数 groupId 或 domain 不能为空");
        }
        Integer userId = null;
        try {
             userId = SsoUtil.getUserId(request);
        }catch (Exception e){
            log.warn("获取用户异常！");
        }
        String requestType ="dj";
        domainVo.setOperatTypeEnum(MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_DJ_Domian);
        return djDomainAbstractService.updateMerchantDomain(request,userId,domainVo.getOperatTypeEnum(),domainVo.getOldDomain(), domainVo.getDomainName(), domainVo.getDomainType() ,domainVo.getMerchantGroupId().intValue(),IPUtils.getIpAddr(request),domainVo.getUsername(), requestType);
    }

    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 查询域名切换异常日志
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findApiDomainLog")
    public Response findApiDomainLog(HttpServletRequest request, @RequestBody MerchantLogFindVO findVO) {
        List<Integer> useTypes = Lists.newArrayList();
        if (findVO.getOperatSourceType() == null) {
            useTypes.add(MerchantLogTypeEnum.CHANGE_MERCHANT_DJ_Domian.getCode());
            useTypes.add(MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_DJ_Domian.getCode());
        } else if (findVO.getOperatSourceType() == 1) {
            useTypes.add(MerchantLogTypeEnum.CHANGE_MERCHANT_DJ_Domian.getCode());
        } else if (findVO.getOperatSourceType() == 2) {
            useTypes.add(MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_DJ_Domian.getCode());
        }
        findVO.setOperatTypes(useTypes);
        return Response.returnSuccess(merchantLogService.queryLog(findVO));
    }

    @PostMapping("/resetDomain")
    public Response resetDomain(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        log.info("恢复成功 /deleteDomain param = {}", id);
        Integer userId = SsoUtil.getUserId(request);
        djDomainAbstractService.resetDomain(userId, id);
        return Response.returnSuccess("恢复成功");
    }
}
