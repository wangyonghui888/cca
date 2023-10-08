package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class StatisticsVO {

    /**
     * 注单统计
     */
    private Integer totalOrder;
    /**
     * 预投注金额
     */
    private BigDecimal orderAmountTotal;
    /**
     * 预投注成功金额
     */
    private BigDecimal preBetOrderAmountTotal;
}
