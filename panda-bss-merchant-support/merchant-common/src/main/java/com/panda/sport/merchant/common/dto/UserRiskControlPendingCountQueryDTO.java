package com.panda.sport.merchant.common.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  账户中心/平台用户风控-获取待处理状态的风控消息数量请求DTO
 * @Date: 2022-04-09
 */
@Data
@Api(value = "账户中心/平台用户风控-获取待处理状态的风控消息数量请求DTO")
public class UserRiskControlPendingCountQueryDTO implements Serializable {

    @ApiModelProperty(value = "商户编码")
    private String merchantCode;
}
