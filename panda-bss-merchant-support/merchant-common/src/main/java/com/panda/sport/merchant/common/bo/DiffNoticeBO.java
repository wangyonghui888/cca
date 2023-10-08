package com.panda.sport.merchant.common.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author :  ifan
 * @Description :  对账单异常处理发送mango的BO
 * @Date: 2022-06-28
 */
@Data
public class DiffNoticeBO implements Serializable {
    /**
     * 异常商户名称
     */
    private String merchantName;

    /**
     * 异常日期
     */
    private String financeDate;

    /**
     * 正常注单数
     */
    private Integer correctOrderSum;

    /**
     * 正常投注用户数
     */
    private Integer correctBetUserSum;

    /**
     * 正常投注金额
     */
    private BigDecimal correctBetAmountSum;

    /**
     *  正确盈利金额
     */
    private BigDecimal correctProfitAmountSum;
}
