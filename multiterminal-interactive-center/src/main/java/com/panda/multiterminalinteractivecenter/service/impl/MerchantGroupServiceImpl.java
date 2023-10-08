package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.config.NacosThirdEnableConfig;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.entity.*;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.*;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import com.panda.multiterminalinteractivecenter.service.MerchantGroupServiceTransfer;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.BeanCopierUtils;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.vo.*;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName MerchantGroupServiceImpl
 * @Description 商户组服务类
 * @Author ifan
 * @Date 2022/7/ 11
 */

@Slf4j
@RefreshScope
@Service("ty")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantGroupServiceImpl implements MerchantGroupServiceTransfer {

    private final MerchantGroupMapper merchantGroupMapper;

    private final DomainMapper domainMapper;

    private final MerchantApiClient merchantApiClient;

    private final MerchantManageClient merchantManageClient;

    private final MerchantLogService merchantLogService;

    private final DomainGroupRelationMapper domainGroupRelationMapper;

    private final DomainProgramMapper domainProgramMapper;

    private final DomainGroupMapper domainGroupMapper;

    @Value("${nacos.api.url:null}")
    private String nacosUrl;

    @Value("${nacos.api.namespace:null}")
    private String nacosNameSpace;

    private final NacosThirdEnableConfig nacosThirdEnableConfig;


    @Override
    public List<ThirdMerchantVo> getMerchantList() {
        return null;
    }

    @Override
    public void sendDJMsg(String merchantCode, Integer domainType, String url,Integer changeType) {}
    @Override
    public void sendCPMsg(String merchantCode, Integer domainType, String url,Boolean isVip,String ipArea,int changeType) {}

    @Override
    public List<ThirdMerchantVo> getMerchantList(Integer code) {
        return null;
    }

    @Override
    public List<ThirdMerchantVo> getTblMerchantList(Integer code) {
        return null;
    }

    @Override
    public APIResponse getMerchantGroup(Integer merchantGroupCode) {
        return null;
    }

    /**
     * 查询商户组信息
     *
     * @param merchantGroupPO
     * @return
     */
    @Override
    public List<MerchantGroupVO> selectMerchantGroup(MerchantGroupVO merchantGroupPO) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        if (!StrUtil.isBlank(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        List<MerchantGroupPO> list = merchantGroupMapper.selectMerchantGroup(groupPO);
        List<MerchantGroupVO> groupList = Lists.newArrayList();
        list.stream().forEach(po -> {
            MerchantGroupVO vo = new MerchantGroupVO();
            vo.setId(String.valueOf(po.getId()));
            vo.setTab(merchantGroupPO.getTab());
            BeanUtils.copyProperties(po, vo);
            if (!Objects.equals(vo.getId(), "0")) {
                List<?> resultList = merchantManageClient.queryMerchantListByGroup(vo.getId(), null);
                if (CollectionUtil.isNotEmpty(resultList)) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<MerchantPO> merchantList = mapper.convertValue(resultList, new TypeReference<List<MerchantPO>>() {
                    });
                   List<MerchantResultVO> merchantGroupInfos = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(merchantList)) {
                        merchantList.forEach(merchantPO -> {
                            MerchantResultVO resultVO = new MerchantResultVO();
                            BeanCopierUtils.copyProperties(merchantPO,resultVO);
                            merchantGroupInfos.add(resultVO);
                        });

                        vo.setMerchantList(merchantGroupInfos);
                    }
                }
            }

            if (Objects.isNull(vo.getProgramId())) {
                //查询商户组默认方案
                DomainProgramVO domainProgramVO = merchantGroupMapper.findProgram(vo);
                vo.setProgramId(domainProgramVO.getId());
                vo.setProgramName(domainProgramVO.getProgramName());

                //修改商户组默认方案初始化
                MerchantGroupPO merchantGroupPO1 = new MerchantGroupPO();
                merchantGroupPO1.setId(Long.valueOf(po.getId()));
                merchantGroupPO1.setProgramId(domainProgramVO.getId());
                merchantGroupMapper.updateMerchantGroup(merchantGroupPO1);
            }

            DomainVO domainVo = new DomainVO();
            domainVo.setMerchantGroupId(po.getId().toString());
            domainVo.setEnable(1);
            List<TDomain> list1 = domainMapper.selectAll(domainVo);
            if (CollectionUtil.isNotEmpty(list1)) {
                vo.setDomain(list1.get(0).getDomainName());
            }
            groupList.add(vo);
        });

        return groupList;
    }

    /**
     * 清除商户缓存
     *
     * @param merchantTag
     * @param containsType
     * @param containsStr
     * @param merchantCode
     * @param parentCode
     * @return
     */
    public APIResponse cleanMerchant(Integer merchantTag, Integer containsType, String containsStr, String merchantCode, String parentCode) {
        Object obj = merchantApiClient.kickoutMerchant(merchantCode);
        log.info("三端/merchant/group接口：踢出商户:" + obj);
        return APIResponse.returnSuccess();
    }

    /**
     * 手动切换域名
     *
     * @param domainVO
     * @param userName
     * @param ip
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse updateMerchantDomain(DomainVO domainVO, String userName, String ip) {
        try {
            int merchantCount = merchantManageClient
                    .queryMerchantCountByGroup(domainVO.getMerchantGroupId(), 1);
            if ( merchantCount == 0) {
                return APIResponse.returnFail("商户组未查询到商户信息");
            }

            // 获取新的域名信息
            List<DomainVO> domainVOS = domainVO.getConfig();

            JSONObject param = new JSONObject();
            param.put("merchantGroupId",domainVO.getMerchantGroupId());
            param.put("domainVOS",domainVOS);

            int resultCount = merchantManageClient.updateMerchantDomainByMerchantCodes(param);

            log.info("手动切换调用manage服务更新商户域名，数量为：{}，清除缓存，数量为：{}",resultCount,merchantCount);

            for (DomainVO domainVO1 : domainVOS) {
                if (StrUtil.isBlank(domainVO1.getNewDomainName())) {
                    continue;
                }
                //重置原域名enable =2, status =1
                domainMapper.reset2Domain(domainVO1.getOldDomainId());

                //更新新域名 enable = 1, status = 1
                domainMapper.updateDomainEnableTimeById(Long.valueOf(domainVO1.getNewDomainId()), System.currentTimeMillis());

                // 记录手动切换域名日志
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                List<String> fieldName = new ArrayList<>();
                fieldName.add(MerchantLogTypeEnum.MANUAL_SWITCH.getRemark());
                vo.setFieldName(fieldName);
                List<String> beforeValues = new ArrayList<>();
                beforeValues.add(JSON.toJSONString(domainVO1.getOldDomainName()));
                vo.setBeforeValues(beforeValues);
                List<String> afterValues = new ArrayList<>();
                afterValues.add(JSON.toJSONString(domainVO1.getNewDomainName()));
                vo.setAfterValues(afterValues);
                vo.setDomainType(domainVO1.getDomainType());
                String merchantName = "SYSTEM";
                if (StringUtils.isNotEmpty(domainVO.getMerchantGroupId())) {
                    String tabStr = org.apache.commons.lang.StringUtils.isNotBlank(domainVO.getTab()) ? domainVO.getTab() : TabEnum.TY.getName();
                    MerchantGroupPO merchantGroupPO = merchantGroupMapper.selectMerchantGroupById(Long.valueOf(domainVO.getMerchantGroupId()), tabStr);
                    if (merchantGroupPO != null) {
                        merchantName = merchantGroupPO.getGroupName();
                    }
                }
                merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP,
                        MerchantLogTypeEnum.MANUAL_SWITCH, vo,
                        MerchantLogConstants.MERCHANT_IN, "0",
                        userName, merchantName,
                        merchantName, merchantName + StringPool.AMPERSAND + domainVO.getMerchantGroupId(), "zs", ip,
                        null,null,domainVO1.getDomainType());
            }
        } catch (Exception ex) {
            log.error("手动切换域名失败:", ex);
            return APIResponse.returnFail("手动切换域名失败!");
        }
        return APIResponse.returnSuccess();
    }

    /**
     * 查询商户组下的域名
     *
     * @param merchantDomainVO
     * @return
     */
    @Override
    public APIResponse getMerchantGroupDomainRelationDataList(MerchantDomainVO merchantDomainVO) {
        try {
            List<MerchantDomainVO> merchantDomainVOS = domainMapper.getMerchantGroupDomainRelationDataList(merchantDomainVO);

            //初始化没有手动切换域名数据
            if (CollectionUtil.isEmpty(merchantDomainVOS)) {
                // 域名组为空
                merchantDomainVOS = domainMapper.getDomainGroupByProgramId(merchantDomainVO.getProgramId(),merchantDomainVO.getTab());
            }else{
                // 查询包含域名组 但数据为空的数据
                MerchantDomainVO merchantDomainVOTemplate = merchantDomainVOS.get(0);
                List<Long> domainGroupIdList= merchantDomainVOS.stream().map(MerchantDomainVO::getDomainGroupId).distinct().collect(Collectors.toList());
                List<Map<String,Object>> domainGroupIdList1 = domainMapper.getDomainGroupListByMerchantCode(merchantDomainVO,domainGroupIdList);
                if(CollectionUtils.isNotEmpty(domainGroupIdList1)){
                    for (Map<String,Object> domainGroup : domainGroupIdList1) {
                        merchantDomainVOS.add(
                                MerchantDomainVO.builder()
                                        .domainGroupId( Objects.isNull(domainGroup.get("domainGroupId")) ? null :Long.valueOf(domainGroup.get("domainGroupId").toString()))
                                        .areaName(Objects.isNull(domainGroup.get("areaName")) ? "" : domainGroup.get("areaName").toString())
                                        .programId(merchantDomainVO.getProgramId())
                                        .merchantGroupId(merchantDomainVOTemplate.getMerchantGroupId())
                                        .groupCode(merchantDomainVOTemplate.getGroupCode())
                                        .groupName(merchantDomainVOTemplate.getGroupName())
                                        .groupType(merchantDomainVOTemplate.getGroupType())
                                        .tab(merchantDomainVOTemplate.getTab())
                                        .build());
                    }
                }
            }

            //去重
            merchantDomainVOS = merchantDomainVOS.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(c ->
                            String.format("%s_%s_%s", c.getAreaName(), c.getDomainType(), c.getDomainName())))), ArrayList::new));

            List<MerchantDomainVO> result = extractedMerchantDomainList(merchantDomainVOS,merchantDomainVO);
            //手动切换选择域名时过滤域名类型为空的数据
            List<MerchantDomainVO>  merchantDomainVOList =
                    result.stream()
                            .filter(e-> Objects.nonNull(e.getDomainType()))
                            .sorted(
                                    Comparator.comparing(MerchantDomainVO::getAreaName,Comparator.reverseOrder())
                                            .thenComparing(MerchantDomainVO::getDomainType)
                            )
                    .collect(Collectors.toList());

            return APIResponse.returnSuccess(merchantDomainVOList);


        } catch (Exception ex) {
            log.error("获取商户组下的域名，失败:", ex);
            return APIResponse.returnFail("获取商户组下的域名失败");
        }

    }

    private List<MerchantDomainVO> extractedMerchantDomainList(List<MerchantDomainVO> merchantDomainVOS, MerchantDomainVO merchantDomainVO) {
        List<MerchantDomainVO> result = Lists.newArrayList();

        DomainProgram domainProgram = domainProgramMapper.selectById(merchantDomainVO.getProgramId());

        Map<String,List<MerchantDomainVO>> domainVOMap = merchantDomainVOS.stream().filter(m->StringUtils.isNotBlank(m.getAreaName())).collect(Collectors.groupingBy(MerchantDomainVO::getAreaName));

        for(Map.Entry<String,List<MerchantDomainVO>> entry : domainVOMap.entrySet()){
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.H5.getCode(),domainProgram.getH5PushDomainNum());
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.PC.getCode(),domainProgram.getPcPushDomainNum());
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.APP.getCode(),domainProgram.getApiPushDomainNum());
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.IMAGE.getCode(),domainProgram.getImgPushDomainNum());
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.OTHER.getCode(),1L);
            result.addAll(entry.getValue());
        }
        return result;
    }

    /**
     * 初始化没有默认数据无法手动切换数据
     * @param areaName
     * @param domainVOS
     * @param domainType
     */
    private void buildDomainVOS(String areaName, List<MerchantDomainVO> domainVOS, int domainType,Long pushDomainNum) {

        List<MerchantDomainVO> result = domainVOS.stream().filter(domainVo-> Objects.equals(domainVo.getDomainType(), domainType)).collect(Collectors.toList());
        if(result.size()== pushDomainNum){}
        else if(result.size() > pushDomainNum){
            // 删
            domainVOS.removeIf(domainVo-> Objects.equals(domainVo.getDomainType(), domainType));
            for (int i = 0; i < pushDomainNum; i++) {
                domainVOS.add(result.get(i));
            }
        }else{
            // 补
            MerchantDomainVO domainVOTemp = domainVOS.get(0);
            MerchantDomainVO domainVO =
                    MerchantDomainVO
                            .builder()
                            .merchantGroupId(domainVOTemp.getMerchantGroupId())
                            .domainType(domainType)
                            .domainGroupId(domainVOTemp.getDomainGroupId())
                            .groupCode(domainVOTemp.getGroupCode())
                            .groupName(domainVOTemp.getGroupName())
                            .programId(domainVOTemp.getProgramId())
                            .areaName(areaName)
                            .build();
            for (int i = result.size(); i < pushDomainNum; i++) {
                domainVOS.add(domainVO);
            }
        }
    }


    /**
     * 根据商户分组类型 域名类型查询域名名称
     *
     * @param domainGroupVO
     * @return
     */
    @Override
    public APIResponse getDomainNameList(DomainGroupVO domainGroupVO) {
        try {
            return APIResponse.returnSuccess(domainMapper.getDomainNameList(domainGroupVO));
        } catch (Exception ex) {
            log.error("获取域名名称，失败:", ex);
            return APIResponse.returnFail("获取域名名称失败");
        }
    }

    /**
     * 通过商户分组类型 域名类型 区域选择域名名称
     *
     * @return
     */
    @Override
    public APIResponse<?> selectDomain(Integer page, Integer size, String domainName, Integer domainType, Long domainGroupId,
                                       String domainGroupName, Long lineCarrierId, Integer groupType, Boolean used, String tab,Long programId,String areaName) {
        try {
            if (page == null) page = 1;
            if (size == null) size = 20;
            if (StringUtils.isNotBlank(tab)) tab = "ty";
            PageHelper.startPage(page, size, true);

            List<TyDomain> list = domainMapper.selectDomainList(page, size, domainName, domainType, domainGroupId, domainGroupName, lineCarrierId, groupType, used, tab);
            if (CollectionUtil.isNotEmpty(list)) {
                List<TyDomain.DomainGroupDTO> domainGroupDetailList;
                for (TyDomain tyDomain : list) {
                    domainGroupDetailList = Lists.newArrayList();
                    String domainGroupDetail = tyDomain.getDomainGroupDetail();
//                    String allDomainGroupDetail = tyDomain.getAllDomainGroupDetail();
                    if (StringUtils.isNotBlank(domainGroupDetail)) {
                        List<TyDomain.DomainGroupDTO> finalDomainGroupDetailList = domainGroupDetailList;
                        Arrays.stream(domainGroupDetail.split(",")).forEach(
                                domainGroupDetailO -> {
                                    String[] domainGroupStr = domainGroupDetailO.split(":");
                                    finalDomainGroupDetailList.add(TyDomain.DomainGroupDTO.builder().domainGroupId(domainGroupStr[0]).domainGroupName(domainGroupStr[1]).build());
                                }
                        );
                    }
                    tyDomain.setDomainGroupDTOList(domainGroupDetailList);
//                    if (StringUtils.isNotBlank(allDomainGroupDetail)) {
//                        String [] arr = allDomainGroupDetail.split(",");
//                        tyDomain.setNotRecommended(arr.length != 1);
//                    }else{
//                        tyDomain.setNotRecommended(false);
//                    }
                }
            }

            PageInfo<TyDomain> poList = new PageInfo<>(list);
            return APIResponse.returnSuccess(poList);
        } catch (Exception ex) {
            log.error("选择域名，失败:", ex);
            return APIResponse.returnFail("选择域名失败");
        }
    }

    /**
     * 修改商户组信息
     *
     * @param merchantGroupPO
     * @return
     */
    @Override
    public APIResponse updateMerchantGroup(MerchantGroupVO merchantGroupPO, HttpServletRequest request) {

        if(merchantGroupPO.getProgramId()!=null){
            int resultCode = domainProgramMapper.hasDefaultDomainGroup(merchantGroupPO.getProgramId(),merchantGroupPO.getTab());
            if(resultCode==0){
                return APIResponse.returnFail("该域名方案没有默认域名组，不可选则");
            }
        }

        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);

        // 转化：group_code
        MerchantGroupEnum merchantGroupEnum = MerchantGroupEnum.getInstance(groupPO.getGroupType());

        groupPO.setGroupCode(merchantGroupEnum.getCode());

        if (!StrUtil.isBlank(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT;
        MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
        Integer type = merchantGroupPO.getType();
        List<String> afters = new ArrayList<>();
        List <String> before = new ArrayList<>();
        MerchantGroupPO oldPO = merchantGroupMapper.selectMerchantGroupById(groupPO.getId(), groupPO.getTab());
        if (groupPO.getTimes() != null || groupPO.getTimeType() != null || groupPO.getUpdateTime() != null || groupPO.getGroupName() != null || groupPO.getStatus() != null) {
            merchantGroupMapper.updateMerchantGroup(groupPO);
        }
        type = Objects.isNull(type) ? 3 : type;
        if(type == 0) {
            filedVO.setFieldName(Arrays.asList("商户编码","商户名称","商户类型"));
            typeEnum = MerchantLogTypeEnum.MERCHANT_SELECT;
            List<?> resultList = merchantManageClient.queryMerchantListByGroup(groupPO.getId().toString(), null);
            if (CollectionUtil.isNotEmpty(resultList)) {
                ObjectMapper mapper = new ObjectMapper();
                List<MerchantPO> merchantList = mapper.convertValue(resultList, new TypeReference<List<MerchantPO>>() {
                });
                if (CollectionUtil.isNotEmpty(merchantList)) {
                    before.add(merchantList.stream().map(m -> m.getMerchantCode() + StringPool.AMPERSAND + m.getMerchantName()).collect(Collectors.joining(",")));
                }
            }
        }
        try {
            if (CollectionUtil.isNotEmpty(merchantGroupPO.getMerchantCodes())) {
                merchantManageClient.updateMerchantGroupId(merchantGroupPO.getId(), groupPO.getGroupCode(), merchantGroupPO.getMerchantCodes());
            }
        } catch (Exception e) {
            log.error("updateMerchantGroup error:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        // 0 选择商户 1 切换方案 2 启用/禁用 3切换频率
        if(type == 0) {
            List<?> resultList = merchantManageClient.queryMerchantListByGroup(groupPO.getId().toString(), null);
            if (CollectionUtil.isNotEmpty(resultList)) {
                ObjectMapper mapper = new ObjectMapper();
                List<MerchantPO> merchantList = mapper.convertValue(resultList, new TypeReference<List<MerchantPO>>() {
                });
                if (CollectionUtil.isNotEmpty(merchantList)) {
                    afters.add(merchantList.stream().map(m -> m.getMerchantCode() + StringPool.AMPERSAND + m.getMerchantName()).collect(Collectors.joining(",")));
                }
            }
        }else if(type == 1 && merchantGroupPO.getProgramId() != null) {
            filedVO.getFieldName().add(MerchantLogTypeEnum.CUT_PROGRAM.getRemark());
            typeEnum = MerchantLogTypeEnum.CUT_PROGRAM;
            DomainProgram old = domainProgramMapper.selectById(oldPO.getProgramId());
            DomainProgram newPO = domainProgramMapper.selectById(merchantGroupPO.getProgramId());
            before.add(old != null ? old.getProgramName() : null);
            afters.add(newPO != null ? newPO.getProgramName() : null);
        }else if(type == 2) {
            filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
            typeEnum = MerchantLogTypeEnum.EDIT_INFO_STATUS;
            before.add(1==oldPO.getStatus()?"开":"关");
            afters.add(1==merchantGroupPO.getStatus()?"开":"关");
        }else if(type == 3) {
            if(!oldPO.getTimes().equals(merchantGroupPO.getTimes()) || !oldPO.getTimeType().equals(merchantGroupPO.getTimeType())) {
                filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("changeNum"));
                before.add("每" + oldPO.getTimes() + getAfterOp(oldPO.getTimeType()));
                afters.add("每" + merchantGroupPO.getTimes() + getAfterOp(merchantGroupPO.getTimeType()));
            }
            if(!oldPO.getGroupName().equals(merchantGroupPO.getGroupName())) {
                filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("groupName"));
                before.add(oldPO.getGroupName());
                afters.add(merchantGroupPO.getGroupName());
            }
        }
        filedVO.setBeforeValues(before);
        filedVO.setAfterValues(afters);
        filedVO.setMerchantName(oldPO.getGroupName());
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, typeEnum, filedVO,  null,
                groupPO.getGroupName() + StringPool.AMPERSAND + groupPO.getId(), request);
        log.info("updateMerchantGroup:end ");
        // 踢出商户 , 清除缓存
        merchantApiClient.kickoutMerchant(null);
        return APIResponse.returnSuccess();
    }

    private String getAfterOp(Integer timeType){
        String afterOp="";
        switch(timeType){
            case 1 :
                afterOp ="分钟";
                break;
            case 2:
                afterOp ="小时";
                break;
            case 3 :
                afterOp ="日";
                break;
            case 4:
                afterOp ="月";
                break;
        }

        return afterOp;
    }

    /**
     * 删除商户组信息
     *
     * @param merchantGroupPO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse deleteMerchantGroup(MerchantGroupVO merchantGroupPO, HttpServletRequest request) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        if (StringUtils.isNotEmpty(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        MerchantGroupPO oldPO = merchantGroupMapper.selectMerchantGroupById(groupPO.getId(), groupPO.getTab());
        int num = merchantGroupMapper.deleteMerchantGroup(groupPO);
        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(oldPO, new MerchantGroupPO());
        filedVO.setMerchantName(oldPO.getGroupName());
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, MerchantLogTypeEnum.DEL, filedVO,  null,
                oldPO.getGroupName() + StringPool.AMPERSAND + oldPO.getId(), request);
        if (num > 0) {
            domainMapper.updateDomainByMerchantGroupId(groupPO.getId().toString());
            merchantManageClient.updateMerchantGroupIdDefult(merchantGroupPO.getId());
            return APIResponse.returnSuccess();
        }
        log.info("deleteMerchantGroup:" + num);
        return APIResponse.returnFail("删除商户组失败！");
    }

    /**
     * 根据商户分组类型查询商户信息
     *
     * @param merchantVO
     * @return
     */
    public APIResponse selectList(MerchantVO merchantVO) {

        List<?> resultList = merchantManageClient.selectList(merchantVO);

        if (CollectionUtil.isEmpty(resultList)) {
            return APIResponse.returnFail("暂无商户数据");
        }
        ObjectMapper mapper = new ObjectMapper();
        List<MerchantVO> merchantVOtList = mapper.convertValue(resultList, new TypeReference<List<MerchantVO>>() {
        });
        return APIResponse.returnSuccess(merchantVOtList);
    }

    /**
     * 创建商户组信息
     *
     * @param merchantGroupPO
     * @return
     */
    @Override
    public APIResponse createMerchantGroup(TMerchantGroup merchantGroupPO, HttpServletRequest request) {

        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);

        // 转化：group_code
        MerchantGroupEnum merchantGroupEnum = MerchantGroupEnum.getInstance(groupPO.getGroupType());
        if (merchantGroupEnum == null) {
            return APIResponse.returnFail("商户分库组必传");
        }

        groupPO.setGroupCode(merchantGroupEnum.getCode());

        int num = merchantGroupMapper.createMerchantGroup(groupPO);

        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(new MerchantGroupPO(), groupPO);
        filedVO.setMerchantName(merchantGroupPO.getGroupName());
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, MerchantLogTypeEnum.SAVE, filedVO,  null,
                merchantGroupPO.getGroupName(), request);
        log.info("createMerchantGroup num:" + num);
        if (num > 0 && merchantGroupPO.getMerchantCodes() != null) {
            num = merchantManageClient.updateMerchantGroupId(String.valueOf(groupPO.getId()), groupPO.getGroupCode(), merchantGroupPO.getMerchantCodes());
            log.info("createMerchantGroup num1:" + num + " merchantGroupPO.getId()，" + merchantGroupPO.getId() + "---" + merchantGroupPO.getMerchantCodes());
        }
        return APIResponse.returnSuccess(groupPO);
    }

    @Override
    public APIResponse delProgramRelationByDomainGroupId(MerchantGroupVO merchantGroupVO, HttpServletRequest request) {
        int num = domainGroupRelationMapper.delProgramRelationByDomainGroupId(merchantGroupVO.getProgramId(),merchantGroupVO.getDomainGroupId());
        if (num > 0) {
            DomainGroup domainGroup = domainGroupMapper.selectById(merchantGroupVO.getDomainGroupId());
            DomainProgram program = domainProgramMapper.selectById(merchantGroupVO.getProgramId());
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().add("删除");
            filedVO.getBeforeValues().add(domainGroup.getDomainGroupName());
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_PROGRAM_INFO, MerchantLogTypeEnum.DEL, filedVO,  null,
                    program.getProgramName() + StringPool.AMPERSAND + merchantGroupVO.getProgramId(), request);
            return APIResponse.returnSuccess();
        }
        log.info("delProgramRelationByDomainGroupId:" + num);
        return APIResponse.returnFail("删除方案关系数据失败！");
    }

    @Override
    public APIResponse getMerchantGroupDomain(MerchantGroupDomainVO req) {
        List<MerchantGroupDomainVO> list = merchantGroupMapper.getMerchantGroupDomain(req);
        List<MerchantGroupDomainVO> result = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(list)){
            Set<String> merchantGroupList = list.stream().map(MerchantGroupDomainVO::getGroupName).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(merchantGroupList)){
                for (String item : merchantGroupList){
                    MerchantGroupDomainVO resultVO = new MerchantGroupDomainVO();
                    resultVO.setGroupName(item);
                    result.add(resultVO);
                }
                for (MerchantGroupDomainVO itemResult : result){
                    Set<String> merchantCodeSet = new HashSet<>();
                    List<DomainInfoVO> domainList = Lists.newArrayList();
                    for (MerchantGroupDomainVO itemList : list){
                        if(itemResult.getGroupName().equals(itemList.getGroupName())){
                            DomainInfoVO domainInfo = new DomainInfoVO();
                            itemResult.setGroupType(itemList.getGroupType());
                            merchantCodeSet.add(itemList.getMerchantCode());
                            domainInfo.setAreaName(itemList.getAreaName());
                            domainInfo.setDomainType(itemList.getDomainType());
                            domainInfo.setDomainName(itemList.getDomainName());
                            domainInfo.setId(itemList.getDomainId());
                            domainList.add(domainInfo);
                        }
                    }
                    itemResult.setMerchantCodeSet(merchantCodeSet);
                    itemResult.setDomainInfo(domainList);
                }
            }
        }
        return APIResponse.returnSuccess(result);
    }

    public APIResponse editThreshold(MerchantGroupVO merchantGroupVO, HttpServletRequest request) {
        List<String> lines = getNacosConfig();
        if (CollectionUtil.isEmpty(lines)) {
            throw new BusinessException("检测域名阀值配置读取错误！");
        }
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            String[] tempSTrAr = line.split("=");
            String key = tempSTrAr[0];
            if (key.equalsIgnoreCase("websocket.17ce.threshold")) {
                sb.append("websocket.17ce.threshold=").append(merchantGroupVO.getThreshold()).append("\r\n\r\n");
            } else if (key.equalsIgnoreCase("self.domain.check.threshold")) {
                sb.append("self.domain.check.threshold=").append(merchantGroupVO.getSelfThreshold()).append("\r\n\r\n");
            }else if (key.equalsIgnoreCase("self.domain.check.nodes")) {
                sb.append("self.domain.check.nodes=").append(merchantGroupVO.getSelfNodes()).append("\r\n\r\n");
            }else if (key.equalsIgnoreCase("websocket.17ce.check.nodes")) {
                sb.append("websocket.17ce.check.nodes=").append(merchantGroupVO.getNodes()).append("\r\n\r\n");
            }
            else {
                sb.append(line).append("\r\n\r\n");
            }
        }
        MerchantGroupVO req = new MerchantGroupVO();
        req.setTab("ty");
        APIResponse response = getThreshold(req);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dataId", "multiterminal-interactive-center.properties");
        params.add("tenant", nacosNameSpace);
        params.add("group", "merchant");
        params.add("type", "properties");
        params.add("content", sb.toString());
        Mono<String> result = WebClient.create().post()
                .uri(nacosUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve().bodyToMono(String.class);
        log.info("publish:{}", result.block());

        req = (MerchantGroupVO) response.getData();
        MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
        filedVO.setFieldName(Arrays.asList("17ce检测阈值","自建检测工具检测阈值"));
        filedVO.setBeforeValues(Arrays.asList(req.getThreshold() + StringPool.SLASH + req.getNodes(), req.getSelfThreshold() + StringPool.SLASH + req.getSelfNodes()));
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, MerchantLogTypeEnum.EDIT, filedVO,  null, null, request);
        return APIResponse.returnSuccess();
    }

    private List<String> getNacosConfig() {
        String url = nacosUrl + "?tenant=" + nacosNameSpace + "&dataId=multiterminal-interactive-center.properties&group=merchant&type=properties";
        Mono<?> mono = WebClient.create().get().uri(url).retrieve().bodyToMono(String.class);
        log.info("result:" + mono.block());
        String configStr = (String) mono.block();
        if (org.apache.commons.lang.StringUtils.isBlank(configStr)) {
            return Lists.newArrayList();
        }
        return Arrays.stream(configStr.split("\\r?\\n")).collect(Collectors.toList());
    }


    public APIResponse getThreshold(MerchantGroupVO req) {
        req.setThreshold(nacosThirdEnableConfig.getThreshold());
        req.setSelfThreshold(nacosThirdEnableConfig.getSelfThreshold());
        req.setSelfNodes(nacosThirdEnableConfig.getSelfNodes());
        req.setNodes(nacosThirdEnableConfig.getSelfNodes());
        return APIResponse.returnSuccess(req);
    }

    public MerchantGroupPO selectMerchantGroupById(Long groupId, String tab) {
        return merchantGroupMapper.selectMerchantGroupById(groupId, tab);
    }
}
