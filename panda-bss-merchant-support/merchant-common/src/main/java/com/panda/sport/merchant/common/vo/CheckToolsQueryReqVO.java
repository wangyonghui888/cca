package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.base.BasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author :  ives
 * @Description :  对账工具查询请求VO
 * @Date: 2022-02-06 19:21
 */
@Data
@Api(value = "对账工具查询请求VO")
public class CheckToolsQueryReqVO extends BasePage implements Serializable {

    private static final long serialVersionUID = -8647185173034480483L;

    @ApiModelProperty(value = "体育类型：|3:VR体育 |4:体育赛事(电竞)| 5:活动，默认所有体育")
    private Integer managerCode;

    @ApiModelProperty(value = "VIP等级：|0：非VIP |1：VIP用户，默认所有用户")
    private Integer vipLevel;

    @ApiModelProperty(value = "时间类型：| 1：投注时间 | 2：结算时间，影响返回什么字段")
    @Range(min = 1, max = 2,message = "日期类型默认为1-2")
    private Integer timeType;

    @ApiModelProperty(value = "日期类型：|1：账务日（默认）|2：自然日")
    @Range(min = 1, max = 2,message = "日期类型默认为1-2")
    private Integer dateType;

    @ApiModelProperty(value = "开始日期")
    @NotBlank(message = "开始日期不允许为空！")
    private String startDate;

    @ApiModelProperty(value = "结束日期")
    @NotBlank(message = "结束日期不允许为空！")
    private String endDate;

    @ApiModelProperty(value = "日期类型 : 日 1  day ,月 2  month")
    private String dateTimeType;

    @ApiModelProperty(value = "商户Code列表")
    private List<String> merchantCodeList;

    @ApiModelProperty(value = "商户Code")
    private String merchantCode;

    @ApiModelProperty(value = "商户类型/代理级别(0,直营;1:渠道;2:二级代理)，默认所有商户")
    private Integer agentLevel;

    @ApiModelProperty(value = "用户ID")
    private String userId ;

    @ApiModelProperty(value = "用户列表")
    List<Long> userIdList ;

    @ApiModelProperty(value = "用户名")
    private String  userName;

    @ApiModelProperty(value = "商户类型/代理级别(0,直营;1:渠道;2:二级代理)，默认所有商户,渠道商户的要携带二级商户的数据")
    private List<Integer> agentLevelList;

    @ApiModelProperty(value = "注单币种，默认全部")
    private Integer currency;
}