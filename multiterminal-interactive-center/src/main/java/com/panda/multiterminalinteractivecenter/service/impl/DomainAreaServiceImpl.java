package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.DomainArea;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.mapper.DomainAreaMapper;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainAreaServiceImpl extends ServiceImpl<DomainAreaMapper, DomainArea> implements IService<DomainArea> {

    private final DomainAreaMapper domainAreaMapper;
    private final MerchantLogService merchantLogService;


    public APIResponse<?> pageList(Integer page, Integer size, String name) {
        if (page==null)   page = 1;
        if (size==null)   size = 20;
        PageHelper.startPage(page, size, true);
        List<DomainArea> list = domainAreaMapper.pageList(page,size,name);
        PageInfo<DomainArea> poList = new PageInfo<>(list);
        return APIResponse.returnSuccess(poList);
    }

    public APIResponse<?> simpleList(String name) {
        return APIResponse.returnSuccess(domainAreaMapper.simpleList(name));
    }

    public void create(DomainArea domainArea, HttpServletRequest request) {

        int count = domainAreaMapper.countByName(domainArea.getName(),null);
        if(count!=0){
            throw new BusinessException("该名称已存在");
        }
        long now = System.currentTimeMillis();
        domainArea.setCreateTime(now);
        domainArea.setUpdateTime(now);
        domainArea.setDeleteTag(0);
        baseMapper.insert(domainArea);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("areaName"), "-", domainArea.getName());
        merchantLogService.saveLog(MerchantLogPageEnum.AREA_MANAGE, MerchantLogTypeEnum.SAVE, filedVO,  null, domainArea.getName(), request);
    }
    public void edit(DomainArea domainArea, HttpServletRequest request) {
        int count = domainAreaMapper.countByName(domainArea.getName(),domainArea.getId());
        if(count!=0){
            throw new BusinessException("该名称已存在");
        }
        MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_INFO_STATUS;
        String beforeValue = domainArea.getStatus() == 1 ? "禁用" : " 启用",
               afterValue =  domainArea.getStatus() == 1 ? "启用" : " 禁用",
               fieldName = MerchantFieldUtil.FIELD_MAPPING.get("status");
        DomainArea old = domainAreaMapper.getById(domainArea.getId());
        if(StringUtils.isNotBlank(domainArea.getName())) {
            beforeValue = old.getName();
            afterValue = domainArea.getName();
            fieldName = MerchantFieldUtil.FIELD_MAPPING.get("areaName");
            typeEnum = MerchantLogTypeEnum.EDIT;
        }

        domainArea.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(domainArea);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(fieldName, beforeValue, afterValue);
        merchantLogService.saveLog(MerchantLogPageEnum.AREA_MANAGE, typeEnum, filedVO,  null,
                old.getName()+ StringPool.AMPERSAND + old.getCode()+ StringPool.AMPERSAND + old.getId(), request);
    }

    public void delete(Long id, HttpServletRequest request) {
        DomainArea old = domainAreaMapper.getById(id);
        domainAreaMapper.logicDeleteById(id);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("areaName"), old.getName(), "-");
        merchantLogService.saveLog(MerchantLogPageEnum.AREA_MANAGE, MerchantLogTypeEnum.DEL, filedVO,  null,
                old.getName()+ StringPool.AMPERSAND + old.getCode()+ StringPool.AMPERSAND + old.getId(), request);
    }

    public APIResponse getDomainAreaList(){

        return APIResponse.returnSuccess(domainAreaMapper.selectList(null));
    }
}