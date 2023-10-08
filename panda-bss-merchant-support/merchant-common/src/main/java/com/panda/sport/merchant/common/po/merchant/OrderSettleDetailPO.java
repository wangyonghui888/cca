package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class OrderSettleDetailPO implements Serializable {
    private static final long serialVersionUID = 2819257632275334581L;
    /**
     * 自动编号
     */
    private Long id;

    /**
     * 订单单号
     */
    private String orderNo;

    /**
     * 注单单号
     */
    private String betNo;

    /**
     * 订单状态(0:待处理，1:已处理，2:取消交易)
     */
    private Integer orderStatus;

    /**
     * 盘口id
     */
    private Long marketId;

    /**
     * 投注项id
     */
    private Long playOptionsId;

    /**
     * 投注项类型
     */
    private String playOptions;

    /**
     * 玩法id
     */
    private Long playId;

    /**
     * 玩法名称
     */
    private String playName;
    /**
     * 赔率
     */
    private Double oddsValue;

    /**
     * 赛事id
     */
    private Long matchId;

    /**
     * 赛事名称
     */
    private String matchName;


    private String matchInfo;

    /**
     * 体育名
     */
    private String sportName;

    /**
     * sport_id
     */
    private Integer sportId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 盘口比赛结果
     */
    private String marketMatchResult;

    /**
     * tournamentid
     */
    private String tournamentId;

    /**
     * 结算状态
     */
    private String settleResult;

    /**
     * 结算时间
     */
    private Long settleTime;

    /**
     * 插入时间
     */
    private Long insertTime;
    /**
     * 修改时间
     */
    private Long updateTime;

}
