package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.base.BasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author :  ives
 * @Description :  对账工具查询响应VO
 * @Date: 2022-02-06 19:21
 */
@Data
@Api(value = "对账工具查询响应VO")
public class CheckToolsQueryRespVO extends BasePage implements Serializable {

    private static final long serialVersionUID = 6006990774655593849L;

    @ApiModelProperty(value = "对账结果|0:核对结果不完全正确 |1:核对结果正确")
    private Integer checkResult;

    @ApiModelProperty(value = "对账结果为0 时，展示的信息")
    private String errMessage;

    @ApiModelProperty(value = "核对结果不完全正确时展示列表")
    private List<CheckToolsQueryDetailRespVO> checkList;

    @ApiModelProperty(value = "redis存储的key,用于修改记录用")
    private String redisKey;

    @ApiModelProperty(value = "日期类型：|1：账务日（默认）|2：自然日")
    private Integer dateType;
}
