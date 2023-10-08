package com.panda.sport.admin.vo;

import lombok.Data;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/7/3 17:06
 */
@Data
public class AdminPermissionVo {

    /**
     * 权限id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

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
