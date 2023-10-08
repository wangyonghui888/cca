package com.panda.sport.merchant.common.vo;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityMerchantVO {
    /**
     * id
     */
    private Long id;
    /**
     * 活动id
     */
    private Long activityId;
    /**
     * 活动入口开关 0关闭，1开启
     */
    private Integer entranceStatus;
    /**
     * 活动入口开关 0关闭，1开启
     */
    private Integer status;
    /**
     * 活动配置类型 1全部活动，2幸运盲盒，3每日活动，4成长任务 5 老虎机活动
     */
    private String activityType;
    /**
     * 商户code
     */
    private String merchantCode;
    /**
     * 活动名称
     */
    private String name ;

}
