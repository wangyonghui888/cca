package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auth: YK
 * @Description:菜单
 * @Date:2020/5/13 12:33
 */
@Repository
public interface AdminMenuMapper extends BaseMapper<AdminMenu> {

    // 获取菜单
    List<AdminMenu> getMenu();

    List<AdminRole> getRoleAdmin(@Param("merchantCode") String merchantCode, @Param("isAdmin") Integer isAdmin);


    List<AdminRolesMenus> getRoleMenu(@Param("roleId") Long roleId);

    List<AdminRolesPermissions> getRolesPermissions(@Param("roleId") Long roleId);

    /**
     * 批量插入
     * @param rolesMenusList
     * @return
     */
    Integer insertBatchRolesMenus(List<AdminRolesMenus> rolesMenusList);



    Integer insertBatchRolesPermissions(List<AdminRolesPermissions> rolesPermissionsList);
}
