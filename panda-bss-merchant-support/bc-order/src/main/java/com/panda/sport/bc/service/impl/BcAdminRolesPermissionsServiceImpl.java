package com.panda.sport.bc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bc.service.BcAdminRolesPermissionsService;
import com.panda.sport.bc.mapper.BcAdminRolesPermissionsMapper;

import com.panda.sport.merchant.common.po.bcorder.BcAdminPermission;
import com.panda.sport.merchant.common.po.bcorder.BcAdminRolesPermissions;
import com.panda.sport.merchant.common.po.bcorder.BcAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName AdminRolesPermissionsServiceImpl
 * @auth YK
 * @Description
 * @Date 2020-09-01 12:26
 * @Version
 */
@Service
public class BcAdminRolesPermissionsServiceImpl extends ServiceImpl<BcAdminRolesPermissionsMapper, BcAdminRolesPermissions> implements BcAdminRolesPermissionsService {

    @Autowired
    private BcAdminPermissionServiceImpl adminPermissionService;


    /**
     * @description: 获取用户的权限
     * @Param: [adminUser]
     * @return: java.util.Collection<org.springframework.security.core.GrantedAuthority>
     * @author: YK
     * @date: 2020/9/1 15:19
     */
    @Override
    public Collection<GrantedAuthority> mapToGrantedAuthorities(BcAdminUser adminUser) {

        QueryWrapper<BcAdminRolesPermissions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", adminUser.getRoleId());
        List<BcAdminRolesPermissions> adminRolesPermissions = list(queryWrapper);
        Set<Long> permissionId = adminRolesPermissions.stream().map(e -> e.getPermissionId()).collect(Collectors.toSet());
        if (permissionId.size() > 0) {
            QueryWrapper<BcAdminPermission> adminPermissionQueryWrapper = new QueryWrapper<>();
            adminPermissionQueryWrapper.in("id", permissionId);
            List<BcAdminPermission> adminPermissionList = adminPermissionService.list(adminPermissionQueryWrapper);
            return adminPermissionList.size() > 0 ?
                    adminPermissionList.stream().map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toList())
                    : null;
        }
        return null;
    }
}
