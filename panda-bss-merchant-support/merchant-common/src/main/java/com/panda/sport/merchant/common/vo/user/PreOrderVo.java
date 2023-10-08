package com.panda.sport.merchant.common.vo.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/*
  提前结算子单
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PreOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @FieldExplain("注单号")
    private String orderNo;

    private String preOrderNo;
    //订单状态(0:未结算,1:已结算,2:注单取消,3:确认中,4:投注失败)
    private Integer orderStatus;
    /**
     * 0 部分提前结算 1 全额提前结算 2 回滚
     */
    private Integer settleType;

    private BigDecimal preBetAmount;

    @FieldExplain("投注总金额")
    private BigDecimal remainingBetAmount;

    private BigDecimal settleAmount;

    @FieldExplain("净盈利")
    private BigDecimal profit;

    private Long createTime;
    /**
     * 1 部分提前结算 2 全额提前结算 3 赛果结算
     */
    private Integer type = 1;
}
