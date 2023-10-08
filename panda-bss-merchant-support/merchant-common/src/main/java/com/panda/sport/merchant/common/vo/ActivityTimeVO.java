package com.panda.sport.merchant.common.vo;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityTimeVO {

    /**
     * 2幸运盲盒，3每日活动，4成长任务 5老虎机
     */
    //private String activityType="2,3,4";
    /**
     * 每日任务开始时间
     */
    private Long dailyTaskStartTime;
    /**
     * 每日任务结束时间
     */
    private Long dailyTaskEndTime;
    /**
     * 每日常驻活动开始时间
     */
    private Long dailyResidentStartTime;

    /**
     * 0关闭，1开启
     */
    private Integer dailyActivityEnd = 0;

    /**
     * 成长任务开始时间 常驻活动直接传0
     */
    private Long growthTaskStartTime;
    /**
     * 成长任务结束时间 常驻活动直接传0
     */
    private Long growthTaskEndTime;
    /**
     * 成长常驻活动开始时间
     */
    private Long growthResidentStartTime;
    /**
     * 0关闭，1开启
     */
    private Integer growthActivityEnd = 0;

    /**
     * 幸运盲盒开始时间 常驻活动直接传0
     */
    private Long blindBoxStartTime;
    /**
     * 幸运盲盒结束时间 常驻活动直接传0
     */
    private Long blindBoxEndTime;
    /**
     * 幸运盲盒常驻活动开始时间
     */
    private Long blindResidentStartTime;
    /**
     * 0关闭，1开启
     */
    private Integer blindActivityEnd = 0;

    /**
     * 老虎机开始时间 常驻活动直接传0
     */
    private Long tigerStartTime;
    /**
     * 老虎机结束时间 常驻活动直接传0
     */
    private Long tigerEndTime;
    /**
     * 老虎机常驻活动开始时间
     */
    private Long tigerResidentStartTime;
    /**
     * 老虎机活动结束tag   0关闭，1开启
     */
    private Integer tigerActivityEnd = 0;

}
