package com.panda.sport.merchant.common.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ives
 * @description 维护记录表 DTO
 * @date 2022-03-25
 */
@Data
@Api(value = "维护记录表 DTO")
public class MaintenanceRecordDTO implements Serializable {

    private static final long serialVersionUID = -4615546002353308783L;
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "维护平台表id")
    private Long maintenancePlatformId;

    @ApiModelProperty(value = "所属项目标识")
    private String dataCode;

    @ApiModelProperty(value = "维护开始时间")
    private Long maintenanceStartTime;

    @ApiModelProperty(value = "维护结束时间")
    private Long maintenanceEndTime;

    @ApiModelProperty(value = "提前提醒用户时间(默认0)")
    private Integer remindTime;
}
