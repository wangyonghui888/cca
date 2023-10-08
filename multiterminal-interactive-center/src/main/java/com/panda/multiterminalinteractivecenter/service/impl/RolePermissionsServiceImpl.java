package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.multiterminalinteractivecenter.entity.RolePermissions;
import com.panda.multiterminalinteractivecenter.entity.UserRoles;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.mapper.RolePermissionsMapper;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.RoleVo;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-11 15:10:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service
public class RolePermissionsServiceImpl extends ServiceImpl<RolePermissionsMapper, RolePermissions> implements IService<RolePermissions> {

    @Autowired
    private MerchantLogService merchantLogService;
    public void addRoleMenu(RoleVo roleVo, HttpServletRequest request){
        LambdaQueryWrapper<RolePermissions> queryWrapper = new QueryWrapper<RolePermissions>().lambda();
        queryWrapper.eq(roleVo != null,RolePermissions::getRoleId,roleVo.getId());
        List<RolePermissions> permissionsList = baseMapper.selectList(queryWrapper);
        baseMapper.delete(queryWrapper);
        if (CollectionUtil.isNotEmpty(roleVo.getPermissionsIds())){
            for (Long id : roleVo.getPermissionsIds()){
                RolePermissions rolePermissions = new RolePermissions();
                rolePermissions.setRoleId(roleVo.getId());
                rolePermissions.setPermissionId(id);
                baseMapper.insert(rolePermissions);
            }
        }
        String beforeValues = CollectionUtil.isNotEmpty(permissionsList) ? JSON.toJSONString(permissionsList.stream().map(RolePermissions::getPermissionId).collect(Collectors.toList())) : "-";
        String afterValues = CollectionUtil.isNotEmpty(roleVo.getPermissionsIds()) ? JSON.toJSONString(roleVo.getPermissionsIds()) : "-";
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("menuPermissionsSet"), beforeValues, afterValues);
        merchantLogService.saveLog(MerchantLogPageEnum.ROLE_MANAGE, MerchantLogTypeEnum.MENU_AUTH_SET, filedVO,  null, String.valueOf(roleVo.getId()), request);
    }

}
