package com.panda.sport.merchant.common.po.bcorder;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName AdminRolesPermissions
 * @auth YK
 * @Description
 * @Date 2020-09-01 14:18
 * @Version
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "admin_roles_permissions")
public class BcAdminRolesPermissions {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;
}
