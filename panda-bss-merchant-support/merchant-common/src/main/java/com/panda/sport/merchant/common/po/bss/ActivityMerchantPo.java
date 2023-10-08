package com.panda.sport.merchant.common.po.bss;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityMerchantPo {

    /**
     * id
     */
    private Long id;

    /**
     * activity_id
     */
    private Long activityId;

    /**
     * 活动类型(1常规活动 2自定义活动 3特殊活动)
     */
    private String merchantCode;

    /**
     * 状态(0，关闭;1，开启)
     */
    private Integer status;
    /**
     * 状态(0，关闭;1，开启)
     */
    private Integer entranceStatus;
}
