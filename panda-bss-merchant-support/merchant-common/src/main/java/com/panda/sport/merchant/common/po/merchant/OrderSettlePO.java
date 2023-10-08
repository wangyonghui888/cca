package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderSettlePO implements Serializable {
    private static final long serialVersionUID = 2819257632275334581L;

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
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 订单状态(0:待处理，1:已处理，2:取消交易)
     */
    private Integer orderStatus;


    /**
     * 串关类型(1：单关(默认) 、2：双式投注，例如1/2 、3：三式投注，例如1/2/3  、4：n串1，例如4串1  、5：n串f，例如5串26 )
     */
    private Integer seriesType;
    private Integer outcome;

    /**
     * 串关值(单关(默认) 、双式投注，例如1/2 、三式投注，例如1/2/3  、n串1，例如4串1  、n串f，例如5串26 )
     */
    private String seriesValue;


    /**
     * 实际付款金额
     */
    private BigDecimal amountTotal;

    /**
     * 结算金额
     */
    private BigDecimal settleAmount;

    /**
     * 返回金额
     */
    private BigDecimal profitAmount;


    /**
     * 结算时间
     */
    private Long settleTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 插入时间
     */
    private Long insertTime;


    private String merchantCode;
    private String tag;

    private Long uid;

    private Integer billStatus;
    private Integer productCount;
    private Double productAmountTotal;
    private Double orderAmountTotal;
    private String remark;
    private Long merchantId;
    private String currencyCode;
    private String deviceImei;
    private Long maxWinAmount;
    private Long confirmTime;

    private String createTimeStr;
    private String orderStatusStr;
    private String billStatusStr;
    private String seriesTypeStr;
}
