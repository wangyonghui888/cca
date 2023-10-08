package com.panda.sport.merchant.common.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author :  ives
 * @Description :  账户中心/平台用户风控-查询用户风控分页列表响应VO
 * @Date: 2022-04-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Api(value = "账户中心/平台用户风控-查询用户风控列表响应VO")
public class UserRiskControlQueryAllRespVO{

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商户编号")
    private String merchantCode;

    @ApiModelProperty(value = "风控类型,1.投注特征标签,2特殊限额,3特殊延时,4提前结算,5赔率分组,6投注特征预警变更标签,7定时任务自动化标签")
    private Integer type;

    @ApiModelProperty(value = "风控建议时间")
    private Long recommendTime;

    @ApiModelProperty(value = "状态:0待处理,1同意,2拒绝,3强制执行")
    private Integer status;

    @ApiModelProperty(value = "风控建议设置值")
    private String recommendValue;

    @ApiModelProperty(value = "风控建议人")
    private String riskOperator;

    @ApiModelProperty(value = "接口请求数据(用于原接口请求 原json数据保存)")
    private String requestData;

    @ApiModelProperty(value = "商户处理时间")
    private Long processTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

    @ApiModelProperty(value = "用户ID")
    @ExcelProperty(value = "用户ID", index = 0)
    private String userId;

    @ApiModelProperty(value = "用户名")
    @ExcelProperty(value = "用户名", index = 1)
    private String userName;

    @ApiModelProperty(value = "风控类型,1.投注特征标签,2特殊限额,3特殊延时,4提前结算,5赔率分组,6投注特征预警变更标签,7定时任务自动化标签")
    @ExcelProperty(value = "风控类型", index = 2)
    private String typeStr;

    @ApiModelProperty(value = "商户后台显示值")
    @ExcelProperty(value = "平台建议设置值", index = 3)
    private String merchantShowValue;

    @ApiModelProperty(value = "风控补充说明")
    @ExcelProperty(value = "平台风控补充说明", index = 4)
    private String supplementExplain;

    @ApiModelProperty(value = "风控建议时间")
    @ExcelProperty(value = "平台建议时间", index = 5)
    private String recommendTimeStr;

    @ApiModelProperty(value = "状态:0待处理,1同意,2拒绝,3强制执行")
    @ExcelProperty(value = "处理状态", index = 6)
    private String statusStr;

    @ApiModelProperty(value = "商户处理人")
    @ExcelProperty(value = "处理人", index = 7)
    private String merchantOperator;

    @ApiModelProperty(value = "商户处理时间")
    @ExcelProperty(value = "处理时间", index = 8)
    private String processTimeStr;

    @ApiModelProperty(value = "商户处理说明")
    @ExcelProperty(value = "处理说明", index = 9)
    private String merchantRemark;

}
