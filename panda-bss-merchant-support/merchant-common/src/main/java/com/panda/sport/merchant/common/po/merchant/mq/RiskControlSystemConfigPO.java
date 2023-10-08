package com.panda.sport.merchant.common.po.merchant.mq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  动态风控-赔率分组-系统配置消费PO
 * @Date: 2022-04-11
 */
@Data
@Api(value = "动态风控-赔率分组-系统配置消费PO")
public class    RiskControlSystemConfigPO implements Serializable {

    @ApiModelProperty(value = "赔率分组动态风控开关 0关 1开", example = "1")
    private Integer tagMarketLevelStatus;

    /*@ApiModelProperty(value = "提前结算动态风控开关 0关 1开", example = "1")
    private Integer preSettlementStatus;

    @ApiModelProperty(value = "投注延时动态风控开关 0关 1开", example = "1")
    private Integer betDelayStatus;

    @ApiModelProperty(value = "投注限额动态风控开关 0关 1开", example = "1")
    private Integer betLimitStatus;*/
}
