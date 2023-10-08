package com.panda.sport.bc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.panda.sport.bc.security.JwtUser;
import com.panda.sport.merchant.common.po.bcorder.BcAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName JwtUserDetailsService
 * @auth YK
 * @Description jwt用户
 * @Date 2020-09-01 12:11
 * @Version
 */
@Service
public class BcJwtUserDetailsService implements UserDetailsService {

    @Autowired
    private BcAdminUserServiceImpl adminUserService;

    @Autowired
    private BcAdminRolesPermissionsServiceImpl adminRolesPermissionsService;

    @Autowired
    private BcAdminRolesMenusServiceImpl adminRolesMenusService;

    /**
    * @description: 获取用户
    * @Param: [username]
    * @return: org.springframework.security.core.userdetails.UserDetails
    * @author: YK
    * @date: 2020/9/11 12:05
    */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BcAdminUser bcAdminUser = adminUserService.getOne(new QueryWrapper<BcAdminUser>().eq("username", username));
        if (bcAdminUser != null) {
            return createJwtUser(bcAdminUser);
        }
        return null;
    }


    /**
    * @description: 创建jwt用户并返回给前端
    * @Param: [adminUser]
    * @return: org.springframework.security.core.userdetails.UserDetails
    * @author: YK
    * @date: 2020/9/1 12:20
    */
    public UserDetails createJwtUser(BcAdminUser adminUser) {
        return new JwtUser(
                adminUser.getId(),
                adminUser.getUsername(),
                adminUser.getPassword(),
                adminUser.getAvatar(),
                adminUser.getEmail(),
                adminUser.getPhone(),
                adminRolesPermissionsService.mapToGrantedAuthorities(adminUser),
                adminRolesMenusService.mapToGrantedMenu(adminUser),
                adminUser.getEnabled() == 0 ? false : true,
                adminUser.getCreateTime(),
                adminUser.getLastPasswordResetTime()
        );
    }
}
