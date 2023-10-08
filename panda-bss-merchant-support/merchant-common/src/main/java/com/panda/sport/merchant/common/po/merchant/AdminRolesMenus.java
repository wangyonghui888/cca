package com.panda.sport.merchant.common.po.merchant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @auth: YK
 * @Description:用户角色菜单
 * @Date:2020/5/13 12:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminRolesMenus {

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 角色ID
     */
    private Long roleId;
}
