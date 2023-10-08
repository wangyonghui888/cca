package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.dto.DomainDTO2;
import com.panda.multiterminalinteractivecenter.dto.DomainGroupDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainImportDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.entity.*;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.mapper.Domain2DomainGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.DomainGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.DomainGroupRelationMapper;
import com.panda.multiterminalinteractivecenter.service.IDomainGroupService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.BeanCopierUtils;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author :  ifan
 * @Description :  域名组服务类
 * @Date: 2021-07-02
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainGroupServiceImpl extends ServiceImpl<DomainGroupMapper, DomainGroup> implements IDomainGroupService {

    private final DomainGroupMapper domainGroupMapper;

    private final Domain2DomainGroupMapper domain2DomainGroupMapper;

    private final LocalCacheService localCacheService;

    private final AbstractMerchantDomainService abstractMerchantDomainService;

    private final MerchantApiClient merchantApiClient;

    private final DomainGroupRelationMapper domainGroupRelationMapper;

    private final MerchantLogService merchantLogService;

    /**一个域名组内最少含有n个线路商*/
    private static final long MIN_LINE_COUNT = 2;


    /**
     * 域名组查询
     *
     * @param domainGroupVO
     * @return
     */
    @Override
    public APIResponse queryList(DomainGroupVO domainGroupVO) {
        domainGroupVO.setDomainGroupName(domainGroupVO.getDomainGroupName());
        int count = domainGroupMapper.pageListCount(domainGroupVO);
        Map<String, Object> resultMap = new HashMap(count);
        if (count == 0) {
            return APIResponse.returnSuccess(resultMap);
        }
        resultMap.put("total", count);
        domainGroupVO.setStarNum((domainGroupVO.getPageNum() - 1) * domainGroupVO.getPageSize());
        List<DomainGroup> list = domainGroupMapper.pageList(domainGroupVO);
        if (CollectionUtil.isEmpty(list)) {
            return APIResponse.returnFail("暂无数据！");
        }
        resultMap.put("list", list.stream().map(this::buildDomainGroup).collect(Collectors.toList()));

        return APIResponse.returnSuccess(resultMap);
    }

    private DomainGroupVO buildDomainGroup(DomainGroup domainGroup) {
        DomainGroupVO domainGroupVO = new DomainGroupVO();
        BeanCopierUtils.copyProperties(domainGroup, domainGroupVO);
        if (Objects.nonNull(domainGroup.getExclusiveType())) {
            domainGroupVO.setExclusiveTypeName(ExclusiveEnum.getByExclusiveType(domainGroup.getExclusiveType()).getExclusiveName());
        }

        if (Objects.nonNull(domainGroup.getGroupType())) {
            domainGroupVO.setGroupTypeName(GroupTypeEnum.getByGroupType(domainGroup.getGroupType()).getGroupName());
        }

        //查找区域缓存中的区域名称
        if (Objects.nonNull(domainGroup.getBelongArea())) {
            DomainArea domainArea = localCacheService.getDomainArea(domainGroup.getBelongArea());
            if (null != domainArea && StrUtil.isNotBlank(domainArea.getName())) {
                domainGroupVO.setAreaName(domainArea.getName());
            }
        }

        //设置方案名称
        if (StrUtil.isNotBlank(domainGroup.getProgramName())) {
            domainGroupVO.setProgramName(domainGroup.getProgramName());
        }

        return domainGroupVO;
    }

    /**
     * 删除域名组
     *
     * @param id
     */
    @Override
    public APIResponse deleteById(Long id, HttpServletRequest request) {
        try {
            DomainGroup oldDomain = domainGroupMapper.selectById(id);
            //删除域名组管理
            int num = domainGroupMapper.delete(id);

            if (num == 0) {
                return APIResponse.returnFail("域名组管默认数据不能删除！");
            }

            //修改域名关系domainGroupId
            domainGroupMapper.deleteByGroupId(id);

            // 删除和方案的关系
            domainGroupRelationMapper.deleteByGroupId(id);

            MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(oldDomain, new DomainGroup());
            String dataId = oldDomain.getDomainGroupName() + StringPool.AMPERSAND + (oldDomain.getExclusiveType() == 1 ? "区域" : "VIP") +StringPool.AMPERSAND + id;
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_GROUP_MANAGE, MerchantLogTypeEnum.DEL, filedVO,  null,
                    dataId, request);

        } catch (Exception ex) {
            log.error("删除域名组管理信息,失败:", ex);
            return APIResponse.returnFail("删除域名组管理信息失败！");
        }
        return APIResponse.returnSuccess(true);
    }

    /**
     * 新建域名组
     *
     * @param domainGroupVO
     * @param userName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse save(DomainGroupVO domainGroupVO, String userName, HttpServletRequest request) {
        DomainGroupVO domainGroupNew = new DomainGroupVO();
        domainGroupNew.setDomainGroupName(domainGroupVO.getDomainGroupName());
        domainGroupNew.setTab(domainGroupVO.getTab());
        int count = domainGroupMapper.pageListCount(domainGroupNew);
        if (count != 0) {
            return APIResponse.returnFail("域名组名称已存在");
        }

        if(Objects.nonNull(domainGroupVO.getExclusiveType()) && domainGroupVO.getExclusiveType() == 1 && domainGroupVO.getTab().equals(TabEnum.TY.getName())){
            int groupCount = domainGroupMapper.checkAreaDomainGroup(domainGroupVO.getBelongArea(), domainGroupVO.getGroupType(),domainGroupVO.getTab());
            if (groupCount > 0) {
                return APIResponse.returnFail("您选择的区域属于同商户分组类型，请重新选择区域！");
            }
        }
        // 线路商校验
        String msg = validateDomainGroupByLineId(domainGroupVO);
        if(StrUtil.isNotBlank(msg)){
            return APIResponse.returnFail(msg);
        }
        try {
            DomainGroup domainGroup = new DomainGroup();

            BeanCopierUtils.copyProperties(domainGroupVO, domainGroup);
            if(domainGroup.getGroupType()==null || domainGroup.getGroupType() == 0)
                domainGroup.setGroupType(1);
            domainGroup.setDomainGroupName(domainGroupVO.getDomainGroupName().trim());
            domainGroup.setCreateTime(System.currentTimeMillis());
            domainGroup.setUpdateTime(System.currentTimeMillis());
            domainGroup.setLastUpdated(userName);
            if (StrUtil.isBlank(domainGroupVO.getTab())) {
                domainGroup.setTab("ty");
            }
            if(domainGroupVO.getH5Threshold()==null || domainGroupVO.getH5Threshold()==0){
                domainGroupVO.setH5Threshold(1);
            }
            if(domainGroupVO.getPcThreshold()==null || domainGroupVO.getPcThreshold()==0){
                domainGroupVO.setPcThreshold(1);
            }
            if(domainGroupVO.getApiThreshold()==null || domainGroupVO.getApiThreshold()==0){
                domainGroupVO.setApiThreshold(1);
            }
            if(domainGroupVO.getImgThreshold()==null || domainGroupVO.getImgThreshold()==0){
                domainGroupVO.setImgThreshold(1);
            }

            domainGroupMapper.insert(domainGroup);

            String dataId = domainGroupVO.getDomainGroupName() + StringPool.AMPERSAND + (domainGroupVO.getExclusiveType() == 1 ? "区域" : "VIP") ;
            MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(new DomainGroup(), domainGroup);
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_GROUP_MANAGE, MerchantLogTypeEnum.SAVE, filedVO,  null,
                    dataId, request);

            //新增域名组关系数据
            if (StrUtil.isNotBlank(domainGroup.getDomainIds())) {
                List<String> list = Arrays.asList(domainGroup.getDomainIds().split(","));
                List<DomainGroupRelation> domainGroupRelationList = new ArrayList<>();
                list.forEach(str -> {
                    DomainGroupRelation domainGroupRelation = new DomainGroupRelation();
                    domainGroupRelation.setDomainId(Long.valueOf(str));
                    domainGroupRelation.setDomainGroupId(domainGroup.getId());
                    domainGroupRelation.setLastUpdated(userName);
                    domainGroupRelation.setTab(domainGroup.getTab());
                    domainGroupRelation.setCreateTime(System.currentTimeMillis());
                    domainGroupRelation.setUpdateTime(System.currentTimeMillis());
                    domainGroupRelationList.add(domainGroupRelation);
                });
                domainGroupMapper.batchSaveDomainGroupRelation(domainGroupRelationList);
            }

            return APIResponse.returnSuccess();

        } catch (Exception ex) {
            log.error("save DomainGroup error:", ex);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR, ex.getMessage());
        }
    }

    private String validateDomainGroupByLineId(DomainGroupVO domainGroupVO) {

        List<String> msgList = Lists.newArrayList();
        if (StrUtil.isNotBlank(domainGroupVO.getDomainIds())) {
            List<Long> domainIdList = Arrays.stream(domainGroupVO.getDomainIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
            List<TyDomain> domainList = abstractMerchantDomainService.getDomainServiceBean(domainGroupVO.getTab())
                    .getDomainListByIds(domainIdList,domainGroupVO.getTab());
            if(CollectionUtils.isNotEmpty(domainList)){
                Map<Integer,List<TyDomain>> domainTypeDetails = domainList.stream().collect(Collectors.groupingBy(TyDomain::getDomainType));
                // 补充未录入类型 强制校验 h5 pc api
                for (int type = 1; type <= 3 ; type++) {
                    if(!domainTypeDetails.containsKey(type)){
                        domainTypeDetails.put(type,Lists.newArrayList());
                    }
                }
                List<Integer> validateDomainTypes = Arrays.asList(1,2,3);
                domainTypeDetails.forEach((k,v)->{
                    if(validateDomainTypes.contains(k) && (
                            CollectionUtils.isEmpty(v) ||
                            v.stream().map(TyDomain::getLineCarrierId).distinct().count() < MIN_LINE_COUNT)
                    ){
                        msgList.add(DomainTypeEnum.getByCode(k).getValue());
                    }
                });
            }
        }
        if(CollectionUtils.isNotEmpty(msgList)){
            return msgList +"域名下只有1个线路商，请补充其他线路商域名";
        }
        return null;
    }
    /**
     * 修改域名组
     *
     * @param domainGroupVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse update(DomainGroupVO domainGroupVO, String userName, HttpServletRequest request) {
        if (domainGroupVO.getId() == null) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID, userName);
        }

        String msg = validateDomainGroupByLineId(domainGroupVO);
        if(StrUtil.isNotBlank(msg)){
            return APIResponse.returnFail(msg);
        }

        try {

            final DomainGroup oldDomainGroup = domainGroupMapper.selectById(domainGroupVO.getId());

            DomainGroup domainGroup = new DomainGroup();
            BeanCopierUtils.copyProperties(domainGroupVO, domainGroup);
            domainGroup.setDomainGroupName(domainGroup.getDomainGroupName());
            domainGroup.setUpdateTime(System.currentTimeMillis());
            domainGroup.setLastUpdated(userName);

            if(domainGroupVO.getH5Threshold()==null || domainGroupVO.getH5Threshold()==0){
                domainGroupVO.setH5Threshold(1);
            }
            if(domainGroupVO.getPcThreshold()==null || domainGroupVO.getPcThreshold()==0){
                domainGroupVO.setPcThreshold(1);
            }
            if(domainGroupVO.getApiThreshold()==null || domainGroupVO.getApiThreshold()==0){
                domainGroupVO.setApiThreshold(1);
            }
            if(domainGroupVO.getImgThreshold()==null || domainGroupVO.getImgThreshold()==0){
                domainGroupVO.setImgThreshold(1);
            }

            domainGroupMapper.update(domainGroup);

            String dataId = domainGroupVO.getDomainGroupName() + StringPool.AMPERSAND + (oldDomainGroup.getExclusiveType() == 1 ? "区域" : "VIP") +StringPool.AMPERSAND + domainGroupVO.getId();
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT;
            MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(oldDomainGroup, domainGroup);
            if(filedVO.getFieldName().size() > 0 && MerchantFieldUtil.FIELD_MAPPING.get("status").equals(filedVO.getFieldName().get(0))) typeEnum = MerchantLogTypeEnum.EDIT_INFO_STATUS;
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_GROUP_MANAGE, typeEnum, filedVO,  null,
                    dataId, request);

            // 域名组被禁用了
            if(oldDomainGroup.getStatus()==1 && domainGroup.getStatus() != null && domainGroup.getStatus() == 0){
                // 删除和方案的关系
                domainGroupRelationMapper.deleteByGroupId(domainGroupVO.getId());
            }


            // 修改域名关系domainGroupId
            if (StrUtil.isNotBlank(domainGroup.getDomainIds())) {
                List<String> list = Arrays.asList(domainGroup.getDomainIds().split(","));
                //删除域名组关系数据
                domainGroupMapper.deleteByDomainIds(domainGroup.getId(),domainGroupVO.getTab());

                List<DomainGroupRelation> domainGroupRelationList = new ArrayList<>();
                list.forEach(str -> {
                    DomainGroupRelation domainGroupRelation = new DomainGroupRelation();
                    domainGroupRelation.setDomainId(Long.valueOf(str));
                    domainGroupRelation.setDomainGroupId(domainGroup.getId());
                    domainGroupRelation.setLastUpdated(userName);
                    domainGroupRelation.setTab(domainGroup.getTab());
                    domainGroupRelation.setCreateTime(System.currentTimeMillis());
                    domainGroupRelation.setUpdateTime(System.currentTimeMillis());
                    domainGroupRelationList.add(domainGroupRelation);
                });
                domainGroupMapper.batchSaveDomainGroupRelation(domainGroupRelationList);
            }

            CompletableFuture.runAsync(()->merchantApiClient.kickoutMerchant(null));

            return APIResponse.returnSuccess();
        } catch (Exception ex) {
            log.error("update DomainGroup error:", ex);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR, ex.getMessage());
        }
    }

    /**
     * 方案下域名组查询
     *
     * @param programId
     * @return
     */
    @Override
    public APIResponse findProgramDownDomainGroupList(Long programId, Integer groupType,String tab) {
        List<DomainGroup> list = domainGroupMapper.findProgramDownDomainGroupList(programId, groupType,tab);
        if (CollectionUtil.isEmpty(list)) {
            return APIResponse.returnFail("暂无数据！");
        }

        return APIResponse.returnSuccess(list.stream().map(this::buildDomainGroup).collect(Collectors.toList()));
    }

    /**
     * 获取域名数据
     *
     * @param domainVO
     * @return
     */
    @Override
    public APIResponse getDomainTree(DomainVO domainVO) {

        // 拼装参数
        List<Long> excludeDomainIdList = domainVO.getExcludeDomainIdList();
        if(excludeDomainIdList==null){
            excludeDomainIdList = Lists.newArrayList();
        }
        if(domainVO.getExcludeDomainId()!=null){
            excludeDomainIdList.add(domainVO.getExcludeDomainId());
        }
        domainVO.setExcludeDomainIdList(excludeDomainIdList);

        if(domainVO.getExcludeDomainGroupId() != null){
            List<Long> domainIds = domain2DomainGroupMapper.getDomainIdsByDomainGroupId(domainVO.getExcludeDomainGroupId(),domainVO.getTab());
            if(CollectionUtils.isNotEmpty(domainIds)){
                if(CollectionUtil.isEmpty(domainVO.getExcludeDomainIdList())){
                    domainVO.setExcludeDomainIdList(domainIds);
                }else{
                    domainVO.getExcludeDomainIdList().addAll(domainIds);
                }
            }
        }

        // 查询
        List<DomainVO> domainVOList = domainGroupMapper.getDomainTree(domainVO);
        if (CollectionUtil.isEmpty(domainVOList)) {
            APIResponse.returnFail("此商户分组类型暂无域名");
        }
        return APIResponse.returnSuccess(domainVOList);
    }

    @Override
    public APIResponse getDomainTreeV2(DomainVO domainVO) {

        // 拼装参数
        List<Long> excludeDomainIdList = domainVO.getExcludeDomainIdList();
        if(excludeDomainIdList == null){
            excludeDomainIdList = Lists.newArrayList();
        }
        if(domainVO.getExcludeDomainId()!=null){
            excludeDomainIdList.add(domainVO.getExcludeDomainId());
        }
        domainVO.setExcludeDomainIdList(excludeDomainIdList);

        if(domainVO.getExcludeDomainGroupId() != null){
            List<Long> domainIds = domain2DomainGroupMapper.getDomainIdsByDomainGroupId(domainVO.getExcludeDomainGroupId(),domainVO.getTab());
            if(CollectionUtils.isNotEmpty(domainIds)){
                if(CollectionUtil.isEmpty(domainVO.getExcludeDomainIdList())){
                    domainVO.setExcludeDomainIdList(domainIds);
                }else{
                    domainVO.getExcludeDomainIdList().addAll(domainIds);
                }
            }
        }

        final List<Long> domainIds = domainVO.getExcludeDomainIdList();
        domainVO.setExcludeUsedDomain(false);
        domainVO.setExcludeDomainId(null);
        domainVO.setExcludeDomainGroupId(null);
        domainVO.setExcludeDomainIdList(null);
        // 查询
        List<DomainVO> domainVOList = domainGroupMapper.getDomainTree(domainVO);
        if (CollectionUtil.isEmpty(domainVOList)) {
            APIResponse.returnFail("此商户分组类型暂无域名");
        }
        for (DomainVO vo : domainVOList) {
            if(domainIds.contains(vo.getId())){
                vo.setIsSelect(true);
            }
        }
        return APIResponse.returnSuccess(domainVOList);
    }

    @Override
    public APIResponse findDomainExist(Long domainGroupId, String tab) {
        // 查询域名和域名组关系表
        List<DomainDTO2> domainDTO2s = domainGroupMapper.findDomainExist(domainGroupId,tab);
        if(CollectionUtils.isEmpty(domainDTO2s)){
            return APIResponse.returnSuccess(domainDTO2s);
        }

        // 查询域名组信息
        final List<DomainGroupDTO> domainGroupDTOSList = domainGroupMapper.selectSimpleAll(tab);
        if(CollectionUtils.isEmpty(domainGroupDTOSList)){
            return APIResponse.returnSuccess(domainDTO2s);
        }
        final  Map<Long,String> domainGroupDTOSMap = domainGroupDTOSList.stream().collect(Collectors.toMap(DomainGroupDTO::getId,DomainGroupDTO::getDomainGroupName));
        final Set<Long> groupIdSet = domainGroupDTOSMap.keySet();

        StringBuilder sb;
        //对域名组信息赋值
        for (DomainDTO2 domainDTO2 : domainDTO2s) {
            String groupIds = domainDTO2.getGroupIdStr();
            if(StringUtils.isBlank(groupIds)){
                continue;
            }

            // 去掉冗余值
            List<Long> groupIdList = Arrays.stream(groupIds.split(",")).map(Long::valueOf).filter(groupIdSet::contains).collect(Collectors.toList());

            sb = new StringBuilder();
            for (Long groupId : groupIdList) {
                if(groupId != null && StringUtils.isNotBlank(domainGroupDTOSMap.get(groupId))){
                    sb.append(domainGroupDTOSMap.get(groupId)).append(",");
                }
            }
            domainDTO2.setGroupNameStr(StringUtils.isNotBlank(sb) ? sb.substring(0,sb.length()-1) : sb.toString());

        }
        return APIResponse.returnSuccess(domainDTO2s);
    }


    /**
     * 校验区域是否属于同分组类型
     *
     * @param areaId
     * @param groupType
     * @param tab
     * @return
     */
    @Override
    public APIResponse checkAreaDomainGroup(Long areaId, Integer groupType,String tab) {
        int count = domainGroupMapper.checkAreaDomainGroup(areaId, groupType,tab);
        if (count > 0) {
            return APIResponse.returnFail("您选择的区域属于同商户分组类型，请重新选择区域！");
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public APIResponse importDomains(DomainImportDTO domainImportDTO, String userName) {
        if(CollectionUtils.isEmpty(domainImportDTO.getDomainList())){
            return APIResponse.returnFail("导入域名不可为空！");
        }
        StringBuilder validateMsg = null;
        final String tab = domainImportDTO.getTab();
        List<String> domainNameList =
                domainImportDTO.getDomainList().stream().map(String::trim).collect(Collectors.toList());

        List<TyDomain> domainList = abstractMerchantDomainService.getDomainServiceBean(tab)
                .getDomainListByNames(domainNameList,tab);

        if(CollectionUtils.isEmpty(domainList)){
            return APIResponse.returnFail("所有域名都不可用，请检查！");
        }

        final List<String> existDomainList  = Optional.of(domainList).orElse(Lists.newArrayList()).stream().map(TyDomain::getDomainName).collect(Collectors.toList());

        domainNameList.removeAll(existDomainList);
        if(CollectionUtils.isNotEmpty(domainNameList)){
            validateMsg = new StringBuilder("以下域名不符合要求，系统将自动忽略:");
            for (String domainName : domainNameList) {
                validateMsg.append("\n").append(domainName);
            }
        }

        //全量
        if(domainImportDTO.getImportType().equals(1)){
            // 删除这个域名组下这个域名类型的域名
            List<Long> ids = domain2DomainGroupMapper.selectIdsByDomainIdAndDomainType(domainImportDTO.getDomainGroupId(),null,tab);
            if(CollectionUtils.isNotEmpty(ids)){
                log.info("{}操作域名导入，param：{}，解绑域名id：{}",userName, JSON.toJSONString(domainImportDTO),ids);
                domain2DomainGroupMapper.deleteBatchIds(ids);
            }
        }
        Long now = System.currentTimeMillis();

        // 增量&全量都要新增域名
        List<Domain2DomainGroup> list = Lists.newArrayList();
        for (TyDomain domain : domainList) {
            list.add(
                    Domain2DomainGroup
                            .builder()
                            .domainId(domain.getId())
                            .domainGroupId(domainImportDTO.getDomainGroupId())
                            .tab(tab)
                            .createTime(now)
                            .updateTime(now)
                            .lastUpdated(userName)
                    .build());

        }
        int insertCount = domain2DomainGroupMapper.insertList(list);
        log.info("{}个域名绑定好当前域名组{}",insertCount,domainImportDTO.getDomainGroupId());

        if(StringUtils.isNotBlank(validateMsg)){
            return APIResponse.returnSuccess("0001",validateMsg.toString(),domainNameList);
        }
        return APIResponse.returnSuccess();
    }

}
