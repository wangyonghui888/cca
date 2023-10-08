package com.oubao.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单表
 *
 * @author butr 2020-01-06
 */
@Data
public class OrderPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自动编号
     */
    private Long id;

    /**
     * 订单单号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long uid;
    private Long userId;

    /**
     * 订单状态(0:待处理，1:已处理，2:取消交易)
     */
    private Integer orderStatus;

    private Integer outcome;

    /**
     * 注单项数量
     */
    private Long playOptionId;

    /**
     * 串关类型(1：单关(默认) 、2：双式投注，例如1/2 、3：三式投注，例如1/2/3  、4：n串1，例如4串1  、5：n串f，例如5串26 )
     */
    private Integer seriesType;

    /**
     * 串关值(单关(默认) 、双式投注，例如1/2 、三式投注，例如1/2/3  、n串1，例如4串1  、n串f，例如5串26 )
     */
    private String seriesValue;

    /**
     * 注单总价
     */
    private BigDecimal betAmount;


    private BigDecimal amountTotal;

    /**
     * 实际付款金额
     */
    private BigDecimal profitAmount;

    /**
     * 实际付款金额
     */
    private BigDecimal settleAmount;


    /**
     * 创建时间
     */
    private Long createTime;
    private Long settleTime;

    /**
     * 创建用户
     */
    private String createUser;
    private String userName;
    private String merchantCode;

    /**
     * match_id
     */
    private Long matchId;

    /**
     * play_option_type
     */
    private String playOptionType;

    /**
     * match_name
     */
    private String matchName;

    /**
     * sport_id
     */
    private Integer sportId;

    private String tag;


}
