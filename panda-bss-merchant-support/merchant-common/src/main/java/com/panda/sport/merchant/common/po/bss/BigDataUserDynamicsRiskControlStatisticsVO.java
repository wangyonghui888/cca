package com.panda.sport.merchant.common.po.bss;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BigDataUserDynamicsRiskControlStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "大数据推送类型| 1 提前结算 2 赔率分组", example = "1")
    private Integer bigDataPushType;

    @ApiModelProperty(value = "主键ID", example = "123123")
    private Long id;

    @ApiModelProperty(value = "用户ID", example = "123123")
    private Long uid;

    @ApiModelProperty(value = "t_user的user_level字段", example = "123123")
    private Integer userLevel;

    @ApiModelProperty(value = "商户编码", example = "123123")
    private String merchantCode;

    @ApiModelProperty(value = "提前结算投注笔数", example = "123123")
    private Integer preOrderCount;

    @ApiModelProperty(value = "提前结算胜率", example = "12.33")
    private BigDecimal preWinRate;

    @ApiModelProperty(value = "提前结算盈利率", example = "12.33")
    private BigDecimal preProfitRate;

    @ApiModelProperty(value = "总投注笔数", example = "123123")
    private Integer totalOrderCount;

    @ApiModelProperty(value = "总胜率", example = "12.33")
    private BigDecimal totalWinRate;

    @ApiModelProperty(value = "总盈利率", example = "12.33")
    private BigDecimal totalProfitRate;

    @ApiModelProperty(value = "总盈利金额", example = "12.33")
    private BigDecimal totalProfit;

}
