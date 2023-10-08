package com.panda.sport.merchant.common.po.merchant.mq;

import lombok.Data;

import java.util.List;

/**
 * @auth: YK
 * @Description:盘口的投注
 * @Date:2020/6/28 15:15
 */
@Data
public class MarketResultMessagePO {


    /**
     * 赛事Id
     */
    private Long matchId;

    /**
     * 盘口ID
     */
    private Long marketId;

    /**
     * 结算时的最新比分
     */
    private String score;

    /**
     * 比赛阶段
     */
    private Long periodId;

    /**
     * 结算次数
     */
    private Integer settlementTimes;


    private Integer totalSettlementTimes;
    /**
     * 盘口下投注项结果集
     */
    private List<MarketOptionsResultPO> marketOptionsResults;

    /**
     * 体种id
     */
    private Integer sportId;

}
