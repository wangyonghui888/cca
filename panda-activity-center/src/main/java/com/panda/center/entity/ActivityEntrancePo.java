package com.panda.center.entity;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityEntrancePo {

    /**
     * 活动商户关联id
     */
    private Long id;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 活动入口状态
     */
    private Integer entranceStatus;
    /**
     * 活动状态
     */
    private Integer status;
    /**
     * 活动设置
     */
    private Integer activityStatus;
    /**
     * 活动名词
     */
    private String name;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 活动id
     */
    private Long activityId;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 开始时间
     */
    private String inStartTime;
    /**
     * 结束时间
     */
    private String inEndTime;
    /**
     * 商户编码
     */
    private String merchantCode;
    /**
     * 活动状态
     */
    private Integer type;
}
