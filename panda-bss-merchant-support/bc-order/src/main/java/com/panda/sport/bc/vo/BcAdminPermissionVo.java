package com.panda.sport.bc.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName AdminPermissionVo
 * @auth YK
 * @Description 用户权限
 * @Date 2020-09-01 14:48
 * @Version
 */
@Setter
@Getter
public class BcAdminPermissionVo {

    /**
     * 权限id
     */
    private Long id;

    /**
     * 权限名称
     */
    private String alias;

    /**
     * 菜单id
     */
    private Long menuId;
}
