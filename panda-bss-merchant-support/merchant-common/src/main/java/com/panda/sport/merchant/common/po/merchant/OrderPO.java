package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class OrderPO implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * merchant_name
     */
    private String merchantName;


    /**
     * 投注笔数
     */
    private Integer betNum;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal profit;

    /**
     * 百分数：盈亏/投注
     */
    private Integer profitRate;

    /**
     * 已结算注单数
     */
    private Integer settleOrderNum;

    /**
     * 已结算注单总金额
     */
    private BigDecimal settleOrderAmount;

    /**
     * 已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal settleProfit;

    /**
     * 百分数：盈亏/投注
     */
    private BigDecimal settleProfitRate;

    /**
     * 当天比赛投注注单
     */
    private Integer liveOrderNum;

    /**
     * 当天比赛投注注单总金额
     */
    private BigDecimal liveOrderAmount;

    /**
     * 当天投注注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal liveProfit;

    /**
     * 百分数：盈亏/投注
     */
    private Integer liveProfitRate;

    /**
     * 币种
     */
    private Integer currency = 1;


    /**
     * 投注用户数/注册用户数r
     */
    private BigDecimal betUserRate;


    /**
     * 注册用户数
     */
    private Integer registerTotalUserSum = 0;

    /**
     * 所有用户数
     */
    private Integer userAllCount;
    /**
     * 已结算注单数
     */
    private Integer ticketSettled;


    /**
     * 派彩金额rnr
     */
    private BigDecimal returnAmount;

    /**
     * rnrn返奖率，派彩金额/总投注量，大于等于100%的要标红
     */
    private BigDecimal returnRate;
    private BigDecimal validBetAmount;
    private BigDecimal failedBetAmount;
    /**
     * 已结算投注额
     */
    private BigDecimal betAmountSettled;

    private Integer totalTickets;
    private Integer validTickets;
    private Integer failedTickets;
    private Integer validBetUsers;

    /**
     * 商户等级rnr
     */
    private String merchantLevel;
    /**
     * 商户等级rnr
     */
    private Integer agentLevel;

    /**
     * 订单数
     */
    private Integer orderSum;

    /**
     * 首投用户数
     */
    private Integer firstBetUser;

    /**
     * 投注用户数rnr
     */
    private Integer betUserSum;

    /**
     * 当天结算注单用户数/注册用户数
     */
    private BigDecimal settileUserRate;


    /**
     * 当天结算注单派彩金额
     */
    private BigDecimal settleReturn;

    /**
     * 返奖率，结算注单派彩金额/结算注单总投注量，大于等于100%的要标红
     */
    private BigDecimal settleReturnRate;

    /**
     * 结算注单总额
     */
    private BigDecimal settleBetAmount;


    private Integer addUser;
    private Integer settleUsers;


}