package com.panda.multiterminalinteractivecenter.service.impl;

import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.entity.DataPermissions;
import com.panda.multiterminalinteractivecenter.entity.Permissions;
import com.panda.multiterminalinteractivecenter.entity.Role;
import com.panda.multiterminalinteractivecenter.entity.User;
import com.panda.multiterminalinteractivecenter.mapper.DataRolePermissionsMapper;
import com.panda.multiterminalinteractivecenter.mapper.PermissionsMapper;
import com.panda.multiterminalinteractivecenter.vo.LoginUserVo;
import com.panda.multiterminalinteractivecenter.vo.PermissionsVo;
import com.panda.multiterminalinteractivecenter.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-13 13:50:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
public class LoginServiceImpl {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private PermissionsServiceImpl permissionsService;

    @Autowired
    private DataPermissionsServiceImpl dataPermissionsService;

    @Autowired
    private MenuServiceImpl menuService;
    @Autowired
    private PermissionsMapper permissionsMapper;
    @Autowired
    private DataRolePermissionsMapper dataRolePermissionsMapper;

    public User findUserByName(String username){
        return userService.findUserByName(username);
    }

    public LoginUserVo creatLoginUserVoByName(String username){
        LoginUserVo userVo = LocalCacheService.userCacheMap.getIfPresent(username);
        if (userVo != null){
            return userVo;
        }else {
            userVo = new LoginUserVo();
        }
        User user = userService.findUserByName(username);
        if (user == null){
            return null;
        }
        userVo.setUsername(user.getName());
        List<Role> roles = roleService.findRoleByUserId(user.getId());
        if (!CollectionUtils.isEmpty(roles)){
            List<RoleVo> roleVos = new ArrayList<>(roles.size());
            for (Role role : roles){
                RoleVo roleVo = new RoleVo();
                roleVo.setId(role.getId());
                roleVo.setRoleName(role.getRoleName());
                roleVos.add(roleVo);
            }
            RoleVo roleVo = new RoleVo();
            List<Permissions> permissions = new ArrayList<>();
            List<DataPermissions> dataPermissions = new ArrayList<>();
            for(RoleVo role : roleVos) {
                if("超级管理员".equals(role.getRoleName())){
                    permissions = permissionsMapper.selectAll();
                    dataPermissions = dataRolePermissionsMapper.selectAll();
                    break;
                }
                List<Permissions> permissions1 = permissionsService.findPermissionsByRoleId(role.getId());
                for (Permissions permissions2 : permissions1){
                    if (!permissions.contains(permissions2)){
                        permissions.add(permissions2);
                    }
                }
                List<DataPermissions> dataPermissions1 = dataPermissionsService.findDataPermissionsByRoleId(role.getId());
                for (DataPermissions dataPermissions2 : dataPermissions1){
                    if (!dataPermissions.contains(dataPermissions2)){
                        dataPermissions.add(dataPermissions2);
                    }
                }
            }
            roleVo.setDataPermissions(dataPermissions);
            List<PermissionsVo> list = menuService.getMenuTree(permissions);
            roleVo.setPermissions(permissions);
            roleVo.setMenuTree(list);
            List<RoleVo> roleVos2 = new ArrayList<>(roles.size());
            roleVos2.add(roleVo);
            userVo.setRoles(roleVos2);
        }else {
            userVo.setRoles(new ArrayList<>());
        }
        //LocalCacheService.userCacheMap.put(userVo.getUsername(), userVo);
        return userVo;
    }

}
