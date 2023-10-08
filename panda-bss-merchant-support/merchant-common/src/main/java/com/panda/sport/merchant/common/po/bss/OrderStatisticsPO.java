package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderStatisticsPO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer sumBetNo;

    private Integer userAmount;

    private String currencyCode;
    
    private BigDecimal betAmount;

    private BigDecimal sumProfitAmount;

    private BigDecimal sumValidBetMoney;

    private Integer sumValidBetNo;
}
