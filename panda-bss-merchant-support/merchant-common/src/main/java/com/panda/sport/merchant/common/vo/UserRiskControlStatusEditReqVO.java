package com.panda.sport.merchant.common.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  账户中心/平台用户风控-修改用户风控状态请求VO
 * @Date: 2022-04-09
 */
@Data
@Api(value = "账户中心/平台用户风控-修改用户风控状态请求VO")
public class UserRiskControlStatusEditReqVO implements Serializable {

    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "商户处理人")
    private String merchantOperator;

    @ApiModelProperty(value = "状态:0待处理,1同意,2拒绝,3强制执行")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "商户处理说明")
    private String merchantRemark;
}
