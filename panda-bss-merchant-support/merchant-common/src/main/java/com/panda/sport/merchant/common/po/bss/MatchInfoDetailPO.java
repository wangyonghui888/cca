package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:08
 * @Version: 1.0
 */
@Data
@ToString
public class MatchInfoDetailPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 赛程信息id.  对应s_match_info.id
     */
    private Long matchInfoId;

    /**
     * 90分钟-上半场比分
     */
    private String firstHalfScore;

    /**
     * 90分钟-下半场比分
     */
    private String secondHalfScore;

    /**
     * 90分钟-最新比分/全场比分
     */
    private String score;

    /**
     * 进球球员
     */
    private String goalPlayer;

    /**
     * 进球时间-多少分钟进的球
     */
    private Integer goalTime;

    /**
     * 赛事状态
     */
    private Integer matchStatus;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 进球队伍（T1主队,T2客队）
     */
    private String goalTeam;

    /**
     * 运动类型（1足球、2篮球）
     */
    private Integer sportId;

    /**
     * 赛事阶段
     */
    private String matchPeriod;

    /**
     * 加时赛-上半场比分
     */
    private String firstHalfScoreOvertime;

    /**
     * 加时赛-下半场比分
     */
    private String secondHalfScoreOvertime;

    /**
     * 加时赛-最新比分/全场比分
     */
    private String scoreOvertime;

    /**
     * 点球比分-最新比分
     */
    private String scorePenalty;

    /**
     * 角球-上半场比分
     */
    private String firstHalfScoreCorner;

    /**
     * 角球-下半场比分
     */
    private String secondHalfScoreCorner;

    /**
     * 角球-总比分(最新比分)
     */
    private String scoreCorner;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 罚牌数-最新比分
     */
    private String scoreBooking;

    /**
     * 罚牌数-上半场
     */
    private String firstHalfScoreBooking;

    /**
     * 罚牌数-下半场
     */
    private String secondHalfScoreBooking;

}
