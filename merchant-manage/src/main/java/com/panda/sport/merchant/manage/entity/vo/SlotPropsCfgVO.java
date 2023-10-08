package com.panda.sport.merchant.manage.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author baylee
 * @version 1.0
 * @date 02/19/22 17:10:50
 */
@Getter
@Setter
@ToString
public class SlotPropsCfgVO {
    /**
     * 主键ID
     */
    @NotNull(message = "props id can not be null")
    private Long id;
    /**
     * 1：奖金倍率卡	2：幸运奖券
     */
    @NotNull(message = "propsType can not be null")
    private Integer propsType;
    /**
     * 道具名称
     */
    @NotEmpty(message = "propsName can not be empty")
    private String propsName;
    /**
     * 奖金倍数或者幸运奖券数
     */
    @NotNull(message = "bonusMultiple can not be null")
    private BigDecimal bonusMultiple;
    /**
     * 道具概率
     */
    @NotNull(message = "propsRate can not be null")
    private BigDecimal propsRate;
}
