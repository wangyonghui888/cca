package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;

/**
 * @Author: Jeffrey
 * @Date: 2020/1/23
 */
@Data
public class MerchantLevelVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 商户等级
     */
    private Integer level;
    /**
     * 等级名称
     */
    private String levelName;
    /**
     * 费率ID
     */
    private String rateId;
    /**
     * 金额范围
     */
    private Long rangeAmountBegin;
    private Long rangeAmountEnd;
    /**
     * 平台费率
     */
    private Double terraceRate;


    private Integer paymentCycle;
    /**
     * 会员费
     */
    private Long vipAmount;
    /**
     * 技术费用
     */
    private Long techniqueAmount;
    /**
     * 技术费用缴纳周期
     */
    private Integer techniquePaymentCycle;
    /**
     * VIP费用缴纳周期
     */
    private Integer vipPaymentCycle;

    /**
     * 计算模式(1,投注;2盈利)
     */
    private String computingStandard;

    private String logo;

}