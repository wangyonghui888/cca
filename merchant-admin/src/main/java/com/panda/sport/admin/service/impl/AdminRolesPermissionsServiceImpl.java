package com.panda.sport.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.admin.service.AdminRolesPermissionsService;
import com.panda.sport.merchant.common.po.merchant.AdminPermission;
import com.panda.sport.merchant.common.po.merchant.AdminRolesPermissions;
import com.panda.sport.merchant.common.po.merchant.AdminUser;
import com.panda.sport.merchant.mapper.AdminRolesPermissionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/12 15:52
 */
@Service
public class AdminRolesPermissionsServiceImpl extends ServiceImpl<AdminRolesPermissionsMapper, AdminRolesPermissions> implements AdminRolesPermissionsService {


    @Autowired
    private AdminPermissionServiceImpl adminPermissionService;

    /**
     * 获取用户权限
     *
     * @param adminUser
     * @return
     */
    @Override
    public Collection<GrantedAuthority> mapToGrantedAuthorities(AdminUser adminUser) {

        QueryWrapper<AdminRolesPermissions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", adminUser.getRoleId());
        List<AdminRolesPermissions> adminRolesPermissions = list(queryWrapper);
        Set<Long> permissionId = adminRolesPermissions.stream().map(e -> e.getPermissionId()).collect(Collectors.toSet());
        if (permissionId.size() > 0) {
            QueryWrapper<AdminPermission> adminPermissionQueryWrapper = new QueryWrapper<>();
            adminPermissionQueryWrapper.in("id", permissionId);
            List<AdminPermission> adminPermissionList = adminPermissionService.list(adminPermissionQueryWrapper);
            return adminPermissionList.size() > 0 ?
                    adminPermissionList.stream().map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toList())
                    : null;
        }
        return null;
    }
}
