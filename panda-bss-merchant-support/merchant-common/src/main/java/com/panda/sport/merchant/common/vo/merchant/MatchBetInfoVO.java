package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MatchBetInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer sportId;
    private String sportName;

    private Long beginTime;
    private String beginTimeStr;
    private Integer matchStatus;


    private Long tournamentId;
    private String id;
    private String matchId;
    private Long playId;
    private Long marketId;
    private String matchInfo;
    private String playName;
    private String marketValue;
    private String marketType;
    private BigDecimal betAmount;
    private BigDecimal orderAmountTotal;
    private BigDecimal validBetAmount;
    private BigDecimal settleAmount;
    private BigDecimal profit;
    private BigDecimal profitRate;
    private Integer userAmount;
    private Integer orderAmount;
    private Integer playAmount;
    private String tournamentName;
    private String merchantCode;
    private String merchantName;
    private Integer tournamentLevel;
    private Integer unSettleOrder;
    private BigDecimal unSettleAmount;
    private BigDecimal parlayVaildBetAmount;
    private Integer parlayValidTickets;
    private BigDecimal parlayProfit;
    private BigDecimal parlayProfitRate;
}