package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @auth: YK
 * @Description:菜单
 * @Date:2020/5/13 12:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "admin_menu")
public class AdminMenu {


    @TableId(value = "id", type = IdType.AUTO)
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

}
