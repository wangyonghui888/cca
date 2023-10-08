package com.oubao.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : Jeffrey
 * @Date: 2020-01-06 20:50
 * @Description : 商户注单VO对象
 */
@Data
public class MerchantBetOrderDetailVO  implements Serializable{
    private Long betNo;
    private Long tournamentId;
    private Long playOptionsId;
    private Long matchId;
    private BigDecimal betAmount;
    private String matchName;
    private String matchInfo;
    private Integer matchType;
    private String marketType;
    private Integer sportId;
    private String sportName;
    private String playOptionName;
    private String playName;
    private String marketValue;
    private BigDecimal handicap;
    private BigDecimal oddsValue;
    private String betResult;


}


