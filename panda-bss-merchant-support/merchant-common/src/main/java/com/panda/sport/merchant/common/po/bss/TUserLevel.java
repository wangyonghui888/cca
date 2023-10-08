package com.panda.sport.merchant.common.po.bss;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员等级表
 * @author admin
 */
@Data
public class TUserLevel implements Serializable {

    @ApiModelProperty(value = "会员等级id")
    private Integer levelId;

    @ApiModelProperty(value = "名称(普通、白银、黄金、钻石)")
    private String levelName;

    @ApiModelProperty(value = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "状态，1为有效。0为无效，默认1")
    private Integer status;

    @ApiModelProperty(value = "背景色")
    private String bgColor;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "提前结算动态Margin")
    private BigDecimal extraMargin;
}
