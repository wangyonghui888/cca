package com.panda.sport.bc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bc.service.BcAdminMenuService;
import com.panda.sport.bc.vo.BcAdminPermissionVo;

import com.panda.sport.bc.mapper.BcAdminMenuMapper;

import com.panda.sport.bc.mapper.BcAdminPermissionMapper;
import com.panda.sport.bc.mapper.BcAdminRoleMapper;
import com.panda.sport.bc.mapper.BcAdminRolesPermissionsMapper;
import com.panda.sport.merchant.common.po.bcorder.BcAdminMenu;
import com.panda.sport.merchant.common.po.bcorder.BcAdminPermission;
import com.panda.sport.merchant.common.po.bcorder.BcAdminRole;
import com.panda.sport.merchant.common.po.bcorder.BcAdminRolesPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AdminMenuServiceImpl
 * @auth YK
 * @Description： 用户菜单
 * @Date 2020-09-01 14:59
 * @Version
 */
@Service
public class BcAdminMenuServiceImpl extends ServiceImpl<BcAdminMenuMapper, BcAdminMenu> implements BcAdminMenuService {

    @Autowired
    private BcAdminPermissionMapper adminPermissionMapper;

    @Autowired
    private BcAdminRoleMapper adminRoleMapper;

    @Autowired
    private BcAdminRolesPermissionsMapper adminRolesPermissionsMapper;

    /**
     * @description: 用户菜单分配角色
     * @Param: [menuIds]
     * @return: java.util.List<com.panda.sport.bc.vo.BcAdminPermissionVo>
     * @author: YK
     * @date: 2020/9/3 12:03
     */
    @Override
    public List<BcAdminPermissionVo> getPermissionInMenuIds(List<String> menuIds) {

        List<BcAdminPermission> adminPermissionList = adminPermissionMapper.getPermissionInMenuIds(menuIds);
        List<BcAdminPermissionVo> adminPermissionVoList = new ArrayList<>();
        for (BcAdminPermission adminPermission : adminPermissionList) {
            BcAdminPermissionVo adminPermissionVo = new BcAdminPermissionVo();
            BeanUtils.copyProperties(adminPermission, adminPermissionVo);
            adminPermissionVoList.add(adminPermissionVo);
        }
        return adminPermissionVoList;
    }

    /**
    * @description: 分配权限
    * @Param: []
    * @return: void
    * @author: YK
    * @date: 2020/9/11 12:06
    */
    @Override
    public void allocatePermisson() {

        List<BcAdminRole> adminRoleList = adminRoleMapper.selectList(new QueryWrapper<>());
        List<BcAdminPermission> adminPermissionList = adminPermissionMapper.selectList(new QueryWrapper<BcAdminPermission>().gt("pid",0));
        for (BcAdminRole adminRole : adminRoleList) {
            for (BcAdminPermission adminPermission : adminPermissionList) {
                BcAdminRolesPermissions adminRolesPermissions = new BcAdminRolesPermissions().setRoleId(adminRole.getId()).setPermissionId(adminPermission.getId());
                BcAdminRolesPermissions appResult = adminRolesPermissionsMapper.selectOne(new QueryWrapper<BcAdminRolesPermissions>().eq("role_id",adminRole.getId()).eq("permission_id",adminPermission.getId()));
                if (StringUtils.isEmpty(appResult)) {
                    adminRolesPermissionsMapper.insert(adminRolesPermissions);
                }
            }
        }

    }
}
