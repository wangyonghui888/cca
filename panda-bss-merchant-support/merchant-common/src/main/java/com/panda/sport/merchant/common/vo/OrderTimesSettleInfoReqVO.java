package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.base.BasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author :  amos
 * @Description :  二次结算查询请求参数
 * @Date: 2022-05-22 17:33
 */
@Data
@Api(value = "二次结算查询请求VO")
public class OrderTimesSettleInfoReqVO  extends BasePage  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间类型 1: 结算时间 2:最后一次账变时间 3:第一次账变时间", example = "1")
    @NotNull(message = "时间类型不能为空")
    private Integer timeType;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "开始时间不能为空")
    private Long endTime;

    @ApiModelProperty(value = "赛事ID")
    private String matchId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "商户名")
    private String merchantName;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "二次结算原因")
    private String remark;

    @ApiModelProperty(value = "多次账变金额差Min")
    private Long negativeAmountMin ;

    @ApiModelProperty(value = "多次账变金额差Max")
    private Long negativeAmountMax ;

    @ApiModelProperty(value = "用户当前账户金额Min")
    private Long amountMin;

    @ApiModelProperty(value = "用户当前账户金额Max")
    private Long amountMax;

    @ApiModelProperty(value = "语言")
    private String language;

    @ApiModelProperty(value = "联赛名称")
    private String matchName;
}

