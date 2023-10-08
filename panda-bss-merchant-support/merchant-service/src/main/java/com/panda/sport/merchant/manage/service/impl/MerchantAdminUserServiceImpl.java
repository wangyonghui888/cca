package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.merchant.*;
import com.panda.sport.merchant.common.utils.EncryptUtils;
import com.panda.sport.merchant.manage.service.MerchantAdminUserService;
import com.panda.sport.merchant.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth: YK
 * @Description:创建用户管理员
 * @Date:2020/5/16 19:33
 */
@Service("merchantAdminUserService")
@Slf4j
public class MerchantAdminUserServiceImpl implements MerchantAdminUserService {

    @Autowired
    AdminRoleMapper adminRoleMapper;

    @Autowired
    AdminMenuMapper adminMenuMapper;

    @Autowired
    AdminPermissionMapper adminPermissionMapper;

    @Autowired
    AdminRolesMenusMapper adminRolesMenusMapper;

    @Autowired
    AdminUserMapper adminUserMapper;

    @Autowired
    AdminRolesPermissionsMapper adminRolesPermissionsMapper;

    /**
     * 创建用户
     *
     * @param
     * @param
     * @return
     */
    @Override
    public int createAdminUser(MerchantPO merchantPO) {
        AdminUser adminUserResult = adminUserMapper.selectOne(new QueryWrapper<AdminUser>().eq("username", merchantPO.getMerchantAdmin()));
        if (adminUserResult != null) {
            return updateAdminUser(merchantPO);
        } else {
            try {
                List<AdminRole> adminRoleList = adminRoleMapper.selectList(new QueryWrapper<AdminRole>().eq("name", "超级管理员").eq("merchant_code", merchantPO.getMerchantCode()));
                Long roleId;
                if (adminRoleList.size() <= 0) {
                    // 插入用户角色
                    AdminRole adminRole = new AdminRole();
                    adminRole.setName("超级管理员");
                    adminRole.setMerchantCode(merchantPO.getMerchantCode());
                    adminRole.setCreateTime(DateUtil.now());
                    adminRole.setAuthorizedTime(DateUtil.now());
                    adminRole.setIsAdmin(1);
                    adminRoleMapper.insert(adminRole);

                    roleId = adminRole.getId();

                    //查询所有菜单，并分配菜单给该角色
                    List<AdminMenu> adminMenuList = adminMenuMapper.selectList(new QueryWrapper<>());

                    List<Long> menuIdList = new ArrayList<>();

                    for (AdminMenu adminMenu : adminMenuList) {

                        // 此菜单要求所有对外商户不展示
                        if("平台用户风控".equals(adminMenu.getName())){
                            continue;
                        }

                        // 只有渠道商户才有二级商户管理,其余都没有
                        if (merchantPO.getAgentLevel() != 1 && "二级商户管理".equals(adminMenu.getName())) {
                            continue;
                        }

                        if(merchantPO.getAgentLevel() == 2 && "电子账单".equals(adminMenu.getName())){
                            continue;
                        }

                        if (merchantPO.getMerchantTag() != null) {
                            if(merchantPO.getMerchantTag() == 0 && "取消注单".equals(adminMenu.getName()) ) {
                                continue;
                            }
                        } else {
                            if ( "取消注单".equals(adminMenu.getName() )) {
                                continue;
                            }
                        }

                        // 只有代理商才有渠道商户管理
                        if (merchantPO.getAgentLevel() != 10 && "渠道商户管理".equals(adminMenu.getName())) {
                            continue;
                        }

                        // 代理商账户没有 我的证书 和 操作日志查询
                        if (merchantPO.getAgentLevel() == 10) {
                            if ( "我的证书".equals(adminMenu.getName()) || "操作日志查询".equals(adminMenu.getName())) {
                                continue;
                            }
                        }
                        menuIdList.add(adminMenu.getId());
                    }


                    menuIdList.forEach(d -> {
                        AdminRolesMenus arm = new AdminRolesMenus();
                        arm.setRoleId(adminRole.getId());
                        arm.setMenuId(d);
                        adminRolesMenusMapper.insert(arm);
                    });


                    //查询所有权限，并分配权限给该角色
                    List<AdminPermission> adminPermissionList = adminPermissionMapper.selectList(new QueryWrapper<AdminPermission>().in("menu_id", menuIdList));
                    for (AdminPermission adminPermission : adminPermissionList) {
                        AdminRolesPermissions arp = new AdminRolesPermissions();
                        arp.setPermissionId(adminPermission.getId());
                        arp.setRoleId(adminRole.getId());
                        adminRolesPermissionsMapper.insert(arp);
                    }
                } else {
                    roleId = adminRoleList.get(0).getId();
                }

                // 插入用户
                AdminUser adminUser = new AdminUser();
                adminUser.setUsername(merchantPO.getMerchantAdmin());
                adminUser.setPassword(EncryptUtils.encryptMd5(merchantPO.getAdminPassword()));
                adminUser.setAvatar(merchantPO.getLogo());
                adminUser.setEnabled(1);
                adminUser.setMerchantId(merchantPO.getId());
                adminUser.setMerchantCode(merchantPO.getMerchantCode());
                adminUser.setMerchantName(merchantPO.getMerchantName());
                adminUser.setParentMerchantCode(merchantPO.getParentCode());
                adminUser.setAgentLevel(merchantPO.getAgentLevel());
                adminUser.setRoleId(roleId);
                adminUser.setIsAdmin(1);
                adminUser.setCreateTime(DateUtil.now());
                adminUser.setLastPasswordResetTime(DateUtil.now());
                adminUser.setPswCode(merchantPO.getAdminpswCode());//密码明码
                adminUserMapper.insert(adminUser);
                return 1;
            } catch (Exception e) {
                log.error("插入后台管理用户失败:" + merchantPO, e);
                return 0;
            }
        }
    }


    /**
     * 更新
     *
     * @param merchantPO
     * @return
     */
    private int updateAdminUser(MerchantPO merchantPO) {

        if (merchantPO == null || StringUtils.isAnyEmpty(merchantPO.getAdminPassword(), merchantPO.getMerchantAdmin())) {
            return 0;
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(merchantPO.getMerchantAdmin());
        adminUser.setPassword(EncryptUtils.encryptMd5(merchantPO.getAdminPassword()));
        adminUser.setPswCode(merchantPO.getAdminpswCode());//明码
        adminUser.setLastPasswordResetTime(DateUtil.now());
        try {
            QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", merchantPO.getMerchantAdmin());
            queryWrapper.eq("merchant_code", merchantPO.getMerchantCode());
            adminUserMapper.update(adminUser, queryWrapper);
            return 1;
        } catch (Exception e) {
            log.info("更新后台管理用户失败:{}", e.getMessage());
            return 0;
        }
    }
}
