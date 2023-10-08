package com.panda.sport.merchant.common.vo.merchant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  获取查询条件设置响应VO
 * @Date: 2022-01-24 18:20
 */
@Data
@Api(value = "获取查询条件设置响应VO")
public class QueryConditionSettingQueryRespVO implements Serializable {

    private static final long serialVersionUID = 3833488056179397273L;

    @ApiModelProperty(value = "商户编码")
    private String merchantCode;

    @ApiModelProperty(value = "默认时间类型|1 投注时间|2 开赛时间 |3 结算时间，默认为2 开赛时间")
    private Integer defaultTimeType;

    @ApiModelProperty(value = "默认是否勾选自然日|0 不勾选 |1 勾选，默认为1 勾选")
    private Integer isNatureDay;
}
