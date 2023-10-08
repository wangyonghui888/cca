package com.panda.sport.merchant.common.po.bss;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 *
 * </p>
 *
 * @author Auto Generator
 * @since 2020-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TMerchantKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String merchantId;
    private String parentName;

    /**
     * 商户私钥
     */
    private String key;

    /**
     * 商户原始历史密钥
     */
    private String historyKey;

    /**
     * 第二密钥
     */
    private String secondMerchantKey;

    /**
     * 第二历史密钥
     */
    private String secondHistoryKey;

    /**
     * 商户原始密钥启用/禁用时间
     */
    private Long enableTime;

    /**
     * 商户第二密钥启用/禁用时间
     */
    private Long secondEnableTime;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 原始密钥状态(0-禁用,1-启用)
     */
    private Integer keyStatus;

    /**
     * 第二密钥状态(0-禁用,1-启用)
     */
    private Integer secondStatus;

    /**
     * 状态
     */
    private String statusDescription;



    private String merchantCode;

    private String merchantName;

    private Integer agentLevel;

    private Integer childConnectMode;

    private Integer directSale;

    private String createdBy;

    private String createTime;

    private String updatedBy;

    private String updateTime;


}
