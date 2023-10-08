package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 *
 * @author :  kiu
 * @Description : T_MATCH_STATISTICS_INFO_MESSAGE 赛事统计信息
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.schedule.po
 * @Date : 2019-10-15 04:02:50
*/
@Data
public class MatchStatisticsInfoMessagePO extends BaseVO {
    /**
    * id
    */
    private Long id;

    /**
    * 标准运动种类id. 对应 standard_sport_type.id
    */
    private Long sportId;

    /**
    * 标准赛事id
    */
    private Long standardMatchId;

    /**
    * 发球得分
    */
    private String acesScore;

    /**
    * 角球比分
    */
    private String cornerScore;

    /**
    * 危险进攻次数比分
    */
    private String dangerousAttackScore;

    /**
    * 两次发球失误比分
    */
    private String doubleFaultScore;

    /**
    * 统计时间点. UTC标准时间
    */
    private Long eventTime;

    /**
    * 加时赛比分
    */
    private String extraTimeScore;

    /**
    * 任意球比分
    */
    private String freeKickScore;

    /**
    * 一局比分(网球)
    */
    private String gameScore;

    /**
    * 主客队信息. home: 主场队;away:客场队
    */
    private String homeAway;

    /**
    * Game short info
    */
    private String info;

    /**
    * 预计比赛时长.  单位:秒
    */
    private Integer matchLength;

    /**
    * 更新时间. UTC时间,精确到毫秒
    */
    private Long modifyTime;

    /**
    * 点球比分
    */
    private String penaltyScore;

    /**
    * 比赛阶段
    */
    private Integer period;

    /**
    * 比赛阶段个数
    */
    private Integer periodLength;

    /**
    * 阶段比分
    */
    private String periodScore;

    /**
    * 四分之一节比分
    */
    private String quarterScore;

    /**
    * 红牌比分
    */
    private String redCardScore;

    /**
    * 比赛剩余时间. 单位:秒
    */
    private Integer remainingTime;

    /**
    * 备注
    */
    private String remark;

    /**
    * 当前比分信息
    */
    private String score;

    /**
    * 当前比赛进行时间.单位:秒
    */
    private Integer secondsMatchStart;

    /**
    * 发球人
    */
    private Integer server;

    /**
    * 汇总比分
    */
    private String setScore;

    /**
    * 汇总比分1
    */
    private String set1Score;

    /**
    * 汇总比分2
    */
    private String set2Score;

    /**
    * 汇总比分3
    */
    private String set3Score;

    /**
    * 汇总比分4
    */
    private String set4Score;

    /**
    * 汇总比分5
    */
    private String set5Score;

    /**
    * 汇总比分6
    */
    private String set6Score;

    /**
    * 汇总比分7
    */
    private String set7Score;

    /**
    * 汇总比分8
    */
    private String set8Score;

    /**
    * 汇总比分9
    */
    private String set9Score;

    /**
    * 汇总比分10
    */
    private String set10Score;

    /**
    * 黄牌比分
    */
    private String yellowCardScore;

    /**
    * set1的角球比分
    */
    private String set1CornerScore;

    /**
    * set1的红牌比分
    */
    private String set1RedCardScore;

    /**
    * set1的黄牌比分
    */
    private String set1YellowCardScore;

    /**
    * set2的角球比分
    */
    private String set2CornerScore;

    /**
    * set2的红牌比分
    */
    private String set2RedCardScore;

    /**
    * set2的黄牌比分
    */
    private String set2YellowCardScore;

    /**
    * Total set count
    */
    private Integer setCount;

    /**
    * 射偏比分
    */
    private String shotOffTargetScore;

    /**
    * 射正比分
    */
    private String shotOnTargetScore;

    /**
    * 第三方事件类型
    */
    private String thirdEventType;

    /**
    * 第三方事件类型id
    */
    private Long thirdEventTypeId;

    /**
    * 第三方赛事id
    */
    private Long thirdMatchId;

    /**
    * 第三方原始事件id
    */
    private String thirdSourceEventId;

    /**
    * 第三方赛事原始id
    */
    private String thirdSourceMatchId;

}