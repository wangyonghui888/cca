package com.panda.sport.merchant.common.vo.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @auth: YK
 * @Description:财务-清算电子账单表
 * @Date:2020/5/21 14:43
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantFinanceBillMonthVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID  表ID  年-月-merchantCode-currency
     */
    private String id;

    /**
     * finance_month表id
     */
    private String financeId;

    /**
     * 注单币种类型，1:人民币 2:美元 3:欧元 4:新元
     */
    private String currency;

    /**
     * 投注金额
     */
    private BigDecimal billOrderAmount;

    /**
     * 投注笔数
     */
    private Integer billOrderNum;

    /**
     * 盈利金额
     */
    private BigDecimal billProfitAmount;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long modifyTime;

    //----------------------------------------------以下为业务字段---------------------------------------------------

    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 原始汇率字段
     */
    private String currencyCode;

    /**
     * 币种名称
     */
    private String currencyStr;

}
