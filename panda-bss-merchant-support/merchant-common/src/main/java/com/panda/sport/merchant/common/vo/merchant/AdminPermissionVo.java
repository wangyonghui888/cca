package com.panda.sport.merchant.common.vo.merchant;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户权限
 */
@Setter
@Getter
public class AdminPermissionVo {

    /**
     * 权限id
     */
    private Long id;

    /**
     * 名称 英文名
     */
    private String nameEn;

    /**
     * 权限名称
     */
    private String alias;

    /**
     * 菜单id
     */
    private Long menuId;
}
