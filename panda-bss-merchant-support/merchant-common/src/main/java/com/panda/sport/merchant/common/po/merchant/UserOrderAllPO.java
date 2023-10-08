package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class UserOrderAllPO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * user_id
     */
    private String userId;

    private String uid;

    private BigDecimal amount;

    private Long registerTime;
    private Long createTime;

    private String lastUpdate;
    /**
     * user_name
     */
    private String userName;
    private String fakeName;

    /**
     * merchant_code
     */
    private String merchantCode;

    private String currencyCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 投注笔数
     */
    private Integer betNum;

    private Integer orderAmount;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    private BigDecimal validBetAmount;

    /**
     * 盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal profit;

    /**
     * 百分数：盈亏/投注
     */
    private BigDecimal profitRate;


    /**
     * 最后登录时间
     */
    private Long lastLogin;

    /**
     * 最后登录时间
     */
    private Integer loginTimes;

    /**
     * 最后登录时间
     */
    private String lastLoginStr;

    /**
     * 最后投注时间
     */
    private Long lastBet;

    private String lastBetStr;

    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 注单限额20%以内注单数量
     */
    private Integer orderLimit20per;
    /**
     * 注单限额20%-50%注单数量
     */
    private Integer orderLimit20to50;
    /**
     * 注单限额50%-80%注单数量
     */
    private Integer orderLimit50to80;
    /**
     * 注单限额80%以上注单数量
     */
    private Integer orderLimit80per;

    /**
     * 日均投注笔数
     */
    private Integer betAvgDay;

    /**
     * 返还金额
     */
    private BigDecimal settleAmount;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 用户标签
     */
    private Integer levelId;

    /**
     * 拒绝订单笔数
     */
    private Integer refuseOrderNum;

    /**
     * 拒绝订单金额
     */
    private BigDecimal refuseOrderAmount;

    /**
     * 取消订单笔数
     */
    private Integer cancelOrderNum;

    /**
     * 取消订单金额
     */
    private BigDecimal cancelOrderAmount;

    /**
     * 大于欧赔为2的订单数(包括等于)
     */
    private Integer greterThan2Num;
    private Integer allNum;

    /**
     * 小于欧赔为2的订单数
     */
    private Integer lessThan2Num;

    /**
     * 足球订单数
     */
    private Integer soccerNum;

    /**
     * 篮球订单数
     */
    private Integer basketballNum;

    /**
     * 其他体种订单数
     */
    private Integer othersNum;

    /**
     * 串关订单数
     */
    private Integer seriesNum;

    /**
     * 篮球让分订单数
     */
    private Integer basketballHandicapNum;

    /**
     * 篮球大小订单数
     */
    private Integer basketballOverunderNum;

    /**
     * 盈利订单笔数
     */
    private Integer profitOrderNum;

    /**
     * 盈利订单笔数/有效订单笔数
     */
    private Integer profitOrderRate;

    /**
     * 有效订单数
     */
    private Integer validOrderNum;

    /**
     * 用户限额类型
     */
    private Integer specialBettingLimitType;
    /**
     * 是否vip用户
     */
    private Integer isvip;

    /**
     * 用户限额时间
     */
    private Long specialBettingLimitTime;
    private Integer specialBettingLimitDelayTime;
    private Integer marketLevel;
    private String agentId;
    private String merchantId;

    private String vipUpdateTime;

     /**
      * 用户状态 0启用 1禁用
     */
    private Integer disabled;

    private Integer allowListSource;

    public void setLastBet(Long lastBet) {
        this.lastBet = lastBet;
    }

    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
    }
}
