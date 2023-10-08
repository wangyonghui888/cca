package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class MerchantOrderDayPO implements Serializable {
    /**
     * 唯一ID,merchantCode+time
     */
    private String id;
    /**
     * merchant_code
     */
    private String merchantCode;
    /**
     * 格式:20210110
     */
    private Integer time;
    /**
     * 投注用户数/注册用户数r
     */
    private BigDecimal betUserRate;
    /**
     * 投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等
     */
    private BigDecimal profit;
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

    private BigDecimal profitRate;
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

    private Integer agentLevel;
    /**
     * rnrn投注额
     */
    private BigDecimal betAmount;
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
     * 当天结算注单-当天结算注单派彩金额
     */
    private BigDecimal settleProfit;
    private BigDecimal settleProfitRate;
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
    /**
     * 结算注单数
     */
    private Integer settleOrderNum;
    /**
     * 当天比赛注单用户数/注册用户数
     */
    private BigDecimal liveUserRate;
    /**
     * 当天比赛注单-当天比赛注单派彩金额
     */
    private BigDecimal liveProfit;
    private BigDecimal liveProfitRate;
    /**
     * 当天比赛注单派彩金额
     */
    private BigDecimal liveReturn;
    /**
     * 返奖率，比赛注单派彩金额/比赛注单总投注量，大于等于100%的要标红
     */
    private BigDecimal liveReturnRate;
    /**
     * 当日比赛注单总额
     */
    private BigDecimal liveBetAmount;
    /**
     * 比赛当日注单数
     */
    private Integer liveOrderNum;
    /**
     * 商户名称
     */
    private String merchantName;
    private Integer addUser;
    private Integer settleUsers;
    private Integer betSettledUsers;
    private Integer liveUsers;
    private String currency = "1";


    /**
     * 注单有效投注额
     */
    private BigDecimal orderValidBetMoney;
    /**
     * 结算有效投注额
     */
    private BigDecimal settleValidBetMoney;
}
