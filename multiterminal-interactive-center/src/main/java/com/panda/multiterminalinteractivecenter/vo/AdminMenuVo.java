package com.panda.multiterminalinteractivecenter.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @auth: YK
 * @Description:获取菜单
 * @Date:2020/5/16 13:53
 */
@Setter
@Getter
public class AdminMenuVo {


    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    private String nameEn;

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
    private List<AdminMenuVo> children;
}
