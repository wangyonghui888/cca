package com.oubao.vo;

import lombok.Data;


@Data
public class UserOperationVo extends BaseVO {
    /**
     * 会员等级
     */
    private Integer userLevel;
    /**
     * 消费大于
     */
    private Long betAmountMin;

    /**
     * 消费小于
     */
    private Long betAmountMax;
    /**
     * 注册时间-开始
     */
    private Long startTime;
    /**
     * 注册时间-结束
     */
    private Long endTime;
    /**
     * 是否黑名单
     **/
    private Integer disabled;

    /**
     * 数据来源(B端或C端)
     */
    private String srcType;

    /**
     * 来源(商户ID)
     */
    private String merchantId;

    /**
     * 币种
     */
    private String currencyCode;
}