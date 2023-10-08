package com.panda.sport.merchant.common.po.bcorder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName AdminMenu
 * @auth YK
 * @Description
 * @Date 2020-09-01 14:15
 * @Version
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "admin_menu")
public class BcAdminMenu {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单英文名
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

}
