package com.panda.sport.merchant.common.vo;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityConfigVO {

    /**
     * h5图片地址
     */
    private String h5Url;
    /**
     * pc图片地址
     */
    private String pcUrl;
    /**
     * 活动参与商户 1全部商户 ，2部分商户
     */
    private Integer activityMerchant;
    /**
     * 部分商户 列表
     */
    private String activityMerchants;
    /**
     * 活动类型配置 1全部活动，2幸运盲盒，3每日活动，4成长任务
     */
    private String activityType;

}
