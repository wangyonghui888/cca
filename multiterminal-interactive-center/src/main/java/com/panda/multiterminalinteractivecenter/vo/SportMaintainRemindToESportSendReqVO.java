package com.panda.multiterminalinteractivecenter.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  体育维护提醒写入电竞请求VO
 * @Date: 2022-03-08 17:18
 */
@Data
@Api(value = "体育维护提醒写入电竞请求VO")
public class SportMaintainRemindToESportSendReqVO implements Serializable {

    private static final long serialVersionUID = -2355097406892758605L;

    @ApiModelProperty(value = "商户ID(电竞平台提供)", example = "31433517168705439")
    private Long merchant;

    @ApiModelProperty(value = "维护开始时间(时间戳单位秒)", example = "1123123")
    private Long maintenance_start_time;

    @ApiModelProperty(value = "维护结束时间(时间戳单位秒)", example = "1123123")
    private Long maintenance_end_time;

    @ApiModelProperty(value = "间隔(分钟)", example = "1")
    private Integer reminder_time;

    @ApiModelProperty(value = "开关|1开,2关", example = "1")
    private Integer status;

}
