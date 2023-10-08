package com.panda.sport.merchant.common.po.bss;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserLevelRelationStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "大数据推送类型| 1 提前结算 2 赔率分组", example = "1")
    private Integer bigDataPushType;

    @ApiModelProperty(value = "用户ID", example = "123123")
    private Long uid;

    @ApiModelProperty(value = "t_user的user_level字段", example = "123123")
    private Integer userLevel;

    @ApiModelProperty(value = "商户编码", example = "123123")
    private String merchantCode;

    @ApiModelProperty(value = "提前结算投注笔数", example = "123123")
    private Integer preOrderCount;

    @ApiModelProperty(value = "总投注笔数", example = "123123")
    private Integer totalOrderCount;

    @ApiModelProperty(value = "胜率extra_margin")
    private BigDecimal winRateExtraMargin;

    @ApiModelProperty(value = "盈利率extra_margin")
    private BigDecimal profitRateExtraMargin;

    @ApiModelProperty(value = "胜率组别")
    private Integer winRateGroupNum;

    @ApiModelProperty(value = "盈利率组别")
    private Integer profitRateGroupNum;

    @ApiModelProperty(value = "extra_margin结果")
    private BigDecimal extraMarginResult;

    @ApiModelProperty(value = "组别结果")
    private Integer groupNumResult;

}
