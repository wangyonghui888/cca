package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.dto.DomainDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamDto;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamTYDTO;
import com.panda.multiterminalinteractivecenter.entity.Domain2DomainGroup;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.mapper.*;
import com.panda.multiterminalinteractivecenter.service.MerchantDomainService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.api.DomainApiVO;
import com.panda.multiterminalinteractivecenter.vo.api.DomainGroupApiVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 *  dj/cp service实现
 */
@Service(value = "thirdMerchantDomainServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RefreshScope
public class ThirdMerchantDomainServiceImpl extends ServiceImpl<ThirdDomainMapper, TyDomain> implements MerchantDomainService {

    private final ThirdDomainMapper thirdDomainMapper;
    private final Domain2DomainGroupMapper domain2DomainGroupMapper;
    private final DomainGroupMapper domainGroupMapper;
    private final MerchantGroupMapper merchantGroupMapper;
    private final CpDjMerchantGroupServiceImpl cpDJMerchantGroupService;
    private final DomainProgramMapper domainProgramMapper;
    private final MerchantLogService merchantLogService;

    @Override
    public APIResponse<?> pageList(Integer page, Integer size, String domainName, Integer domainType, Long domainGroupId, String domainGroupName, Long lineCarrierId, Integer groupType, Boolean used, String tab) {
        PageHelper.startPage(page, size, true);
        List<TyDomain> list = thirdDomainMapper.pageList(page, size, domainName, domainType, domainGroupId, domainGroupName, lineCarrierId, groupType, used, tab);

        if (CollectionUtil.isNotEmpty(list)) {
            List<TyDomain.DomainGroupDTO> domainGroupDetailList;
            for (TyDomain tyDomain : list) {
                domainGroupDetailList = Lists.newArrayList();
                String domainGroupDetail = tyDomain.getDomainGroupDetail();
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
            }
        }

        PageInfo<TyDomain> poList = new PageInfo<>(list);
        return APIResponse.returnSuccess(poList);
    }

    @Override
    public void create(TyDomain tyDomain) {
        int count = thirdDomainMapper.countByName(tyDomain.getDomainName(), null);
        if (count != 0) {
            throw new BusinessException("该域名已存在!");
        }
        long now = System.currentTimeMillis();
        tyDomain.setCreateTime(now);
        tyDomain.setUpdateTime(now);
        tyDomain.setStatus(0);
        tyDomain.setSelfTestTag(1);
        tyDomain.setEnable(2);
        thirdDomainMapper.insertIgnoreNull(tyDomain);
    }

    @Override
    public void edit(TyDomain tyDomain, HttpServletRequest request) {
        int count = thirdDomainMapper.countByName(tyDomain.getDomainName(), tyDomain.getId());
        if (count != 0) {
            throw new BusinessException("该域名已存在");
        }
        TyDomain oldDomain = thirdDomainMapper.getById(tyDomain.getId());
        tyDomain.setUpdateTime(System.currentTimeMillis());
        tyDomain.setUpdateUser(tyDomain.getUpdateUser());
        thirdDomainMapper.updateIgnoreNull(tyDomain);
        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(BeanUtil.toBean(oldDomain, DomainVO.class), BeanUtil.toBean(tyDomain, DomainVO.class));
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.EDIT, filedVO,  null,
                tyDomain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(tyDomain.getDomainType()) + StringPool.AMPERSAND +
                        MerchantGroupEnum.getNameByKey(tyDomain.getGroupType())+ StringPool.AMPERSAND + tyDomain.getId(), request);
    }

    @Override
    public void delete(Long id,String tab, HttpServletRequest request) {
        TyDomain oldDomain = thirdDomainMapper.getById(id);
        validate(id, "删除",tab);
        thirdDomainMapper.delById(id);
        thirdDomainMapper.deleteByDomainId(id,tab);
        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(BeanUtil.toBean(oldDomain, DomainDTO.class), new DomainDTO());
        filedVO.setAfterValues(Collections.singletonList("-"));
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.DEL, filedVO,  null,
                oldDomain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(oldDomain.getDomainType()) + StringPool.AMPERSAND +
                        MerchantGroupEnum.getNameByKey(oldDomain.getGroupType())+ StringPool.AMPERSAND + oldDomain.getId(), request);
    }

    @Override
    public void off(List<Long> ids, String tab, HttpServletRequest request, String dataId){
        thirdDomainMapper.offDomain(ids,tab);
        if (tab.equalsIgnoreCase(TabEnum.DJ.getName())) {
            cpDJMerchantGroupService.sendDJMsg("", 0, "", DomainConstants.DOMAIN_CHANGE_MANUAL);
        } else {
            List<Long> list = thirdDomainMapper.selectMerchantGroupId(ids, tab);
            if(list != null && list.size() > 0) {
                list.stream().distinct().forEach(merchantGroupId -> cpDJMerchantGroupService.clearCPCache(merchantGroupId, null));
            }
        }
        //操作日志
        List<TyDomain> list = thirdDomainMapper.getDomainListByIds(ids, tab);
        String beforeValue = CollectionUtils.isNotEmpty(list) ? list.stream().map(TyDomain::getDomainName).collect(Collectors.joining(",")) : null;
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("soldOut"), beforeValue,null );
        String[] data = dataId.split(StringPool.AMPERSAND);
        filedVO.setMerchantName(data[0]);
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_SOLD_OUT, MerchantLogTypeEnum.DOMAIN_SOLD_OUT, filedVO,  null,
                dataId, request);
    }

    @Override
    public void importDomains(DomainImportReqDTO domainDTO) {
        List<String> domainList = domainDTO.getDomainName();

        if (CollectionUtil.isEmpty(domainList)) {
            throw new BusinessException("域名数据不能为空！");
        }

        List<String> domains = domainList.stream().distinct().collect(Collectors.toList());
        for (String domain : domains) {
            if (StringUtils.isBlank(domain)) {
                continue;
            }
            // 查重复
            int count = thirdDomainMapper.countByName(domain, null);
            if (count != 0) {
                throw new BusinessException(String.format("【%s】此域名已存在，无法导入！", domain));
            }
        }

        long now = System.currentTimeMillis();
        TyDomain insertTyDomain = new TyDomain();

        insertTyDomain.setStatus(1);
        insertTyDomain.setEnable(DomainEnableEnum.WAIT_USE.getCode());
        insertTyDomain.setCreateTime(now);
        insertTyDomain.setUpdateTime(now);
        insertTyDomain.setSelfTestTag(1);
        insertTyDomain.setGroupType(1);

        insertTyDomain.setCreateUser(domainDTO.getOperator());
        insertTyDomain.setUpdateUser(domainDTO.getOperator());
        insertTyDomain.setDomainType(domainDTO.getDomainType());
        insertTyDomain.setLineCarrierId(domainDTO.getLineCarrierId());
        insertTyDomain.setTab(StringUtils.defaultIfBlank(domainDTO.getTab(), "ty"));

        domains.forEach(
                domainName -> {
                    domainName = domainName.trim();
                    if (StringUtils.isNotBlank(domainName)) {
                        insertTyDomain.setDomainName(domainName);
                        thirdDomainMapper.insertIgnoreNull(insertTyDomain);
                    }
                }
        );
    }

    @Override
    public void replace(Long oldDomainId, Long domainId,String tab, HttpServletRequest request) {
        TyDomain oldTyDomain = thirdDomainMapper.getById(oldDomainId);
        TyDomain tyDomain = thirdDomainMapper.getById(domainId);
        if (Objects.isNull(oldTyDomain)) {
            throw new BusinessException("域名不存在或已删除");
        }
        List<Domain2DomainGroup> domain2DomainGroup = domain2DomainGroupMapper.selectByDomainId(oldDomainId,tab);

        if (CollectionUtil.isEmpty(domain2DomainGroup)) {
            throw new BusinessException("当前域名【" + oldTyDomain.getDomainName() + "】没有域名组使用");
        }

        domain2DomainGroupMapper.replaceByDomainId(oldDomainId, domainId,tab);
        //记录操作日志
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("replaceDomain"), oldTyDomain.getDomainName(), tyDomain.getDomainName());
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.DOMAIN_SELECT, filedVO,  null,
                tyDomain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(tyDomain.getDomainType()) + StringPool.AMPERSAND +
                        MerchantGroupEnum.getNameByKey(tyDomain.getGroupType())+ StringPool.AMPERSAND + tyDomain.getId(), request);
        /**
         * 处理域名状态  ：  这里产品要求
         * a是待使用的   b过来也是待使用
         * a是使用中的   b过来也是使用中
         * a是其他状态的时候  b过来 还是 b的状态,但是不可用要改为可用
         */
        if(oldTyDomain.getEnable().equals(DomainEnableEnum.USED.getCode())){
            oldTyDomain.setEnable(DomainEnableEnum.WAIT_USE.getCode());
            thirdDomainMapper.updateIgnoreNull(oldTyDomain);
            tyDomain.setEnable(DomainEnableEnum.USED.getCode());
            thirdDomainMapper.updateIgnoreNull(tyDomain);
        }else {
            tyDomain.setEnable(DomainEnableEnum.WAIT_USE.getCode());
            thirdDomainMapper.updateIgnoreNull(tyDomain);
        }

        // 清缓存

        String merchantName = "SYSTEM";
        if (null != tyDomain.getId()) {
            String tabStr = StringUtils.isNotBlank(tyDomain.getTab())? tyDomain.getTab() : TabEnum.TY.getName();
            String reallyMerchantName = merchantGroupMapper.getMerchantGroupNameByDomainId(tyDomain.getId(),tabStr);
            if (StringUtils.isNotBlank(reallyMerchantName)) {
                merchantName = reallyMerchantName;
            }
        }
        String finalMerchantName = merchantName;

        CompletableFuture.runAsync(()->{
            if(tab.equals(TabEnum.DJ.getName())){
                cpDJMerchantGroupService.sendDJMsg(finalMerchantName,0,"", DomainConstants.DOMAIN_CHANGE_AUTO);
            }else{
                List<Long> programIds = domainProgramMapper.getIdByDomainId(tyDomain.getId(),tab);
                if(CollectionUtils.isNotEmpty(programIds)){
                    for (Long programId : programIds) {
                        cpDJMerchantGroupService.clearCPCache(null,programId);
                    }
                }
            }
        });

    }

    @Override
    public void switchStatus(TyDomain tyDomain) {
        long id = tyDomain.getId();
        int status = tyDomain.getStatus();
        int enable = tyDomain.getEnable();
        if (status == 0) {
            if(enable == DomainEnableEnum.USED.getCode()){
                throw new BusinessException("正在使用的域名不可关闭！");
            }
            validate(id, "关闭",tyDomain.getTab());
        } else {
            if (enable != DomainEnableEnum.USED.getCode()) enable = DomainEnableEnum.WAIT_USE.getCode();
        }
        thirdDomainMapper.switchStatus(id, status, enable, System.currentTimeMillis(),tyDomain.getUpdateUser());

    }

    @Override
    public void switchSelfTestTag(TyDomain tyDomain) {
        tyDomain.setUpdateTime(System.currentTimeMillis());
        thirdDomainMapper.switchSelfTestTag(tyDomain);
    }

    @Override
    public List<?> getDomainByMerchantAndArea(Long merchantGroupId, String domainGroupCode) {
        List<DomainGroupApiVO> result = Lists.newArrayList();
        try {
            List<DomainGroupApiVO> dglist = thirdDomainMapper.getDomainGroupByMerchant(merchantGroupId);
            if (CollectionUtil.isNotEmpty(dglist)) {
                for (DomainGroupApiVO dg : dglist) {
                    List<DomainApiVO> dls = thirdDomainMapper.getDomainByDomainGroupId(dg.getId(), GroupTypeEnum.getTypeIntByCode(domainGroupCode));
                    if (CollectionUtil.isNotEmpty(dls)) {
                        dg.setDomainls(dls);
                    }
                }
            }
            result.addAll(dglist);
            log.info("getDomainByMerchantAndArea, merchantgroupid:{}, domaingroupcode:{}, result:{}", merchantGroupId, domainGroupCode, result);
            return result;
        } catch (Exception e) {
            log.error(merchantGroupId + "getDomainByMerchantAndArea异常：" + domainGroupCode, e);
            return result;
        }
    }

    @Override
    public List<TyDomain> getNewDomainByGroupId(DomainSqlParamTYDTO domainSqlParamTYDTO) {
        return thirdDomainMapper.getNewDomainByGroupId(domainSqlParamTYDTO);
    }

    @Override
    public List<TyDomain> getDomainListByLineId(Long id, String tab) {
        return thirdDomainMapper.getDomainListByLineId(id,tab);

    }

    @Override
    public List<TyDomain> getDomainListByNames(List<String> domainNameList, String tab) {
        return thirdDomainMapper.getDomainListByNames(domainNameList,tab);
    }

    @Override
    public List<TyDomain> getDomainListByIds(List<Long> domainIdList, String tab) {
        return thirdDomainMapper.getDomainListByIds(domainIdList,tab);
    }


    private void validate(Long domainId, String msg, String tab) {
        if (Objects.isNull(domainId)) {
            throw new BusinessException("域名不存在或已删除");
        }
        TyDomain tyDomain = thirdDomainMapper.getById(domainId);

        if(Objects.equals(tyDomain.getEnable(), DomainEnableEnum.USED.getCode())){
            throw new BusinessException("正在使用的域名不可"+msg);
        }

        List<Domain2DomainGroup> domain2DomainGroups = domain2DomainGroupMapper.selectByDomainId(domainId,tab);

        if (CollectionUtil.isNotEmpty(domain2DomainGroups)) {
            List<Long> ids = domain2DomainGroups.stream().map(Domain2DomainGroup::getDomainGroupId).collect(Collectors.toList());
            List<DomainGroupVO> domainGroupVOList = domainGroupMapper.getDomainGroupByIds(ids, tab);
            for (DomainGroupVO domainGroupVO : domainGroupVOList) {
                int domainCount = domain2DomainGroupMapper.countByGroupId(domainGroupVO.getId(),tab);
                long alarmThreshold = domainGroupVO.getThreshold(tyDomain.getDomainType());
                if (domainCount - 1 < alarmThreshold) {
                    throw new BusinessException("此域名无法" + msg + "，" + msg + "后此域名所在域名组【" + domainGroupVO.getDomainGroupName() + "】将低于预警值");
                }
            }
        }
    }

    @Override
    public List<TyDomain> getDomainByDomainByParam(DomainSqlParamDto domainSqlParamDto, TyDomain domain){
        return thirdDomainMapper.getDomainByDomainByParam(domainSqlParamDto,domain);
    }

    @Override
    public TyDomain selectById(Long id){
        return thirdDomainMapper.getById(id);
    }

}
