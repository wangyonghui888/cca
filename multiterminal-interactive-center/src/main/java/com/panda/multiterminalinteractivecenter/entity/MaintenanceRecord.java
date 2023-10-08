package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 维护记录表
 * @author duwan
 * @date 2022-03-18
 */
@Data
@TableName("m_maintenance_record")
public class MaintenanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 维护平台表id
     */
    private Long maintenancePlatformId;

    /**
     * 维护开始时间
     */
    private Long maintenanceStartTime;
    /**
     * 实际开始时间
     */
    private Long realStartTime;
    /**
     * 计划结束时间
     */
    private Long planEndTime;

    /**
     * 维护结束时间
     */
    private Long maintenanceEndTime;

    /**
     * 提前提醒用户时间(默认0)
     */
    private Integer remindTime;

    /**
     * 是否提醒(0,不提醒,1,提醒)
     */
    private Integer isRemind;
    /**
     * 叠加时间(默认0)
     */
    private Integer superimposeTime;

    /**
     * 是否发送维护公告
     */
    private Integer isSendNotice;

    /**
     * 公告发送状态|0 未发送 |1已发送，默认未发送
     */
    private Integer noticeSendStatus;

    /**
     * 是否维护时自动踢用户
     */
    private Integer isKickUser=0;

    /**
     * 维护状态(默认无维护0，未开始1，维护中2，已结束3)
     */
    private Integer maintenanceStatus=0;

    /**
     * 创建时间
     */
    private Long createTime =0L;

    /**
     * 提醒状态0未提醒 1已提醒
     */
    private Integer remindStatus;

    /**
     * 修改时间
     */
    private Long updateTime;

    @TableField(exist = false)
    private String serverName;

    /**
     * 踢除用户类型1 2 3 4
     */
    @TableField(exist = false)
    private String kickUserType;
    public MaintenanceRecord() {
    }



}
