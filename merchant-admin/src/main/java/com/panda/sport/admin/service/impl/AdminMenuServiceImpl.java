package com.panda.sport.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.admin.service.AdminMenuService;
import com.panda.sport.admin.vo.AdminPermissionVo;
import com.panda.sport.bss.mapper.MerchantConfigMapper;
import com.panda.sport.merchant.common.po.merchant.*;
import com.panda.sport.merchant.mapper.AdminMenuMapper;
import com.panda.sport.merchant.mapper.AdminPermissionMapper;
import com.panda.sport.merchant.mapper.AdminRoleMapper;
import com.panda.sport.merchant.mapper.AdminRolesPermissionsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/13 13:49
 */
@Service
public class AdminMenuServiceImpl extends ServiceImpl<AdminMenuMapper, AdminMenu> implements AdminMenuService {

    /**
     * 获取权限
     */
    @Autowired
    private AdminPermissionMapper adminPermissionMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private AdminRolesPermissionsMapper adminRolesPermissionsMapper;

    @Autowired
    private MerchantConfigMapper merchantConfigMapper;


    /**
     * 获取菜单的权限
     * @param menuIds
     * @return
     */
    @Override
    public List<AdminPermissionVo> getPermissionInMenuIds(List<String> menuIds) {

        List<AdminPermission> adminPermissionList = adminPermissionMapper.getPermissionInMenuIds(menuIds);
        List<AdminPermissionVo> adminPermissionVoList = new ArrayList<>();
        for (AdminPermission adminPermission : adminPermissionList) {
            AdminPermissionVo adminPermissionVo = new AdminPermissionVo();
            BeanUtils.copyProperties(adminPermission,adminPermissionVo);
            adminPermissionVoList.add(adminPermissionVo);
        }
        return adminPermissionVoList;
    }


    /**
     * 权限分配
     *
     */
    @Override
    public void allocatePermisson() {

        List<AdminRole> adminRoleList = adminRoleMapper.selectList(new QueryWrapper<>());
        List<AdminPermission> adminPermissionList = adminPermissionMapper.selectList(new QueryWrapper<AdminPermission>().gt("pid",0));
        for (AdminRole adminRole : adminRoleList) {
            for (AdminPermission adminPermission : adminPermissionList) {
                AdminRolesPermissions adminRolesPermissions = new AdminRolesPermissions().setRoleId(adminRole.getId()).setPermissionId(adminPermission.getId());
                AdminRolesPermissions appResult = adminRolesPermissionsMapper.selectOne(new QueryWrapper<AdminRolesPermissions>().eq("role_id",adminRole.getId()).eq("permission_id",adminPermission.getId()));
                if (StringUtils.isEmpty(appResult)) {
                    adminRolesPermissionsMapper.insert(adminRolesPermissions);
                }
            }
        }
    }

    /**
     * 获取是不是信用网商户
     * @param merchantCode
     * @return
     */
    @Override
    public String getMerchantCredit (String merchantCode) {
        return merchantConfigMapper.getCreditMerchant(merchantCode);
    }
}
