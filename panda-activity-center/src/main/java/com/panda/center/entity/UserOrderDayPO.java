package com.panda.center.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOrderDayPO extends OrderPO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**rder/user/queryUserBetListByTime
     * 格式20200110
     */
    private Integer time;
    /**
     * 活跃天数
     */
    private Integer activeDays = 1;

    private BigDecimal orderValidBetMoney;
    /**
     * 结算有效投注额
     */
    private BigDecimal settleValidBetMoney;

}