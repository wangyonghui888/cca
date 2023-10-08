package com.panda.center.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/29 16:21:58
 */
@Getter
@Setter
@ToString
public class AcMerParam {
    /**
     * 商户开启的活动，活动ID逗号分割
     */
    private String activityId;

    /**
     * 商户ID
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantAccount;

    /**
     * 状态(0,关闭;1,开启)
     */
    private String status = "0";

    /**
     * 活动入口状态 0：关闭 1：开启
     */
    private Long entranceStatus;
}
