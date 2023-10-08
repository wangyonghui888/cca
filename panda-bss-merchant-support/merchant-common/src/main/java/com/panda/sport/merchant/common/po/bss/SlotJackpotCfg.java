package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 老虎机彩金配置表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-14
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slot_jackpot_cfg")
public class SlotJackpotCfg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 老虎机ID
     */
    @TableField("slot_machine_id")
    private Long slotMachineId;

    /**
     * 1：个位	2：十位	3：百位	4：千位
     */
    @TableField("place")
    private Integer place;

    /**
     * 数字：0-9
     */
    @TableField("number")
    private Integer number;

    /**
     * 出现概率
     */
    @TableField("appearance_rate")
    private BigDecimal appearanceRate;


}
