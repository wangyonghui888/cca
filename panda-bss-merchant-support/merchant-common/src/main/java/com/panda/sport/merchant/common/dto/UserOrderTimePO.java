package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserOrderTimePO implements Serializable {

    private static final long serialVersionUID = -1L;

    private String uid;
    private String merchantCode;
    private Long lastBetTime;
    private BigDecimal validBetAmount;
    private BigDecimal profitAmount;
    private BigDecimal settleProfit;
    private Integer validTickets;
    private BigDecimal settledBetAmount;
    private Long updatedTime;
}
