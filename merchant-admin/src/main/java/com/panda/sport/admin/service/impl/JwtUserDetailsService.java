package com.panda.sport.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.merchant.common.po.merchant.AdminUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @auth: YK
 * @Description:
 * @Date:2020/5/10 17:17
 */
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserServiceImpl adminUserService;

    @Autowired
    private AdminRolesPermissionsServiceImpl adminRolesPermissionsService;

    @Autowired
    private AdminRolesMenusServiceImpl adminRolesMenusService;


    /**
     * 根据用户名访问
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AdminUser adminUser = adminUserService.getOne(new QueryWrapper<AdminUser>().eq("username", username));
        if (adminUser != null) {
            return createJwtUser(adminUser);
        }
        return null;
    }


    public UserDetails createJwtUser(AdminUser adminUser) {

        return new JwtUser(
                adminUser.getId(),
                adminUser.getUsername(),
                adminUser.getPassword(),
                adminUser.getAvatar(),
                adminUser.getEmail(),
                adminUser.getPhone(),
                adminUser.getMerchantId(),
                adminUser.getMerchantCode(),
                adminUser.getMerchantName(),
                0,
                adminUser.getParentMerchantCode(),
                adminUser.getAgentLevel(),
                adminUser.getIsAdmin(),
                adminRolesPermissionsService.mapToGrantedAuthorities(adminUser),
                adminRolesMenusService.mapToGrantedMenu(adminUser),
                adminUser.getEnabled() == 0 ? false : true,
                adminUser.getCreateTime(),
                adminUser.getLastPasswordResetTime()
        );
    }

}
