package com.panda.sport.merchant.common.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  账户中心/平台用户风控-查询用户风控分页列表请求DTO
 * @Date: 2022-04-09
 */
@Data
@Api(value = "账户中心/平台用户风控-查询用户风控分页列表请求DTO")
public class UserRiskControlPageQueryDTO implements Serializable {

    @ApiModelProperty(value = "当前页", example = "1")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示的记录数", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "商户编码")
    private String merchantCode;

    @ApiModelProperty(value = "用户名/用户id")
    private String userName;

    @ApiModelProperty(value = "风控类型,1.投注特征标签,2特殊限额,3特殊延时,4提前结算,5赔率分组,6投注特征预警变更标签,7定时任务自动化标签")
    private Integer type;

    @ApiModelProperty(value = "商户处理人")
    private String merchantOperator;

    @ApiModelProperty(value = "风控建议时间--开始时间")
    private String recommendStartTime;

    @ApiModelProperty(value = "风控建议时间--结束时间")
    private String recommendEndTime;

    @ApiModelProperty(value = "商户处理时间--开始时间")
    private String processStartTime;

    @ApiModelProperty(value = "商户处理时间--结束时间")
    private String processEndTime;

    @ApiModelProperty(value = "状态:0待处理,1同意,2拒绝,3强制执行")
    private Integer status;

    @ApiModelProperty(value = "排序列：1-平台建议时间 2-处理时间")
    private Integer orderKey;

    @ApiModelProperty(value = "排序方式：desc-降序 asc-升序")
    private String orderType;

}
