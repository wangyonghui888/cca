package com.panda.sport.merchant.common.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  对账工具修正请求VO
 * @Date: 2022-02-06 19:21
 */
@Data
@Api(value = "对账工具修正请求VO")
public class CheckToolsEditReqVO implements Serializable {


    private static final long serialVersionUID = -5664847109159430371L;
    @ApiModelProperty(value = "redis存储的key,用于修改记录用")
    @NotBlank
    private String redisKey;

    @ApiModelProperty(value = "日期类型：|1：账务日（默认）|2：自然日，回传查询列表的结果值")
    @Range(min = 1, max = 2,message = "日期类型默认为1-2")
    private Integer dateType;
}
