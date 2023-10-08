package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
@ToString
public class ActivityConfigPo {
    /**
     * id
     */
    private Long id;

    /**
     * name
     */
    private String name;

    /**
     * 活动类型(1常规活动 2自定义活动 3特殊活动)
     */
    private Integer type;

    /**
     * 活动终端
     */
    private String terminal;

    /**
     * start_time
     */
    private Long startTime;

    /**
     * end_time
     */
    private Long endTime;

    /**
     * 领取时效(单位hour)
     */
    private Integer timeLimit;

    /**
     * 是否有ip限制
     */
    private int ipLimit;

    /**
     * 参与活动赛种
     */
    private String sportId;

    /**
     * 奖励类型: 1 直接上分额度，2筹码彩金，3机会(如抽奖次数)
     */
    private int rewardType;

    /**
     * 活动总投入:根据活动的预算，来限制活动礼金发放.
     */
    private Long totalCost;

    /**
     * 中奖比例:可以按一定的算法把总预算礼金分到固定数量的玩家上.(如1000个玩家参与活动，要保证不低于100个玩家获奖)
     */
    private Double rewardPercentage;

    /**
     * 指定中奖:可以指定某些vip玩家中奖.
     */
    private String rewardGuy;

    /**
     * 单日最高中奖额度:把总预算分配到活动的每天里面，对每天的中奖额度进行限制.
     */
    private Long singleDayMax;

    /**
     * 单用户最高中奖额度.
     */
    private Long singleUserMax;

    /**
     * 参与次数:单个用户参与活动的次数.
     */
    private Integer userPartitionTimes;

    /**
     * 是否自动审批:会员参与活动中奖时，是否把对应奖励额度自动发放，或者需要运营人员审核之后才能发放.
     */
    private int autoCheck;

    /**
     * 参与活动规则.(会员需要满足参与活动的条件)
     */
    private Long partitionRule;

    /**
     * 领奖规则
     */
    private Long rewardRule;

    /**
     * 结算周期(1h，单位小时)
     */
    private int settleCycle;

    /**
     * 状态(0，关闭;1，开启)
     */
    private Integer status;

    /**
     * 玩家参与开始时间
     */
    private Long inStartTime;

    /**
     * 玩家参与结束时间
     */
    private Long inEndTime;

    /**
     * pc活动图片地址
     */
    private String pcUrl;

    /**
     * h5活动图片地址
     */
    private String h5Url;
}
