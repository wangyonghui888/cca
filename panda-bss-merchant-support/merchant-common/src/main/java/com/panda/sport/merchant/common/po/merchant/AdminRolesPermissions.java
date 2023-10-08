package com.panda.sport.merchant.common.po.merchant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @auth: YK
 * @Description:用户角色分配权限
 * @Date:2020/5/12 14:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminRolesPermissions {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;
}
