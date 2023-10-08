package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.entity.DomainGroup;
import com.panda.multiterminalinteractivecenter.entity.DomainProgram;
import com.panda.multiterminalinteractivecenter.entity.DomainProgramRelation;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.mapper.DomainGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.DomainGroupRelationMapper;
import com.panda.multiterminalinteractivecenter.mapper.DomainProgramMapper;
import com.panda.multiterminalinteractivecenter.mapper.MerchantGroupMapper;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.service.IDomainProgramService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.BeanCopierUtils;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainProgramVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  ifan
 * @Description :  域名切换方案服务类
 * @Date: 2021-07-02
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainProgramServiceImpl implements IDomainProgramService {

    private final DomainProgramMapper programMapper;

    private final DomainGroupMapper domainGroupMapper;

    private final DomainGroupRelationMapper domainGroupRelationMapper;

    private final MerchantGroupMapper merchantGroupMapper;

    private final MerchantLogService merchantLogService;

    /**
     * 域名切换方案查询
     *
     * @param domainProgramVO
     * @return
     */
    @Override
    public APIResponse queryList(DomainProgramVO domainProgramVO) {
        domainProgramVO.setProgramName(domainProgramVO.getProgramName());
        int count = programMapper.pageListCount(domainProgramVO);
        Map<String, Object> resultMap = new HashMap(count);
        if (count == 0) {
            return APIResponse.returnSuccess(resultMap);
        }
        resultMap.put("total", count);
        domainProgramVO.setStarNum((domainProgramVO.getPageNum() - 1) * domainProgramVO.getPageSize());
        List<DomainProgram> list = programMapper.pageList(domainProgramVO);
        if (CollectionUtil.isEmpty(list)) {
            return APIResponse.returnFail("暂无数据！");
        }
        resultMap.put("list", list.stream().map(this::buildDomainProgram).collect(Collectors.toList()));
        return APIResponse.returnSuccess(resultMap);
    }

    private DomainProgramVO buildDomainProgram(DomainProgram domainProgram) {
        DomainProgramVO domainProgramVO = new DomainProgramVO();
        BeanCopierUtils.copyProperties(domainProgram, domainProgramVO);
        domainProgramVO.setGroupTypeName(GroupTypeEnum.getByGroupType(domainProgram.getGroupType()).getGroupName());
        return domainProgramVO;
    }

    /**
     * 删除域名切换方案
     *
     * @param domainProgramVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse deleteById(DomainProgramVO domainProgramVO, HttpServletRequest request) {
        try {
            // 被使用的方案不能被删除
            int useCount = programMapper.getUseCountById(domainProgramVO.getId());
            if(useCount > 0){
                return APIResponse.returnFail("该方案正在使用，不可删除");
            }
            DomainProgram oldProgram = programMapper.selectById(domainProgramVO.getId());

            //删除域名切换方案数据
            int num = programMapper.delete(domainProgramVO.getId());
            if (num == 0) {
                return APIResponse.returnFail("域名切换方案默认数据不能删除！");
            }
            // 删除域名切换方案关系表数据
            programMapper.delDomainProgramRelation(domainProgramVO.getId(), domainProgramVO.getGroupType(),domainProgramVO.getTab());

            //修改商户组方案id
            merchantGroupMapper.updateMerchantGroupByProgramId(domainProgramVO.getId(), domainProgramVO.getGroupType(),domainProgramVO.getTab());
            MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(oldProgram, new DomainProgram());
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_PROGRAM, MerchantLogTypeEnum.DEL, filedVO,  null,
                    oldProgram.getProgramName() + StringPool.AMPERSAND  + domainProgramVO.getId(), request);
        } catch (Exception ex) {
            log.error("删除域名切换方案管理信息,失败:", ex);
            return APIResponse.returnFail("删除域名切换方案管理信息失败！");
        }
        return APIResponse.returnSuccess(true);
    }

    /**
     * 查询方案集合
     *
     * @return
     */
    @Override
    public APIResponse findProgramList(DomainProgramVO domainProgramVO) {
        return APIResponse.returnSuccess(programMapper.findProgramList(domainProgramVO));
    }

    /**
     * 批量保存方案域名组关系数据
     *
     * @param domainProgramRelation
     * @param userName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse batchSaveDomainProgramRelation(DomainProgramRelation domainProgramRelation, String userName, HttpServletRequest request) {

        try {
            List<DomainProgramRelation> relationList = new ArrayList<>();
            domainProgramRelation.getConfig().forEach(programRelation -> {
                if(programRelation.getGroupType()==null||programRelation.getGroupType()==0)programRelation.setGroupType(1);
                programRelation.setLastUpdated(userName);
                programRelation.setCreateTime(System.currentTimeMillis());
                programRelation.setUpdateTime(System.currentTimeMillis());
                programRelation.setProgramId(domainProgramRelation.getProgramId());
                relationList.add(programRelation);
            });

            // 删除域名方案关系数据
            domainGroupRelationMapper.deleteByDomainProgramId(relationList, domainProgramRelation.getProgramId());
            programMapper.batchSaveDomainProgramRelation(relationList);
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().add("选择域名组");
            DomainProgram program = programMapper.selectById(domainProgramRelation.getProgramId());
            List<Long> ids = domainProgramRelation.getConfig().stream().map(DomainProgramRelation::getDomainGroupId).collect(Collectors.toList());
            List<DomainGroup> groups = domainGroupMapper.selectBatchIds(ids);
            filedVO.getAfterValues().add(groups.stream().map(DomainGroup::getDomainGroupName).collect(Collectors.joining(",")));
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_PROGRAM_INFO, MerchantLogTypeEnum.DOMAIN_GROUP_SELECT, filedVO,  null,
                    program.getProgramName() + StringPool.AMPERSAND + program.getId(), request);
            return APIResponse.returnSuccess();
        } catch (Exception ex) {
            log.error("批量保存方案域名组关系数据失败:", ex);
            return APIResponse.returnFail(ex.getMessage());
        }
    }

    /**
     * 查询方案详情
     *
     * @param domainProgramVO
     * @return
     */
    @Override
    public APIResponse findProgramDetail(DomainProgramVO domainProgramVO) {
        try {
            List<String> list = Arrays.asList(domainProgramVO.getProgramIds().split(","));
            return APIResponse.returnSuccess(
                    programMapper.findProgramDetail(list, domainProgramVO.getGroupType(), domainProgramVO.getTab())
            );
        } catch (Exception ex) {
            log.error("查询方案详情失败：", ex);
            return APIResponse.returnFail(ex.getMessage());
        }
    }

    /**
     * 新建域名切换方案
     *
     * @param domainProgramVO
     * @param userName
     * @return
     */
    @Override
    public APIResponse save(DomainProgramVO domainProgramVO, String userName, HttpServletRequest request) {
        DomainProgramVO programNew = new DomainProgramVO();
        programNew.setProgramName(domainProgramVO.getProgramName());
        programNew.setTab(domainProgramVO.getTab());
        int count = programMapper.countByProgram(programNew);
        if (count != 0) {
            return APIResponse.returnFail("域名方案名称已存在");
        }
        try {
            DomainProgram domainProgram = new DomainProgram();
            BeanCopierUtils.copyProperties(domainProgramVO, domainProgram);
            if(domainProgram.getGroupType()==null||domainProgram.getGroupType()==0) domainProgram.setGroupType(1);
            domainProgram.setProgramName(domainProgramVO.getProgramName().trim());
            domainProgram.setCreateTime(System.currentTimeMillis());
            domainProgram.setUpdateTime(System.currentTimeMillis());
            domainProgram.setLastUpdated(userName);
            programMapper.insert(domainProgram);
            String value;
            String fieldName = MerchantFieldUtil.FIELD_MAPPING.get("merchantGroupName");
            if(StringUtils.isNotBlank(domainProgramVO.getMerchantGroupId())) {// dj、cp
                MerchantGroupPO group = merchantGroupMapper.selectMerchantGroupById(Long.valueOf(domainProgramVO.getMerchantGroupId()), domainProgramVO.getTab());
                value = group.getGroupName();
            }else {
                fieldName = MerchantFieldUtil.FIELD_MAPPING.get("groupType");
                value = MerchantGroupEnum.getNameByKey(domainProgramVO.getGroupType());
            }
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().addAll(Arrays.asList(MerchantFieldUtil.FIELD_MAPPING.get("programName"), fieldName));
            filedVO.setAfterValues(Arrays.asList(domainProgram.getProgramName(), value));
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_PROGRAM, MerchantLogTypeEnum.SAVE, filedVO,  null,
                    domainProgramVO.getProgramName(), request);
            return APIResponse.returnSuccess();

        } catch (Exception ex) {
            log.error("save domainProgram error:", ex);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR, ex.getMessage());
        }
    }

    /**
     * 修改域名切换方案
     *
     * @param domainProgramVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse update(DomainProgramVO domainProgramVO, String userName, HttpServletRequest request) {
        if (domainProgramVO.getId() == null) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID, userName);
        }
        if (null != domainProgramVO.getStatus() &&  2 == domainProgramVO.getStatus() && domainProgramVO.getDelTag() == 1) {
            return APIResponse.returnFail("默认方案不能禁用！");
        }
        if (null != domainProgramVO.getStatus() &&  2 == domainProgramVO.getStatus()) {
            int useCount = programMapper.getUseCountById(domainProgramVO.getId());
            if(useCount > 0){
                return APIResponse.returnFail("该方案正在使用，不可禁用");
            }
        }

        if(StringUtils.isNotBlank(domainProgramVO.getProgramName())){
            int count = programMapper.countByProgram(domainProgramVO);
            if (count != 0) {
                return APIResponse.returnFail("域名方案名称已存在");
            }
        }

        try {
            DomainProgram oldProgram = programMapper.selectById(domainProgramVO.getId());
            DomainProgram domainProgram = new DomainProgram();
            BeanCopierUtils.copyProperties(domainProgramVO, domainProgram);
            if(domainProgram.getGroupType()==null||domainProgram.getGroupType()==0) domainProgram.setGroupType(1);
            domainProgram.setProgramName(domainProgram.getProgramName());
            domainProgram.setUpdateTime(System.currentTimeMillis());
            domainProgram.setLastUpdated(userName);
            programMapper.update(domainProgram);

            MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(oldProgram, domainProgram);
            MerchantLogTypeEnum typeEnum = MerchantFieldUtil.FIELD_MAPPING.get("status").equals(filedVO.getFieldName().get(0)) ? MerchantLogTypeEnum.EDIT_INFO_STATUS : MerchantLogTypeEnum.EDIT;
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_PROGRAM, typeEnum, filedVO,  null,
                    oldProgram.getProgramName() + StringPool.AMPERSAND + domainProgramVO.getId(), request);
            return APIResponse.returnSuccess();
        } catch (Exception ex) {
            log.error("update domainProgram error:", ex);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR, ex.getMessage());
        }
    }

}
