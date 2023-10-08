package com.panda.sport.bc.service;


import com.panda.sport.merchant.common.po.bcorder.BcAdminUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
* @description:  角色所对应的权限
* @Param:
* @return:
* @author: YK 
* @date: 2020/9/1 12:25
*/
public interface BcAdminRolesPermissionsService {

    /**
    * @description: 获取角色对应的权限
    * @Param: [bcAdminUser]
    * @return: java.util.Collection<org.springframework.security.core.GrantedAuthority>
    * @author: YK
    * @date: 2020/9/11 12:05
    */
    Collection<GrantedAuthority> mapToGrantedAuthorities(BcAdminUser bcAdminUser);

}
