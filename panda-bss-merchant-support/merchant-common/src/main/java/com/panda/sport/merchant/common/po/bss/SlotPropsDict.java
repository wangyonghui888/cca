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
import java.math.BigDecimal;

/**
 * <p>
 * 老虎机道具字典表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-14
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slot_props_dict")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotPropsDict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 1：奖金倍率卡	2：幸运奖券
     */
    @TableField("props_type")
    private Integer propsType;

    /**
     * 道具名称
     */
    @TableField("props_name")
    private String propsName;

    /**
     * 奖金倍数或者幸运奖券数
     */
    @TableField("bonus_multiple")
    private BigDecimal bonusMultiple;

    /**
     * 0：关闭	1：开启
     */
    @TableField("state")
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 最后修改时间
     */
    @TableField("last_update_time")
    private Long lastUpdateTime;


}
