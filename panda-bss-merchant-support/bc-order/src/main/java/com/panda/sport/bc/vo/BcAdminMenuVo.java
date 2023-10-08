package com.panda.sport.bc.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName AdminMenuVo
 * @auth YK
 * @Description 菜单栏
 * @Date 2020-09-01 11:35
 * @Version
 */
@Setter
@Getter
public class BcAdminMenuVo {

    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 英文
     */
    private String en;

    /**
     * 用户父级
     */
    private Long pid;

    /**
     * 路径
     */
    private String path;

    private String icon;

    /**
     * 排序，越小越前
     */
    private Long sort;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 父级
     */
    private String parentName;

    /**
     * 子级
     */
    private List<BcAdminMenuVo> children;
}
