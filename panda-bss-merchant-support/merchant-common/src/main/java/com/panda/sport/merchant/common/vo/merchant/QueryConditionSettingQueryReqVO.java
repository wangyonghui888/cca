package com.panda.sport.merchant.common.vo.merchant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  获取查询条件设置请求VO
 * @Date: 2022-01-24 18:20
 */
@Data
@Api(value = "获取查询条件设置请求VO")
public class QueryConditionSettingQueryReqVO implements Serializable {
    private static final long serialVersionUID = -1110974478398148001L;

    @ApiModelProperty(value = "商户编码")
    @NotBlank(message = "商户编码不允许为空！")
    public String merchantCode;
}
