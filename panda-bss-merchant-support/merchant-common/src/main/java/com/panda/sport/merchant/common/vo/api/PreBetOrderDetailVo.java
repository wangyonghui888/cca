package com.panda.sport.merchant.common.vo.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PreBetOrderDetailVo {

    private static final long serialVersionUID = 5241526152768786398L;

    /**
     * 投注编号
     */
    private Long betNo;

    /**
     * 联赛ID
     */
    private Long tournamentId;

    /**
     * 玩法项ID
     */
    private Long playOptionsId;

    /**
     * 赛事ID
     */
    private Long matchId;

    /**
     * 赛事开始时间
     */
    private Long beginTime;

    /**
     * 赛事开始时间
     */
    private String beginTimeStr;

    /**
     * 联赛名称
     */
    private String tournamentName;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 赛事名称
     */
    private String matchName;

    /**
     * 对阵信息
     */
    private String matchInfo;

    /**
     * 赛事类型
     */
    private Integer matchType;

    /**
     * 盘口类型
     */
    private String marketType;

    /**
     * 运动种类编号
     */
    private Integer sportId;

    /**
     * 运动种类名称
     */
    private String sportName;

    /**
     * 投注项名称
     */
    private String playOptionName;

    /**
     * 玩法名称
     */
    private String playName;

    /**
     * 盘口值
     */
    private String marketValue;

    /**
     *  组装盘口值
     */
    private BigDecimal handicap;

    /**
     * 注单赔率
     */
    private BigDecimal oddsValue;

    /**
     * 最终赔率
     */
    private BigDecimal oddFinally;

    /**
     * 投注结算结果
     */
    private String betResult;

    /**
     * 基准比分
     */
    private String scoreBenchmark;

    /**
     * 投注项结算比分
     */
    private String settleScore;

    /**
     * 投注类型
     */
    private String playOptions;

    /**
     * 玩法ID
     */
    private Integer playId;

    /**
     * 取消类型 0未取消，1比赛取消，2比赛延期， 3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，
     * 10联赛错误，11比分错误，12电视裁判， 13主客场错误，14赛制错误，15赛程错误，16事件错误，17赛事提前，
     * 18自定义原因，19数据源取消，20比赛延迟，40PA手动拒单，41PA自动拒单，42业务拒单，43MTS拒单，46赔率调整中
     */
    private Integer cancelType;
}
