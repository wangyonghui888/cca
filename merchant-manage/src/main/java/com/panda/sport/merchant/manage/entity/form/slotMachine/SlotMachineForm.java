package com.panda.sport.merchant.manage.entity.form.slotMachine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author baylee
 * @version 1.0
 * @date 02/16/22 14:16:42
 */
@Getter
@Setter
@ToString
public class SlotMachineForm {

    /**
     * 老虎机ID
     */
    private Long id;
    /**
     * 老虎机名称
     */
    @NotBlank(message = "slotMachineName can not be empty")
    private String slotMachineName;

    /**
     * 老虎机抽奖消耗奖券ID
     */
    @NotNull(message = "slotTicketId can not be null")
    private Integer slotTicketId;

    /**
     * 单次抽奖消耗奖券数
     */
    @NotNull(message = "lotteryNumber can not be null")
    private Integer lotteryNumber;
}
