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
 * 老虎机奖券字典表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-14
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slot_ticket_dict")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotTicketDict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 奖券名称
     */
    @TableField("ticket_name")
    private String ticketName;

    /**
     * 奖券类型	1：基础奖券（不可编辑）	2：游戏奖券（可配置）
     */
    @TableField("ticket_type")
    private Integer ticketType;

    /**
     * 合成材料：基础奖券ID
     */
    @TableField("base_ticket_id")
    private Integer baseTicketId;

    /**
     * 合成率
     */
    @TableField("synthetic_rate")
    private Integer syntheticRate;

    /**
     * 单次合成材料数
     */
    @TableField("base_ticket_number")
    private Integer baseTicketNumber;

    /**
     * 返还率
     */
    @TableField("return_rate")
    private Integer returnRate;

    /**
     * 返还奖券ID
     */
    @TableField("return_ticket_id")
    private Integer returnTicketId;

    /**
     * 幸运奖券提升合成概率
     */
    @TableField("synthetic_improve_rate")
    private Integer syntheticImproveRate;

    /**
     * 单次提升合成概率消耗幸运奖券数
     */
    @TableField("synthetic_improve_number")
    private Integer syntheticImproveNumber;

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
