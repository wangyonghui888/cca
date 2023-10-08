package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;


@Data
public class MaintenanceRecordOutDto {

    /**
     * 维护开始时间
     */
    private Long maintenanceStartTime;

    /**
     * 维护结束时间
     */
    private Long maintenanceEndTime;

    /**
     * 维护状态(默认无维护0，未开始1，维护中2，已结束3)
     */
    private int maintenanceStatus;

    /**
     * 所属项目
     */
    private String serverCode;

}
