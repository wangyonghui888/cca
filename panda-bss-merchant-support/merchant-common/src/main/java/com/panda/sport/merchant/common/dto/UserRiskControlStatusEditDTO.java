package com.panda.sport.merchant.common.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  账户中心/平台用户风控-修改用户风控状态DTO
 * @Date: 2022-04-09
 */
@Data
@Api(value = "账户中心/平台用户风控-修改用户风控状态DTO")
public class UserRiskControlStatusEditDTO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商户处理人")
    private String merchantOperator;

    @ApiModelProperty(value = "状态:0待处理,1同意,2拒绝,3强制执行")
    private Integer status;

    @ApiModelProperty(value = "商户处理说明")
    private String merchantRemark;
}
