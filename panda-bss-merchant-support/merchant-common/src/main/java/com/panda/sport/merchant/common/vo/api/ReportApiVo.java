package com.panda.sport.merchant.common.vo.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ReportApiVo implements Serializable {
    private static final long serialVersionUID = 5241526151768786394L;
    private String id;
    private String merchantCode;
    private String currency;
    private Integer time;
    private Double profit;
    private Double profitRate;
    private Double returnRate;
    private Double returnAmount;
    private Double validBetAmount;
    private Double failedBetAmount;
    private Double betAmountSettled;
    private Integer totalTickets;
    private Integer ticketSettled;
    private Integer validTickets;
    private Integer failedTickets;
    private Integer validBetUsers;
    private Integer settleUsers;
    private Integer agentLevel;
    private Double betAmount;
    private Double settleProfit;
    private Double settleProfitRate;
    private Double settleReturn;
    private Double settleReturnRate;
    private Double settleBetAmount;
    private Integer settleTickets;
}
