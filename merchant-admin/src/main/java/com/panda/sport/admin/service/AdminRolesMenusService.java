package com.panda.sport.admin.service;

import com.panda.sport.admin.vo.AdminMenuVo;
import com.panda.sport.merchant.common.po.merchant.AdminUser;

import java.util.List;

/**
 * @auth: YK
 * @Description:菜单所属权限
 * @Date:2020/5/13 12:42
 */
public interface AdminRolesMenusService {

    List<AdminMenuVo> mapToGrantedMenu(AdminUser adminUser);
}
