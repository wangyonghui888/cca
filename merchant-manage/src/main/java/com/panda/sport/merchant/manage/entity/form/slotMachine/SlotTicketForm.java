package com.panda.sport.merchant.manage.entity.form.slotMachine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author baylee
 * @version 1.0
 * @date 02/18/22 16:07:11
 */
@Getter
@Setter
@ToString
public class SlotTicketForm {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 奖券名称
     */
    @NotBlank(message = "ticketName can not be empty")
    private String ticketName;

    /**
     * 奖券类型	1：基础奖券（不可编辑）	2：游戏奖券（可配置）
     */
    @Min(value = 1, message = "ticketType must be 1 or 2")
    @Max(value = 2, message = "ticketType must be 1 or 2")
    @NotNull(message = "ticketType can not be null")
    private Integer ticketType;

    /**
     * 合成材料：基础奖券ID
     */
    @Min(value = 1, message = "baseTicketId must be greater than or equal to 1")
    @NotNull(message = "baseTicketId can not be null")
    private Integer baseTicketId;

    /**
     * 合成率
     */
    @Min(value = 0,message = "syntheticRate must be greater than or equal to 0")
    @Max(value = 100,message = "syntheticRate must be less than or equal to 100")
    @NotNull(message = "syntheticRate can not be null")
    private Integer syntheticRate;

    /**
     * 单次合成材料数
     */
    @Min(value = 1,message = "baseTicketNumber must be greater than or equal to 1")
    @NotNull(message = "baseTicketNumber can not be null")
    private Integer baseTicketNumber;

    /**
     * 返还率
     */
    @Min(value = 0,message = "returnRate must be greater than or equal to 0")
    @Max(value = 100,message = "returnRate must be less than or equal to 100")
    @NotNull(message = "returnRate can not be null")
    private Integer returnRate;

    /**
     * 返还奖券ID
     */
    @Min(value = 1,message = "returnTicketId must be greater than or equal to 1")
    @NotNull(message = "returnTicketId can not be null")
    private Integer returnTicketId;

    /**
     * 幸运奖券提升合成概率
     */
    @Min(value = 0,message = "syntheticImproveRate must be greater than or equal to 0")
    @Max(value = 100,message = "syntheticImproveRate must be less than or equal to 100")
    @NotNull(message = "syntheticImproveRate can not be null")
    private Integer syntheticImproveRate;

    /**
     * 单次提升合成概率消耗幸运奖券数
     */
    @Min(value = 1,message = "syntheticImproveNumber must be greater than or equal to 1")
    @NotNull(message = "syntheticImproveNumber can not be null")
    private Integer syntheticImproveNumber;

    // /**
    //  * 0：关闭	1：开启
    //  */
    // @Min(value = 0, message = "state must be 0 or 1")
    // @Max(value = 1, message = "state must be 0 or 1")
    // @NotNull(message = "state can not be null")
    // private Integer state;
}
