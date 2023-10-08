package com.oubao.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : Jeffrey
 * @Date: 2020-01-06 20:50
 * @Description : 商户注单VO对象
 */
@Data
public class MerchantBetOrderVO implements Serializable {


    private String tag;
    private String userName;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 注单数量
     */
    private Integer betCount;
    /**
     * 串关类型
     */
    private Integer seriesType;

    /**
     * 串关类型值
     */
    private String seriesValue;

    /**
     * 订单
     */
    private String orderNo;
    /**
     * 订单创建时间
     */
    private Long createTime;
    /**
     * 投注总额
     */
    private BigDecimal orderAmount;
    /**
     * 订单结果
     */
    private Integer outcome;
    /**
     * 派彩金额
     */
    private BigDecimal settleAmount;

    /**
     * 盈利金额
     */
    private BigDecimal profitAmount;
    /**
     * 结算时间
     */
    private Long settleTime;

    /**
     * 订单明细
     */
    List<MerchantBetOrderDetailVO> detailList;

}


