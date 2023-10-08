package com.panda.multiterminalinteractivecenter.dto;

import lombok.Data;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.dto
 * @Description :  维护信息 对应发公共
 * @Date: 2022-04-16 13:04:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class MaintainRecordDto {

    /**
     * 发送公共系统编码 默认只要客户端才会推送此消息
     */
    private String systemCode;

    /**
     * 开始维护时间 时间戳
     */
    private Long maintenanceStartTime;

    /**
     * 计划结束维护时间 时间戳
     */
    private Long maintenanceEndTime;

    /**
     * 状态 0 有效状态 1 结束状态 维护结束后传 1.
     */
    private Integer status;
}
