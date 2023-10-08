package com.panda.sport.merchant.common.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserBetMonthVO implements Serializable {
    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal profit;

    /**
     * 已结算注单总金额
     */
    private BigDecimal settleOrderAmount;

    /**
     * 已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal settleProfit;

    /**
     * 当月比赛投注注单总金额
     */
    private BigDecimal liveOrderAmount;

    /**
     * 当月投注注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal liveProfit;

    private Integer time;
}
