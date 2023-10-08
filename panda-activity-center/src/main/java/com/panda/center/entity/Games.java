package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 游戏信息表
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_games")
public class Games implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏ID
     */
    @TableId("id")
    private Long id;

    /**
     * 简称
     */
    @TableField("short_name")
    private String shortName;

    /**
     * 英文名称
     */
    @TableField("en_name")
    private String enName;

    /**
     * 中文名称
     */
    @TableField("cn_name")
    private String cnName;

    /**
     * 状态 开启-1关闭-0
     */
    @TableField("status")
    private Integer status;

    /**
     * 排序码
     */
    @TableField("sort_code")
    private Integer sortCode;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Integer createTime;

    /**
     * 创建人ID
     */
    @TableField("create_by_id")
    private Long createById;

    /**
     * 创建人名称
     */
    @TableField("create_by_name")
    private String createByName;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Integer updateTime;

    /**
     * 修改人ID
     */
    @TableField("update_by_id")
    private Long updateById;

    /**
     * 修改人名称
     */
    @TableField("update_by_name")
    private String updateByName;

    /**
     * 是否显示：0=隐藏，1=显示
     */
    @TableField("visible")
    private Integer visible;


}
