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
 * 老虎机游戏配置表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-14
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slot_machine_cfg")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotMachineCfg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 老虎机名称
     */
    @TableField("slot_machine_name")
    private String slotMachineName;

    /**
     * 老虎机抽奖消耗奖券ID
     */
    @TableField("slot_ticket_id")
    private Integer slotTicketId;

    /**
     * 单次抽奖消耗奖券数
     */
    @TableField("lottery_number")
    private Integer lotteryNumber;

    /**
     * 0：不限制次数	大于0：每日可参与游戏次数
     */
    @TableField("daily_game_number")
    private Integer dailyGameNumber;

    /**
     * 道具配置
     */
    @TableField("props_cfg")
    private String propsCfg;

    /**
     * 0：关闭	1：开启
     */
    @TableField("state")
    private Integer state;

    /**
     * 排序字段：客户端老虎机排序
     */
    @TableField("sort_no")
    private Integer sortNo;

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
