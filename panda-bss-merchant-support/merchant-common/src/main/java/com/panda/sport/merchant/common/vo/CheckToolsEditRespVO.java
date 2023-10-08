package com.panda.sport.merchant.common.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  对账工具修正响应VO
 * @Date: 2022-02-06 19:21
 */
@Data
@Api(value = "对账工具修正响应VO")
public class CheckToolsEditRespVO implements Serializable {


    private static final long serialVersionUID = 2662645294814739820L;
    @ApiModelProperty(value = "修正结果|0:修正失败 |1:修正成功")
    private Integer checkResult;

    @ApiModelProperty(value = "修正结果为0 时，展示的信息")
    private String errMessage;

}
