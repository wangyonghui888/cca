package com.panda.sport.merchant.common.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author :  ives
 * @Description :  对账工具查询细节响应VO
 * @Date: 2022-02-06 19:21
 */
@Data
@Api(value = "对账工具查询细节响应VO")
@Accessors(chain = true)
public class CheckToolsQueryDetailRespVO implements Serializable {

    private static final long serialVersionUID = 2114746961912206314L;

    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    @ApiModelProperty(value = "商户Code")
    private String merchantCode;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户名 ")
    private String userName;

    @ApiModelProperty(value = "日期")
    private String financeDate;

    @ApiModelProperty(value = "日期类型 : 日 1  day ,月 2  month")
    private String dateTimeType;

    @ApiModelProperty(value = "注单币种")
    private String currency;

    @ApiModelProperty(value = "正确注单数")
    private Integer correctOrderSum;

    @ApiModelProperty(value = "正确投注用户数")
    private Integer correctBetUserSum;

    @ApiModelProperty(value = "原始的正确投注金额")
    private BigDecimal originalCorrectBetAmountSum;

    @ApiModelProperty(value = "原始的正确盈利金额")
    private BigDecimal originalCorrectProfitSum;

    @ApiModelProperty(value = "正确投注金额")
    private BigDecimal correctBetAmountSum;

    @ApiModelProperty(value = "正确有效投注金额")
    private BigDecimal correctValidBetAmountSum;

    @ApiModelProperty(value = "正确盈利金额")
    private BigDecimal correctProfitSum;

    @ApiModelProperty(value = "对账单核对结果|0:错误 |1：正确")
    private Integer financeCheckResult;

    @ApiModelProperty(value = "商户投注统计核对结果|0:错误 |1：正确")
    private Integer userBetSummaryCheckResult;
}
