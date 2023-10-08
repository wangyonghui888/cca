package com.panda.sport.order.vo;

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
    /**
     * 用户id
     */
    private Long  userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 注单总金额
     */
    private BigDecimal amountTotal;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 注单类型
     */
    private Integer seriesType;
    /**
     * 注单类型名称
     */
    private String seriesValue;

    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 商户
     */
    private String merchantCode;


    /**
     * 商户
     */

    private BigDecimal settleAmount;

    /**
     * 商户
     */

    private BigDecimal profitAmount;


    /**
     * 商户
     */

    private Long settleTime;

    /**
     * 商户
     */

    private String remark;
    /**
     * 商户
     */

    private Integer outcome;

/**
     * 订单明细
     *//*

    List<MerchantBetOrderDetailVO> orderDetailList;
*/

}


