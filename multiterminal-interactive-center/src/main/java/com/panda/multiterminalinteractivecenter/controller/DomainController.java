package com.panda.multiterminalinteractivecenter.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.dto.MerchantDomainReqDTO;
import com.panda.multiterminalinteractivecenter.entity.LineCarrier;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.service.*;
import com.panda.multiterminalinteractivecenter.service.impl.AbstractMerchantDomainService;
import com.panda.multiterminalinteractivecenter.service.impl.LineCarrierServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.TyMerchantDomainServiceImpl;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.utils.TelegramBot;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/merchant/domain")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainController {

    private final TyMerchantDomainServiceImpl merchantDomainService;

    private final AbstractMerchantDomainService absTractDomainService;

    private final OssService ossService;

    private final TelegramBot telegramBot;

    private final IMongoService mongoService;

    private final FrontendDomainV2Service frontendDomainService;

    private final MerchantLogService merchantLogService;

    private final LineCarrierServiceImpl lineCarrierServiceImpl;


    @GetMapping("list")
    public APIResponse<?> list(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "size", required = false) Integer size,
                               @RequestParam(value = "domainName", required = false) String domainName,
                               @RequestParam(value = "domainType", required = false) Integer domainType,
                               @RequestParam(value = "domainGroupId", required = false) Long domainGroupId,
                               @RequestParam(value = "domainGroupName", required = false) String domainGroupName,
                               @RequestParam(value = "lineCarrierId", required = false) Long lineCarrierId,
                               @RequestParam(value = "groupType", required = false) Integer groupType,
                               @RequestParam(value = "used", required = false) Boolean used,
                               @RequestParam(value = "tab", required = false) String tab
    ) {
        if (page == null) page = 1;
        if (size == null) size = 20;
        if (org.apache.commons.lang.StringUtils.isBlank(tab)) tab = "ty";
        return absTractDomainService.getDomainServiceBean(tab).pageList(page, size, domainName, domainType, domainGroupId, domainGroupName, lineCarrierId, groupType, used, tab);
    }


    @PostMapping(value = "create")
    @Deprecated
    public APIResponse<Object> create(HttpServletRequest request, @RequestBody TyDomain tyDomain) {
        if (StringUtils.isEmpty(tyDomain.getDomainName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String currentUser = JWTUtil.getUsername(request.getHeader("token"));
        tyDomain.setCreateUser(currentUser);
        tyDomain.setUpdateUser(currentUser);
        absTractDomainService.getDomainServiceBean(tyDomain.getTab()).create(tyDomain);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "import")
    public APIResponse<Object> importDomains(HttpServletRequest request, @RequestBody DomainImportReqDTO domainImportReqDTO) {
        if (CollectionUtil.isEmpty(domainImportReqDTO.getDomainName())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if (org.apache.commons.lang.StringUtils.isBlank(domainImportReqDTO.getTab())) domainImportReqDTO.setTab("ty");
        domainImportReqDTO.setOperator(JWTUtil.getUsername(request.getHeader("token")));
        absTractDomainService.getDomainServiceBean(domainImportReqDTO.getTab()).importDomains(domainImportReqDTO);
        // 操作日志
        MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
        filedVO.setFieldName(Arrays.asList("域名类型", "线路商","商户分组类型","域名"));
        String domainType = DomainTypeEnum.getNameByCode(domainImportReqDTO.getDomainType());
        String groupName = MerchantGroupEnum.getNameByKey(domainImportReqDTO.getGroupType());
        LineCarrier lineCarrier = lineCarrierServiceImpl.getByLineCarrierId(domainImportReqDTO.getLineCarrierId());
        filedVO.setAfterValues(Arrays.asList(domainType,lineCarrier.getLineCarrierName(), groupName, JSON.toJSONString(domainImportReqDTO.getDomainName())));
        String dataId= String.join(",", domainImportReqDTO.getDomainName()) +StringPool.AMPERSAND + domainType +StringPool.AMPERSAND + groupName;
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.DOMAIN_IMPORT, filedVO,  null,
                dataId, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "edit")
    public APIResponse<Object> edit(HttpServletRequest request, @RequestBody TyDomain tyDomain) {
        if (tyDomain.getId() == null) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if (org.apache.commons.lang.StringUtils.isBlank(tyDomain.getTab())) tyDomain.setTab("ty");
        tyDomain.setUpdateUser(JWTUtil.getUsername(request.getHeader("token")));
        absTractDomainService.getDomainServiceBean(tyDomain.getTab()).edit(tyDomain, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "del")
    public APIResponse<Object> del(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        String id = requestMap.get("id");
        String tab = requestMap.get("tab");
        if (id == null || StringUtils.isBlank(tab)) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        absTractDomainService.getDomainServiceBean(tab).delete(Long.valueOf(id),tab, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "off")
    public APIResponse<Object> off(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        String ids = requestMap.get("ids");
        String tab = requestMap.get("tab");
        String merchantGroupName = requestMap.get("merchantGroupName");
        String domainTypeName = requestMap.get("domainTypeName");
        if (StringUtils.isBlank(ids) || StringUtils.isBlank(tab)) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        log.info("/merchant/domain/off,param:id,{},tab,{},user,{}",ids,tab,JWTUtil.getUsername(request.getHeader("token")));
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList());
        absTractDomainService.getDomainServiceBean(tab).off(idList,tab, request,merchantGroupName + StringPool.AMPERSAND + domainTypeName);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "replace")
    public APIResponse<Object> replace(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        String oldDomainId = requestMap.get("oldDomainId");
        String domainId = requestMap.get("domainId");
        String tab = requestMap.get("tab");
        if (oldDomainId == null || domainId == null||tab==null) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        absTractDomainService.getDomainServiceBean(tab).replace(Long.valueOf(oldDomainId), Long.valueOf(domainId),tab, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "switch/status")
    public APIResponse<Object> switchStatus(HttpServletRequest request, @RequestBody TyDomain tyDomain) {
        if (null == tyDomain.getId() || null == tyDomain.getStatus() || null == tyDomain.getEnable()||null == tyDomain.getTab()) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("/merchant/domain/switch/status,param:param,{},user,{}", JSON.toJSONString(tyDomain),userName);
        tyDomain.setUpdateUser(userName);
        absTractDomainService.getDomainServiceBean(tyDomain.getTab()).switchStatus(tyDomain);
        //操作日志
        String beforeValue = tyDomain.getStatus() == 1 ? "禁用" : "启用";
        String afterValue = tyDomain.getStatus() == 1 ? "启用" : "禁用";
        TyDomain domain = absTractDomainService.getDomainServiceBean(tyDomain.getTab()).selectById(tyDomain.getId());
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("enableSwitch"), beforeValue, afterValue);
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.EDIT_INFO_STATUS, filedVO,  null,
                domain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(domain.getDomainType()) + StringPool.AMPERSAND +
                        MerchantGroupEnum.getNameByKey(domain.getGroupType())+ StringPool.AMPERSAND + tyDomain.getId(), request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "switch/selfTestTag")
    public APIResponse<Object> switchSelfTestTag(HttpServletRequest request,@RequestBody TyDomain tyDomain) {
        if (null == tyDomain.getId() || null == tyDomain.getSelfTestTag()|| null == tyDomain.getTab()) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String userName = JWTUtil.getUsername(request.getHeader("token"));
        log.info("/merchant/domain/switch/selfTestTag,param:param,{},user,{}", JSON.toJSONString(tyDomain),userName);
        absTractDomainService.getDomainServiceBean(tyDomain.getTab()).switchSelfTestTag(tyDomain);
        //操作日志
        String beforeValue = tyDomain.getSelfTestTag() == 1 ? "禁用" : "启用";
        String afterValue = tyDomain.getSelfTestTag() == 1 ? "启用" : "禁用";
        TyDomain domain = absTractDomainService.getDomainServiceBean(tyDomain.getTab()).selectById(tyDomain.getId());
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("selfTestSwitch"), beforeValue, afterValue);
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.EDIT_INFO_STATUS, filedVO,  null,
                domain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(domain.getDomainType()) + StringPool.AMPERSAND +
                        MerchantGroupEnum.getNameByKey(domain.getGroupType())+ StringPool.AMPERSAND + tyDomain.getId(), request);
        return APIResponse.returnSuccess();
    }

    @GetMapping(value = "/getDomainByMerchantAndArea")
    public List<?> getDomainByMerchantAndArea(@RequestParam(value = "merchantGroupId", required = false) Long merchantGroupId,
                                              @RequestParam(value = "domainGroupCode", required = false) String domainGroupCode,
                                              @RequestParam(value = "tab", required = false) String tab) {
        if (StringUtils.isEmpty(domainGroupCode)) {
            log.error("getDomainByMerchantAndArea非法参数");
            return Lists.newArrayList();
        }
        if(StringUtils.isBlank(tab)) tab = "ty";
        log.info("/getDomainByMerchantAndArea merchantGroupId:{},domainGroupCode:{}" , merchantGroupId,domainGroupCode);
        return absTractDomainService.getDomainServiceBean(tab).getDomainByMerchantAndArea(merchantGroupId, domainGroupCode);
    }

    /**前端域名池,api-new使用的*/
    @GetMapping(value = "/getFrontDomainByTerminal")
    public String getFrontDomainByTerminal(@RequestParam(value = "terminal") String terminal,
                                            @RequestParam(value = "merchantCode") String merchantCode) {
        if (StringUtils.isAnyBlank(terminal,merchantCode)) {
            return null;
        }
        log.info("/getDomainByMerchantAndArea merchantCode:{},terminal:{}" , merchantCode,terminal);
        return frontendDomainService.getFrontDomainByTerminal(terminal,merchantCode);
    }

    @GetMapping(value = "getThirdEnable")
    public APIResponse<Object> getThirdEnable() {
        try {
            return APIResponse.returnSuccess(merchantDomainService.getThirdEnable());
        } catch (Exception e) {
            log.error("DomainController.getThirdEnable,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping(value = "editThirdEnable")
    public APIResponse<Object> updateThirdEnable(HttpServletRequest request, @RequestBody JSONObject param) {
        log.info("/updateThirdEnable.user:{},param:{}", JWTUtil.getUsername(request.getHeader("token")), param.toJSONString());
        Boolean enable = param.getBoolean("enable");
        if (null == enable) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            merchantDomainService.editThirdEnable(enable, request);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("DomainController.editThirdEnable,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "getNewH5PcDomain")
    public APIResponse getNewH5PcDomain() {
        return merchantDomainService.getNewH5PcDomain();
    }

    @GetMapping("/checkOssDomain")
    public void checkOssDomain( @RequestParam(value = "ossText", required = false) String ossText) {
        ossService.checkOssDomain(ossText);
    }


    @PostMapping(value = "cleanMerchant")
    @Deprecated
    public APIResponse<?> cleanMerchant(@RequestBody MerchantDomainReqDTO merchantDomainReqDTO) {
        log.info("/merchant/domain/cleanMerchant" + ",merchantTag:"
                + merchantDomainReqDTO.getMerchantTag() + ",containsType:"
                + merchantDomainReqDTO.getContainsType() + ",merchantCode:"
                + merchantDomainReqDTO.getMerchantCode() + ",parentCode:"
                + merchantDomainReqDTO.getContainsStr() + ",containsStr:" + merchantDomainReqDTO.getContainsStr());
        try {
            return merchantDomainService.cleanMerchant(merchantDomainReqDTO.getMerchantCode());
        } catch (Exception e) {
            log.error("MerchantController.cleanMerchant,exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "resMsg")
    public void resMsg(@RequestParam(value = "resMsg", required = false) String resMsg) {
         telegramBot.sendGroupMessage(resMsg);
    }

    @GetMapping(value = "mangoMsg")
    public void mangoMsg(@RequestParam(value = "resMsg", required = false) String resMsg) {
        mongoService.send(resMsg);
    }
}
