package com.panda.sport.admin.service;

import com.panda.sport.merchant.common.po.merchant.AdminUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AdminRolesPermissionsService {

     Collection<GrantedAuthority> mapToGrantedAuthorities(AdminUser adminUser);

}
