package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.dto.FrontDomainDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.entity.VideoMerchantGroup;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiNewClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.MerchantGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.TyDomainMapper;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.VideoDomainService;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.constant.SystemConfigConstant;
import com.panda.sport.merchant.common.dto.FrontDomainMerchantDTO;
import com.panda.sport.merchant.common.dto.VideoDomainMerchantDTO;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroup;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import com.panda.sport.merchant.common.po.bss.VideoMerchantGroupDomainPO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.merchant.MerchantSimpleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RefreshScope
public class VideoDomainServiceImpl implements VideoDomainService {

    private static final String pattern = "^(http(s)?://)\\w+[^\\s]+(\\.[^\\s]+)$";

    @Autowired
    private MerchantApiNewClient merchantApiNewClient;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private TyDomainMapper tyDomainMapper;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private MerchantManageClient merchantManageClient;

    @Autowired
    private TyMerchantDomainServiceImpl domainService;

    @Autowired
    private MerchantGroupMapper merchantGroupMapper;

    @Override
    public APIResponse<?> queryMerchantDomain(FrontendMerchantDomain merchantDomain) {
        try {
            Integer page = merchantDomain.getPageNum();
            Integer size = merchantDomain.getPageSize();
            if (page == null) page = 1;
            if (size == null) size = 20;

            List<Integer> domainTypeList = Lists.newArrayList();
            domainTypeList.add(DomainTypeEnum.VIDEO_ALL.getCode());
            domainTypeList.add(DomainTypeEnum.VIDEO_Exciting_Editing.getCode());
            APIResponse<?> response = domainService.pageListCore(page, size,
                    merchantDomain.getDomainName(), merchantDomain.getDomainType(), null,
                    null, null, null, null, TabEnum.TY.getName(), domainTypeList
            );

            PageInfo<TyDomain> poList = (PageInfo<TyDomain>) response.getData();
            if (CollectionUtils.isEmpty(poList.getList())) {
                return response;
            }

            List<TyDomain> domainList = poList.getList();
            for (TyDomain tyDomain : domainList) {
                tyDomain.setStatus(0);
            }
            poList.setList(domainList);

            SystemConfig systemConfig = getSystemConfigById(merchantDomain.getGroupId());
            if (systemConfig == null) {
                return response;
            }
            String videoAllUrl = getVideoAllBySystemConfig(systemConfig.getRemark());
            String videoExcitingEditingUrl = getVideoExcitingEditingBySystemConfig(systemConfig.getRemark());
            for (TyDomain tyDomain : domainList) {
                if (StringUtils.isNotBlank(videoAllUrl) && tyDomain.getDomainName().equals(videoAllUrl)) {
                    tyDomain.setStatus(1);
                }
                if (StringUtils.isNotBlank(videoExcitingEditingUrl) && tyDomain.getDomainName().equals(videoExcitingEditingUrl)) {
                    tyDomain.setStatus(1);
                }
            }
            poList.setList(domainList);

            return APIResponse.returnSuccess(poList);

        } catch (Exception e) {
            log.error("queryMerchantDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse<Object> changeMerchantDomain(DomainChangeDTO domainChangeDto) {
        try {
            String newDomain = domainChangeDto.getNewDomain();
            if (newDomain.endsWith(SystemConfigConstant.DOMAIN_COLON)) newDomain = newDomain.substring(0, newDomain.length() - 1);

            long groupId = domainChangeDto.getGroupId();
            int domainType = domainChangeDto.getDomainType();

            SystemConfig systemConfig = getSystemConfigById(groupId);
            if (systemConfig == null) {
                return APIResponse.returnFail("商户组不存在");
            }
            final String remark = systemConfig.getRemark();
            JSONObject remarkJ = getRemarkBySystemConfig(remark);

            List<Integer> domainTypeList = Lists.newArrayList();
            domainTypeList.add(DomainTypeEnum.VIDEO_ALL.getCode());
            domainTypeList.add(DomainTypeEnum.VIDEO_Exciting_Editing.getCode());
            APIResponse<?> response = domainService.pageListCore(1, 1,
                    newDomain, domainChangeDto.getDomainType(), null,
                    null, null, null, null, TabEnum.TY.getName(), domainTypeList
            );

            PageInfo<TyDomain> poList = (PageInfo<TyDomain>) response.getData();
            if (CollectionUtils.isEmpty(poList.getList())) {
                return APIResponse.returnFail("新域名不存在与域名池中，请检查！");
            }

            if (domainType == DomainTypeEnum.VIDEO_ALL.getCode()) {
                remarkJ.put(SystemConfigConstant.VIDEO_COLUMN_VIDEO_ALL, newDomain);
            }
            if (domainType == DomainTypeEnum.VIDEO_Exciting_Editing.getCode()) {
                remarkJ.put(SystemConfigConstant.VIDEO_COLUMN_VIDEO_EXCITING_EDITING, newDomain);
            }
            systemConfig.setRemark(remarkJ.toJSONString());
            merchantApiNewClient.updateSystemConfigByDomain(systemConfig);

            if(domainType == DomainTypeEnum.VIDEO_ALL.getCode() && systemConfig.getConfigValue() != null && systemConfig.getConfigValue().equals("1")){
                VideoDomainMerchantDTO request = VideoDomainMerchantDTO.builder().videoAll(newDomain).merchantCodeList(this.getMerchantCodeListBySystemConfig(remark)).build();
                this.replaceVideoDomainByMerchant(request);
            }

            // 添加操作日志
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.setFieldName(Collections.singletonList(MerchantLogTypeEnum.MANUAL_SWITCH.getRemark()));
                List<String> beforeValues = new ArrayList<>();
                beforeValues.add(domainChangeDto.getOldDomain());
                vo.setBeforeValues(beforeValues);
                List<String> afterValues = new ArrayList<>();
                afterValues.add(newDomain);
                vo.setAfterValues(afterValues);
                vo.setDomainType(domainChangeDto.getDomainType());


                MerchantLogTypeEnum merchantLogTypeEnum = MerchantLogTypeEnum.MANUAL_SWITCH;
                merchantLogService.saveLog(MerchantLogPageEnum.VIDEO_DOMAIN_SET,
                        merchantLogTypeEnum, vo,
                        MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, "0",
                        domainChangeDto.getUpdateUser(), domainChangeDto.getGroupName() + "(视频商户组)",
                        domainChangeDto.getGroupName(), domainChangeDto.getGroupName() + StringPool.AMPERSAND + groupId, "zs", domainChangeDto.getIp(),
                        null, null, domainChangeDto.getDomainType());

            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("changeMerchantDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse<Object> delMerchantDomainCache(Long id) {
        try {
            List<SystemConfig> systemConfigList = this.querySystemConfig(SystemConfig.builder().id(id).build());
            if (CollectionUtils.isNotEmpty(systemConfigList)) {
                return APIResponse.returnFail("商户组不存在！");
            }
            List<String> merchantCodeLists = Lists.newArrayList();
            JSONObject remarkJ = JSON.parseObject(systemConfigList.get(0).getRemark());
            String merchantCodesStr = remarkJ.getString("merchantCodeList");
            if (StringUtils.isNotBlank(merchantCodesStr)) {
                merchantCodeLists = Arrays.stream(merchantCodesStr.split(",")).collect(Collectors.toList());
            }
            merchantApiClient.kickoutMerchants(merchantCodeLists);
        } catch (Exception e) {
            log.error("changeMerchantDomain, exception:", e);
        }
        return APIResponse.returnSuccess();
    }


    @Override
    public APIResponse<Object> createVideoDomain(FrontendMerchantDomain merchantDomain, HttpServletRequest request) {
        try {
            if(StringUtils.isBlank(merchantDomain.getDomainName())){
                return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
            }
            List<String> domainList = Arrays.stream(merchantDomain.getDomainName().split(",")).collect(Collectors.toList());
            for (String domain : domainList) {
                if (!Pattern.matches(pattern, domain)) {
                    return APIResponse.returnFail("域名:【"+domain+"】格式异常，正确格式为【http(s)://xxx】");
                }
            }

            domainService.importDomains(
                    new DomainImportReqDTO(merchantDomain.getDomainType(), GroupTypeEnum.C_GROUP.getGroupType(), null,
                            domainList, TabEnum.TY.getName(), merchantDomain.getCreateUser()));
            MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("domainImport"), null, JSON.toJSONString(domainList));
            try {
                MerchantGroupPO groupPO = merchantGroupMapper.selectMerchantGroupById(merchantDomain.getMerchantGroupId(), merchantDomain.getTab());
                filedVO.setMerchantName(groupPO != null ? groupPO.getGroupName() : null);
            }
            catch (Exception e) {
            }
            merchantLogService.saveLog(MerchantLogPageEnum.VIDEO_DOMAIN_POOL_SET, MerchantLogTypeEnum.DOMAIN_IMPORT, filedVO,  null,
                   DomainTypeEnum.getNameByCode(merchantDomain.getDomainType()), request);
            return APIResponse.returnSuccess();
        } catch (BusinessException e) {
            log.error("createFrontendDomain, Businessexception:", e);
            return APIResponse.returnFail(e.getMessage());
        } catch (Exception e) {
            log.error("createFrontendDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse<Object> delVideoDomain(FrontendMerchantDomain merchantDomain, HttpServletRequest request) {
        try {
            TyDomain oldDomain = tyDomainMapper.selectById(Long.valueOf(merchantDomain.getId()));
            tyDomainMapper.deleteById(Long.valueOf(merchantDomain.getId()));
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().addAll(Arrays.asList("域名类型","地址", "使用状态"));
            filedVO.getBeforeValues().addAll(Arrays.asList(DomainTypeEnum.getNameByCode(oldDomain.getDomainType()),oldDomain.getDomainName(),
                    DomainEnableEnum.getValueByCode(oldDomain.getStatus())));
            merchantLogService.saveLog(MerchantLogPageEnum.VIDEO_DOMAIN_POOL_SET, MerchantLogTypeEnum.DEL, filedVO,  null,
                    String.valueOf(merchantDomain.getId()), request);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("delFrontendDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse<Object> queryMerchantGroup() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigKey(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX);
        try {
            List<SystemConfig> systemConfigs = this.querySystemConfig(systemConfig);
            List<VideoMerchantGroupDomainPO> videoMerchantGroupDomainPOS = SystemConfig.toVideoPO(systemConfigs);
            if (CollectionUtils.isEmpty(videoMerchantGroupDomainPOS)) {
                return APIResponse.returnSuccess();
            }

            this.TransferMerchantList(videoMerchantGroupDomainPOS);

            return APIResponse.returnSuccess(videoMerchantGroupDomainPOS);
        } catch (Exception e) {
            log.error("queryFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    private void TransferMerchantList(List<VideoMerchantGroupDomainPO> vMerchantGroupDomainPOS){

        List<String> merchantCodes = Lists.newArrayList();
        for (VideoMerchantGroupDomainPO frontendMerchantGroupDomainPO : vMerchantGroupDomainPOS) {
            if (CollectionUtils.isEmpty(frontendMerchantGroupDomainPO.getMerchantCodeSet())) {
                continue;
            }
            merchantCodes.addAll(frontendMerchantGroupDomainPO.getMerchantCodeSet());
        }

        if(CollectionUtils.isNotEmpty(merchantCodes)){
            JSONObject param = new JSONObject();
            param.put("merchantCodes", String.join(",",merchantCodes));
            List<MerchantSimpleVO> merchantList = merchantManageClient.queryMerchantSimpleListByParams(param);

            if(CollectionUtils.isNotEmpty(merchantList)){
                final Map<String,List<MerchantSimpleVO>> merchantMap = merchantList.stream().collect(Collectors.groupingBy(MerchantSimpleVO::getMerchantCode));

                for (VideoMerchantGroupDomainPO frontendMerchantGroupDomainPO : vMerchantGroupDomainPOS) {
                    Set<String> merchantCodesTemp = frontendMerchantGroupDomainPO.getMerchantCodeSet();
                    List<MerchantSimpleVO> merchantListTemp = Lists.newArrayList();
                    if (CollectionUtils.isEmpty(merchantCodesTemp)) {
                        continue;
                    }
                    for (String merchantCode : merchantCodesTemp) {
                        if(CollectionUtils.isNotEmpty(merchantMap.get(merchantCode))){
                            merchantListTemp.addAll(merchantMap.get(merchantCode));
                        }
                    }
                    frontendMerchantGroupDomainPO.setMerchantList(merchantListTemp);
                }

            }

        }
    }

    @Override
    public APIResponse<Object> queryMerchantList(FrontendMerchantGroup frontendMerchantGroup) {


        List<SystemConfig> systemConfigs = this.querySystemConfig(new SystemConfig());
        if (CollectionUtils.isEmpty(systemConfigs)) {
            return APIResponse.returnFail("商户组不存在");
        }
        List<SystemConfig> systemConfigs2 = systemConfigs.stream().filter(s -> s.getId().equals(frontendMerchantGroup.getGroupId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(systemConfigs2)) {
            return APIResponse.returnFail("商户组不存在");
        }
        SystemConfig systemConfig = systemConfigs2.get(0);

        List<String> merchantCodes = Lists.newArrayList();
        JSONObject param = new JSONObject();
        param.put("merchantName", frontendMerchantGroup.getMerchantName());
        List<MerchantSimpleVO> merchantVOList = null;


        if (frontendMerchantGroup.getType() == 1) {
            merchantCodes = getMerchantCodeListBySystemConfig(systemConfig.getRemark());
            if(CollectionUtils.isNotEmpty(merchantCodes)){
                param.put("merchantCodes", String.join(",", merchantCodes));
                merchantVOList = merchantManageClient.queryMerchantSimpleListByParams(param);
            }
        } else {
            merchantVOList = merchantManageClient.queryMerchantSimpleListByParams(param);
            if(CollectionUtils.isEmpty(merchantVOList)){
                return APIResponse.returnSuccess();
            }
            for (SystemConfig config : systemConfigs) {
                merchantCodes.addAll(getMerchantCodeListBySystemConfig(config.getRemark()));
            }
            List<String> finalMerchantCodes = merchantCodes;
            merchantVOList = merchantVOList.stream().filter(m -> m.getMerchantCode() != null && !finalMerchantCodes.contains(m.getMerchantCode())).collect(Collectors.toList());
        }

        if (CollectionUtils.isNotEmpty(merchantVOList)) {
            merchantVOList = merchantVOList.stream()
                    .filter(m -> {
                        if (StringUtils.isNotBlank(frontendMerchantGroup.getMerchantName())) {
                            return m.getMerchantName().contains(frontendMerchantGroup.getMerchantName());
                        }
                        return true;
                    })
                    .filter(m -> {
                        if (frontendMerchantGroup.getAgentLevel() != null && frontendMerchantGroup.getAgentLevel() != 0) {
                            return Objects.equals(m.getAgentLevel(), frontendMerchantGroup.getAgentLevel());
                        }
                        return true;
                    })
                    .filter(m -> {
                        if (StringUtils.isNotBlank(frontendMerchantGroup.getParentCode())) {
                            return Objects.equals(m.getParentCode(), frontendMerchantGroup.getParentCode());
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        return APIResponse.returnSuccess(merchantVOList);
    }

    @Override
    public APIResponse<Object> queryVideoMerchantDomain(VideoMerchantGroupDomainPO videoMerchantGroup) {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigKey(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX);
        List<String> merchantCodes = null;
        try {
            List<SystemConfig> systemConfigs = this.querySystemConfig(systemConfig);
            List<VideoMerchantGroupDomainPO> merchantGroupDomainPOS = SystemConfig.toVideoPO(systemConfigs);
            if (CollectionUtils.isEmpty(merchantGroupDomainPOS)) {
                return APIResponse.returnSuccess();
            }

            if (StringUtils.isNotBlank(videoMerchantGroup.getMerchantName())) {
                merchantCodes = merchantManageClient.getMerchantByName(videoMerchantGroup.getMerchantName());
            }

            List<String> finalMerchantCodes = merchantCodes;
            merchantGroupDomainPOS = merchantGroupDomainPOS.stream()
                    .filter(merchantGroupDomainPO -> {
                        if (StringUtils.isNotBlank(videoMerchantGroup.getGroupName())) {
                            return merchantGroupDomainPO.getGroupName().contains(videoMerchantGroup.getGroupName());
                        }
                        return true;
                    })
                    .filter(merchantGroupDomainPO -> {
                        if (StringUtils.isNotBlank(videoMerchantGroup.getDomainName())) {
                            String videoAllUrl = merchantGroupDomainPO.getVideoAll();
                            String videoExcitingEditingUrl = merchantGroupDomainPO.getVideoExcitingEditing();

                            if (videoMerchantGroup.getDomainType() != null) {
                                if (Objects.equals(videoMerchantGroup.getDomainType(), DomainTypeEnum.VIDEO_ALL.getCode())) {
                                    return StringUtils.isNotBlank(videoAllUrl) && videoAllUrl.contains(videoMerchantGroup.getDomainName());
                                } else {
                                    return StringUtils.isNotBlank(videoExcitingEditingUrl) && videoExcitingEditingUrl.contains(videoMerchantGroup.getDomainName());
                                }
                            }
                            return StringUtils.isNotBlank(videoAllUrl) && videoAllUrl.contains(videoMerchantGroup.getDomainName())
                                    || StringUtils.isNotBlank(videoExcitingEditingUrl) && videoExcitingEditingUrl.contains(videoMerchantGroup.getDomainName());
                        }
                        return true;
                    })
                    .filter(merchantGroupDomainPO -> {
                        if (StringUtils.isNotBlank(videoMerchantGroup.getMerchantName())) {
                            if(CollectionUtils.isEmpty(merchantGroupDomainPO.getMerchantCodeSet())){
                                return false;
                            }
                            if (CollectionUtils.isNotEmpty(finalMerchantCodes)) {
                                return CollectionUtils.containsAny(merchantGroupDomainPO.getMerchantCodeSet(), finalMerchantCodes);
                            }
                        }
                        return true;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            VideoMerchantGroupDomainPO defaultVideoMerchantGroupDomainPO = SystemConfig.getVideoDefaultConfig(systemConfigs);

            if (validate(defaultVideoMerchantGroupDomainPO,videoMerchantGroup,finalMerchantCodes,merchantGroupDomainPOS)) {
                return APIResponse.returnSuccess(Collections.singleton(SystemConfig.getDefaultConfig(systemConfigs)));
            }
            this.TransferMerchantList(merchantGroupDomainPOS);
            return APIResponse.returnSuccess(merchantGroupDomainPOS);
        } catch (Exception e) {
            log.error("queryFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    private boolean validate(VideoMerchantGroupDomainPO defaultVideoMerchantGroupDomainPO,
                             VideoMerchantGroupDomainPO frontendMerchantGroup,
                             List<String> finalMerchantCodes, List<VideoMerchantGroupDomainPO> merchantGroupDomainPOS) {
        if(CollectionUtils.isNotEmpty(merchantGroupDomainPOS)){
            return false;
        }
        if(StringUtils.isBlank(frontendMerchantGroup.getMerchantName())){
            return false;
        }
        if(CollectionUtils.isEmpty(finalMerchantCodes)){
            return false;
        }
        if(defaultVideoMerchantGroupDomainPO == null){
            return false;
        }
        if(frontendMerchantGroup.getDomainType() != null){
            if (StringUtils.isBlank(frontendMerchantGroup.getDomainName())) return true;
            if(frontendMerchantGroup.getDomainType().equals(DomainTypeEnum.VIDEO_ALL.getCode())){
                return StringUtils.isNotBlank(defaultVideoMerchantGroupDomainPO.getVideoAll())
                        && defaultVideoMerchantGroupDomainPO.getVideoAll().contains(frontendMerchantGroup.getDomainName());
            }else{
                return StringUtils.isBlank(defaultVideoMerchantGroupDomainPO.getVideoAll())
                        && defaultVideoMerchantGroupDomainPO.getVideoAll().contains(frontendMerchantGroup.getDomainName());
            }
        }
        if(frontendMerchantGroup.getDomainType() == null){
            return (StringUtils.isNotBlank(defaultVideoMerchantGroupDomainPO.getVideoExcitingEditing())
                    && defaultVideoMerchantGroupDomainPO.getVideoExcitingEditing().contains(frontendMerchantGroup.getDomainName()))
                    ||
                    (StringUtils.isBlank(defaultVideoMerchantGroupDomainPO.getVideoExcitingEditing())
                    && defaultVideoMerchantGroupDomainPO.getVideoExcitingEditing().contains(frontendMerchantGroup.getDomainName()));
        }
        return true;
    }

    @Override
    public APIResponse<Object> replaceDomain(FrontDomainDTO domainDTO) {
        try {
            domainDTO.setNewDomain(domainDTO.getNewDomain().trim());
            domainDTO.setOldDomain(domainDTO.getOldDomain().trim());
            String newDomain = domainDTO.getNewDomain();
            if (newDomain.endsWith(SystemConfigConstant.DOMAIN_COLON)) newDomain = newDomain.substring(0, newDomain.length() - 1);
            JSONObject param = new JSONObject();
            param.put("oldDomain", domainDTO.getOldDomain());
            APIResponse apiResponse = merchantApiNewClient.queryMerchantVideoDomain(param);
            if (!apiResponse.getCode().equals("0000")) {
                return APIResponse.returnFail(apiResponse.getMsg());
            }
            if(Objects.isNull(apiResponse.getData()) || (int) apiResponse.getData() == 0){
                return APIResponse.returnFail("暂无商户组使用此域名，请重试！");
            }
            int count = (int) apiResponse.getData();
            merchantApiNewClient.updateNewVideoDomainAndChangeSystemConfig(domainDTO.getOldDomain(), null, newDomain);

            // 添加操作日志
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            List<String> fieldName = new ArrayList<>();
            fieldName.add(MerchantLogTypeEnum.ONE_KEY_REPLACE.getRemark());
            vo.setFieldName(fieldName);
            List<String> beforeValues = new ArrayList<>();
            beforeValues.add(domainDTO.getOldDomain());
            vo.setBeforeValues(beforeValues);
            List<String> afterValues = new ArrayList<>();
            afterValues.add(newDomain);
            vo.setAfterValues(afterValues);
            vo.setDomainType(DomainTypeEnum.VIDEO_ALL.getCode());

            MerchantLogTypeEnum merchantLogTypeEnum = MerchantLogTypeEnum.ONE_KEY_REPLACE;
            merchantLogService.saveLog(MerchantLogPageEnum.VIDEO_DOMAIN_SET,
                    merchantLogTypeEnum, vo,
                    MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, "0",
                    domainDTO.getUserName(),  "视频域名设置 - 一键切换",
                    null, null, "zs", domainDTO.getIp(),
                    null, null, DomainTypeEnum.VIDEO_ALL.getCode());
            String message = "发现" + count + "个商户使用此域名("+domainDTO.getOldDomain()+")，已处理！";
            log.info(message);
            return APIResponse.returnSuccess(message,message);
        } catch (Exception e) {
            log.error("delFrontendDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    public void replaceVideoDomainByMerchant(VideoDomainMerchantDTO domainDTO) {
        try {
            merchantApiNewClient.replaceVideoDomainByMerchant(domainDTO);
        } catch (Exception e) {
            log.error("replaceDomainByMerchant, exception:", e);
        }
    }

    @Override
    public APIResponse<Object> clearCache() {
        try {
            merchantApiClient.kickoutMerchant(null);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("clearCache, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse<Object> createMerchantGroup(VideoMerchantGroup merchantGroup, HttpServletRequest request) {
        try {

            SystemConfig sourceSystemConfig = new SystemConfig();
            sourceSystemConfig.setConfigKey(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX + merchantGroup.getGroupName());
            List<SystemConfig> systemConfigList = this.querySystemConfig(sourceSystemConfig);
            log.info("querySystemConfig.result: {}", systemConfigList);
            if (CollectionUtils.isNotEmpty(systemConfigList) && systemConfigList.size() >= 1) {
                return APIResponse.returnFail("视频商户组名称重复！");
            }
            int result = merchantApiNewClient.createSystemConfig(merchantGroup.toSystemConfig());
            if (result == 1) {
                String prefix = "";
                /*时间类型  1为分钟 2为小时 3为日  4为月*/
                if (merchantGroup.getTimeType() == 1) {
                    prefix = "分钟";
                }else if (merchantGroup.getTimeType() == 2){
                    prefix = "小时";
                }else if (merchantGroup.getTimeType() == 3){
                    prefix = "日";
                }else if (merchantGroup.getTimeType() == 4){
                    prefix = "月";
                }
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().addAll(Arrays.asList("域名防护商户组","详细商户","切换频率","域名提醒设置","启用/禁用"));
                vo.getAfterValues().addAll(Arrays.asList(merchantGroup.getGroupName(),JSON.toJSONString(merchantGroup.getMerchantCodeList()),
                        merchantGroup.getTimes() + prefix, "低于" + merchantGroup.getAlarmNum(), "禁用"));
                vo.setMerchantName(merchantGroup.getGroupName());
                merchantLogService.saveLog(MerchantLogPageEnum.VIDEO_DOMAIN_SET, MerchantLogTypeEnum.SAVE, vo,  null, merchantGroup.getGroupName(), request);
                return APIResponse.returnSuccess(result);
            }
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        } catch (Exception e) {
            log.error("createFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public APIResponse<Object> updateMerchantGroup(VideoMerchantGroup merchantGroup, HttpServletRequest httpRequest) {
        try {
            SystemConfig sourceSystemConfig = new SystemConfig();
            List<SystemConfig> systemConfigList = this.querySystemConfig(sourceSystemConfig);
            if (CollectionUtils.isNotEmpty(systemConfigList) && systemConfigList.stream()
                    .anyMatch(s -> StringUtils.equals(s.getConfigKey(), SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX + merchantGroup.getGroupName()) && !Objects.equals(s.getId(), merchantGroup.getGroupId()))) {
                return APIResponse.returnFail("商户组名称重复");
            }

            systemConfigList = systemConfigList.stream().filter(s -> s.getId().equals(merchantGroup.getGroupId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(systemConfigList)) {
                return APIResponse.returnFail("商户组不存在");
            }
            Integer type = merchantGroup.getType();
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.setMerchantName(merchantGroup.getGroupName());
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_INFO_STATUS;

            List<VideoMerchantGroupDomainPO> videoMerchantGroupDomainPOS = SystemConfig.toVideoPO(systemConfigList);
            VideoMerchantGroupDomainPO po = new VideoMerchantGroupDomainPO();
            if(CollectionUtils.isNotEmpty(videoMerchantGroupDomainPOS)) po = videoMerchantGroupDomainPOS.get(0);

            //0 选择商户  2 启用/禁用 3切换频率
            if(type == 0) {
                typeEnum = MerchantLogTypeEnum.MERCHANT_SELECT;
                filedVO.setFieldName(Arrays.asList("商户编码","商户名称","商户类型"));
                if (CollectionUtils.isNotEmpty(videoMerchantGroupDomainPOS)) {
                    this.TransferMerchantList(videoMerchantGroupDomainPOS);
                    po = videoMerchantGroupDomainPOS.get(0);
                    if(CollectionUtils.isNotEmpty(po.getMerchantList())) {
                        po.getMerchantList().forEach( m -> filedVO.getBeforeValues().add(m.getMerchantCode() + StringPool.AMPERSAND + m.getMerchantName() +
                                StringPool.AMPERSAND + AgentLevelEnum.getRemarkByCode(m.getAgentLevel())));
                    }
                }
            }else if(type == 2) {
                filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("closeAndOpen"));
                filedVO.getBeforeValues().add(1==po.getStatus()?"开":"关");
                filedVO.getAfterValues().add(1==merchantGroup.getStatus()?"开":"关");
            }else if (type == 3) {
                typeEnum = MerchantLogTypeEnum.EDIT;
                if(!po.getGroupName().equals(merchantGroup.getGroupName())) {
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("groupName"));
                    filedVO.getBeforeValues().add(po.getGroupName());
                    filedVO.getAfterValues().add(merchantGroup.getGroupName());
                }
            }
            sourceSystemConfig = systemConfigList.get(0);
            final int sourceStatus = Integer.parseInt(sourceSystemConfig.getConfigValue());

            sourceSystemConfig.setConfigKey(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX + merchantGroup.getGroupName());
            sourceSystemConfig.setConfigValue(merchantGroup.getStatus().toString());
            String remark = sourceSystemConfig.getRemark();
            // status 打开的时候要校验h5,pc配置
            if (merchantGroup.getStatus() == 1) {
                if (StringUtils.isBlank(getVideoAllBySystemConfig(remark))) {
                    return APIResponse.returnFail("请配置全量域名再启用商户组");
                }
                if (StringUtils.isBlank(getVideoExcitingEditingBySystemConfig(remark))) {
                    return APIResponse.returnFail("请配置精彩剪辑域名再启用商户组");
                }
            }
            JSONObject jsonObject;
            if (StringUtils.isNotBlank(remark)) {
                jsonObject = JSONObject.parseObject(remark, JSONObject.class);
            } else {
                jsonObject = new JSONObject();
            }
            if(CollectionUtils.isNotEmpty(merchantGroup.getMerchantCodeList())){
                jsonObject.put("merchantCodeList", String.join(",", merchantGroup.getMerchantCodeList()));
            }
            sourceSystemConfig.setRemark(JSON.toJSONString(jsonObject));
            int result = merchantApiNewClient.updateSystemConfigByDomain(sourceSystemConfig);

            // 刚操作打开
            if((sourceStatus == 0 && merchantGroup.getStatus() == 1)||(sourceStatus == 1 && merchantGroup.getStatus() == 0)){
                // 同时改一下商户的配置
                VideoDomainMerchantDTO request = VideoDomainMerchantDTO.builder().videoAll(getVideoAllBySystemConfig(remark)).videoExcitingEditing(getVideoExcitingEditingBySystemConfig(remark)).merchantCodeList(this.getMerchantCodeListBySystemConfig(remark)).build();
                this.replaceVideoDomainByMerchant(request);
            }
            if(result > 0){
                merchantApiClient.kickoutMerchant(null);
            }
            if(type == 0) {
                List<SystemConfig> configList = this.querySystemConfig(new SystemConfig());
                configList = configList.stream().filter(s -> s.getId().equals(merchantGroup.getGroupId())).collect(Collectors.toList());
                List<VideoMerchantGroupDomainPO> pos = SystemConfig.toVideoPO(configList);
                if(CollectionUtils.isNotEmpty(pos)) {
                    this.TransferMerchantList(pos);
                    List<MerchantSimpleVO> merchantList = pos.get(0).getMerchantList();
                    if(CollectionUtils.isNotEmpty(merchantList)) {
                        merchantList.forEach( m -> filedVO.getAfterValues().add(m.getMerchantCode() + StringPool.AMPERSAND + m.getMerchantName() +
                                StringPool.AMPERSAND + AgentLevelEnum.getRemarkByCode(m.getAgentLevel())));
                    }
                }
            }
            merchantLogService.saveLog(MerchantLogPageEnum.VIDEO_DOMAIN_SET, typeEnum, filedVO,  null,
                    merchantGroup.getGroupName() + StringPool.AMPERSAND + merchantGroup.getGroupId(), httpRequest);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("updateFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    private SystemConfig getSystemConfigById(Long groupId) {
        List<SystemConfig> systemConfigList = this.querySystemConfig(SystemConfig.builder().id(groupId).build());
        if (CollectionUtils.isEmpty(systemConfigList)) {
            return null;
        }
        return systemConfigList.get(0);
    }

    private JSONObject getRemarkBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            remarkJ = new JSONObject();
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ;
    }

    private String getVideoAllBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return "";
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ.getString(SystemConfigConstant.VIDEO_COLUMN_VIDEO_ALL) != null ?remarkJ.getString(SystemConfigConstant.VIDEO_COLUMN_VIDEO_ALL) :"";
    }

    private String getVideoExcitingEditingBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return "";
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ.getString(SystemConfigConstant.VIDEO_COLUMN_VIDEO_EXCITING_EDITING) != null ?remarkJ.getString(SystemConfigConstant.VIDEO_COLUMN_VIDEO_EXCITING_EDITING) :"";
    }

    private List<String> getMerchantCodeListBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return Lists.newArrayList();
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        String merchantCodeStr = remarkJ.getString("merchantCodeList");
        return StringUtils.isBlank(merchantCodeStr) ?
                Lists.newArrayList() : Arrays.stream(merchantCodeStr.split(",")).collect(Collectors.toList());
    }

    private List<SystemConfig> querySystemConfig(SystemConfig systemConfig) {
        List<SystemConfig> result = merchantApiNewClient.querySystemConfig(systemConfig);
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }
        return result.stream().filter(s -> s.getConfigKey().startsWith(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX)).collect(Collectors.toList());
    }

}
