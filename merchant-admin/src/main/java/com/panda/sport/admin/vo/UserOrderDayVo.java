package com.panda.sport.admin.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author YK
 * @Description:
 * @date 2020/3/16 15:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserOrderDayVo {

    /**
     * id
     */
    private String id;

    /**
     * user_id
     */
    private String userId;

    /**
     * user_name
     */
    private String userName;

    /**
     * merchant_code
     */
    private String merchantCode;


    /**
     * 投注笔数
     */
    private Integer betNum;
    private Integer validTickets;

    private BigDecimal returnAmount;

    private BigDecimal validBetAmount;
    /**
     * 已结算投注额
     */
    private BigDecimal betAmountSettled;
    /**
     * 投注金额
     */
    private BigDecimal betAmount = BigDecimal.ZERO;
    private Integer ticketSettled;

    /**
     * 盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal profit = BigDecimal.ZERO;

    /**
     * 百分数：盈亏/投注
     */
    private BigDecimal profitRate = BigDecimal.ZERO;

    /**
     * 已结算注单数
     */
    private Integer settleOrderNum = 0;

    /**
     * 已结算注单总金额
     */
    private BigDecimal settleOrderAmount = BigDecimal.ZERO;

    /**
     * 已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal settleProfit = BigDecimal.ZERO;

    /**
     * 百分数：盈亏/投注
     */
    private BigDecimal settleProfitRate = BigDecimal.ZERO;

    /**
     * 当天比赛投注注单
     */
    private Integer liveOrderNum = 0;

    /**
     * 当天比赛投注注单总金额
     */
    private BigDecimal liveOrderAmount = BigDecimal.ZERO;

    /**
     * 当天投注注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal liveProfit = BigDecimal.ZERO;

    /**
     * 百分数：盈亏/投注
     */
    private BigDecimal liveProfitRate = BigDecimal.ZERO;

    /**
     * 格式20200110
     */
    private Integer time = 0;

    /**
     * merchant_name
     */
    private String merchantName;

    /**
     * 活跃天数
     */
    private Integer activeDays = 1;

    /**
     * 币种
     */
    private Integer currency = 1;

    /**
     * 注单有效投注额
     */
    private BigDecimal orderValidBetMoney;
    /**
     * 结算有效投注额
     */
    private BigDecimal settleValidBetMoney;

}
