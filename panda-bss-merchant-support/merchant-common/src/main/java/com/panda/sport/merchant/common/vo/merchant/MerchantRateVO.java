package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;

import java.io.Serializable;


/**
 * @Author: Jeffrey
 * @Date: 2020/1/23
 */
@Data
public class MerchantRateVO extends BaseVO{
    /**
     * 费率ID
     */
    private Long id;
    /**
     * 计算模式
     */
    private String computingStandard;
    /**
     * 金额范围开始
     */
    private Long rangeAmountBegin;
    /**
     * 金额范围结束
     */
    private Long rangeAmountEnd;
    /**
     * 平台费率
     */
    private Double terraceRate;
    /**
     *缴纳周期
     */
    private Integer paymentCycle;
/*    *//**
     * VIP费用
     *//*
    private Long vipAmount;
    *//**
     * VIP费用缴纳周期
     *//*
    private Integer vipPaymentCycle;
    *//**
     * 技术费用
     *//*
    private Long techniqueAmount;
    *//**
     * 技术费用缴纳周期
     *//*
    private Integer techniquePaymentCycle;*/
    private String remarks;
    private static final long serialVersionUID = 1L;

}
