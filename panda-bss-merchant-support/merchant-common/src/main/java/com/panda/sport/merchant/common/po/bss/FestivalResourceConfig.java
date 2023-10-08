package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 节日 banner 配置表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-01-29
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("t_festival_resource_config")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FestivalResourceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 多语言code
     */
    @TableField("language_code")
    private String languageCode;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * PC白天版
     */
    @TableField("pc_daytime_link")
    private String pcDaytimeLink;

    /**
     * H5白天版
     */
    @TableField("h5_daytime_link")
    private String h5DaytimeLink;

    /**
     * PC黑夜版
     */
    @TableField("pc_night_link")
    private String pcNightLink;

    /**
     * H5黑夜版
     */
    @TableField("h5_night_link")
    private String h5NightLink;

    /**
     * 是否覆盖越南语资源 0：false 1：true
     */
    @TableField("cover_vi")
    private Integer coverVi;

    /**
     * 是否启用 0：false 1：true
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Long startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Long endTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Long modifyTime;


}
