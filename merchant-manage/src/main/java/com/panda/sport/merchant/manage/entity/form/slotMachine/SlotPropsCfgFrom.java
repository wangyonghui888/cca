package com.panda.sport.merchant.manage.entity.form.slotMachine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.manage.entity.vo.SlotPropsCfgVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 02/19/22 17:01:58
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotPropsCfgFrom {
    /**
     * 老虎机ID
     */
    @NotNull(message = "id can not be null")
    private Long id;
    /**
     * 道具消耗奖券ID
     */
    @NotNull(message = "ticketId can not be null")
    private Long ticketId;

    /**
     * 奖券名称
     */
    @NotEmpty(message = "ticketName can not be empty")
    private String ticketName;

    /**
     * 重置消耗奖券数
     */
    @Min(value = 1, message = "请输入大于0的整数值")
    private Integer resetTicketNumber;

    /**
     * 道具列表
     */
    @Valid
    private List<SlotPropsCfgVO> props;
}
