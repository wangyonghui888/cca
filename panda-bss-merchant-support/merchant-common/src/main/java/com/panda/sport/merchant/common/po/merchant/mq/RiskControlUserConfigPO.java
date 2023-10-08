package com.panda.sport.merchant.common.po.merchant.mq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  动态风控-赔率分组-用户配置消费PO
 * @Date: 2022-04-11
 */
@Data
@Api(value = "动态风控-赔率分组-用户配置消费PO")
public class RiskControlUserConfigPO implements Serializable {

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "赔率分组动态风控开关 0关 1开", example = "1")
    private Integer tagMarketLevelStatus;
}
