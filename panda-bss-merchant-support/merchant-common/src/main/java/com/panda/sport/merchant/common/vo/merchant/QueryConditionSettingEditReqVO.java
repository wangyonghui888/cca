package com.panda.sport.merchant.common.vo.merchant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  获取查询条件设置响应VO
 * @Date: 2022-01-24 18:20
 */
@Data
@Api(value = "获取查询条件设置响应VO")
public class QueryConditionSettingEditReqVO implements Serializable {


    private static final long serialVersionUID = 7852520769836030511L;

    @ApiModelProperty(value = "商户编码")
    @NotBlank(message = "商户编码不允许为空！")
    private String merchantCode;

    @ApiModelProperty(value = "默认时间类型|1 投注时间|2 开赛时间 |3 结算时间，默认为2 开赛时间")
    @Range(min = 1,max = 3,message = "默认时间类型为1-3")
    private Integer defaultTimeType;

    @ApiModelProperty(value = "默认是否勾选自然日|0 不勾选 |1 勾选，默认为1 勾选")
    @Range(min = 0,max = 1,message = "默认时间类型为0-1")
    private Integer isNatureDay;

    @ApiModelProperty(value = "商户后台重置密码|0 关 |1 开，默认为1 开")
    @Range(min = 0,max = 1,message = "默认商户后台重置密码为0-1")
    private Integer resetPasswordSwitch;

    /**
     * 异常用户名单点击时间
     */
    private Long abnormalClickTime;
}
