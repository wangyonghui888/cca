package com.panda.sport.merchant.common.po.bcorder;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName AdminRolesMenus
 * @auth YK
 * @Description 用户角色菜单
 * @Date 2020-09-01 14:17
 * @Version
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "admin_roles_menus")
public class BcAdminRolesMenus {

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 角色ID
     */
    private Long roleId;
}
