package com.panda.sport.merchant.common.vo.user;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserOrderAllVO implements Serializable {

    private String userId;

    /**
     * user_name
     */
    private String userName;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 日均投注笔数
     */
    private Integer betAvgDay;


    /**
     * 最后投注时间
     */
    private String lastBet;


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



    private Long registerTime;

    private String lastUpdate;


    /**
     * merchant_code
     */
    private String merchantCode;
    private String currencyCode;

    private Integer orderAmount;


    private BigDecimal validBetAmount;



    /**
     * 百分数：盈亏/投注
     */
    private BigDecimal profitRate;


    /**
     * 最后登录时间
     */
    private String lastLogin;
    /**
     * 最后登录时间
     */
    private String lastLoginStr;

    /**
     * 最后投注时间
     */
    private String lastBetStr;



    /**
     * 返还金额
     */
    private Integer settleAmount;

    /**
     * 余额
     */
    private Long balance;

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
    private Long refuseOrderAmount;

    /**
     * 取消订单笔数
     */
    private Integer cancelOrderNum;

    /**
     * 取消订单金额
     */
    private Long cancelOrderAmount;

    /**
     * 大于欧赔为2的订单数(包括等于)
     */
    private Integer greterThan2Num;

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
}
