package com.panda.sport.merchant.common.vo.merchant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author :  ives
 * @Description :  超级管理员用户名修改VO
 * @Date: 2022-03-04 22:53
 */
@Data
@Api(value = "超级管理员用户名修改VO")
public class AdminUserNameUpdateReqVO {

    @ApiModelProperty(value = "商户表ID")
    @NotNull(message = "ID不允许为空！")
    private Long id;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不允许为空！")
    private String userName;


    @ApiModelProperty(value = "商户等级")
    private String agentLevel;
}
