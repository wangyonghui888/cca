package com.panda.sport.merchant.common.vo;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityStatusVO {

    /**
     * 活动商户
     */
    private String activityMerchants;
    /**
     * 状态 0关闭，1开启
     */
    private Integer status;

}
