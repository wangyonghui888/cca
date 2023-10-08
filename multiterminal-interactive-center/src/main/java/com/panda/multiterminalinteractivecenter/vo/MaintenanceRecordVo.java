package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.util.List;


@Data
public class MaintenanceRecordVo {
    /**
     * 维护平台表id
     */
    private List<Long> maintenancePlatformIds;

    /**
     * 维护平台表id
     */
    private Long maintenancePlatformId;
    /**
     * 维护开始时间
     */
    private Long maintenanceStartTime;

    /**
     * 维护结束时间
     */
    private Long maintenanceEndTime;

    /**
     * 实际开始时间
     */
    private Long realStartTime;
    /**
     * 提前提醒用户时间(默认0)
     */
    private Integer remindTime;
    /**
     * 是否提醒(0,不提醒,1,提醒)
     */
    private int isRemind;

    /**
     * 叠加时间(默认0)
     */
    private Integer superimposeTime;

    /**
     * 是否发送维护公告
     */
    private int isSendNotice;

    /**
     * 是否维护时自动踢用户
     */
    private int isKickUser;

    /**
     * 维护状态(默认无维护0，未开始1，维护中2，已结束3)
     */
    private int maintenanceStatus;

    /**
     * 所属项目
     */
    private String dataCode;

}
