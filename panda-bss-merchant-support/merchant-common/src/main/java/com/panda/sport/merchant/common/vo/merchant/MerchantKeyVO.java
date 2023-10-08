package com.panda.sport.merchant.common.vo.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantKeyVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户状态
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
     * 原始密钥
     */
    private String merchantKey;

    /**
     * 第二密钥
     */
    private String secondMerchantKey;

    /**
     * 商户原始历史密钥
     */
    private String historyKey;

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
     * 商户证书数量
     */
    private Integer merchantKeyCount;

    /**
     * 商户有效期起始时间
     */
    private String startTime;
    private String endTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改时间
     */
    private String updateTime;
}
