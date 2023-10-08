package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 活动管理配置表
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_activity_config")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("name")
    private String name;

    /**
     * 活动类型(1常规活动 2自定义活动 3特殊活动)
     */
    @TableField("type")
    private Integer type;

    /**
     * 活动终端
     */
    @TableField("terminal")
    private String terminal;

    @TableField("start_time")
    private Long startTime;

    @TableField("end_time")
    private Long endTime;

    /**
     * 领取时效(单位Hour)
     */
    @TableField("time_limit")
    private Integer timeLimit;

    /**
     * 参与活动赛种
     */
    @TableField("sport_id")
    private String sportId;

    /**
     * 奖励类型:  1 直接上分额度,2筹码彩金,3机会(如抽奖次数)
     */
    @TableField("reward_type")
    private Integer rewardType;

    /**
     * 活动总投入:根据活动的预算,来限制活动礼金发放.
     */
    @TableField("total_cost")
    private Long totalCost;

    /**
     * 中奖比例:可以按一定的算法把总预算礼金分到固定数量的玩家上.(如1000个玩家参与活动,要保证不低于100个玩家获奖)
     */
    @TableField("reward_percentage")
    private Double rewardPercentage;

    /**
     * 单日最高中奖额度:把总预算分配到活动的每天里面,对每天的中奖额度进行限制.
     */
    @TableField("single_day_max")
    private Long singleDayMax;

    /**
     * 单用户最高中奖额度.
     */
    @TableField("single_user_max")
    private Long singleUserMax;

    /**
     * 参与次数:单个用户参与活动的次数.
     */
    @TableField("user_partition_times")
    private Integer userPartitionTimes;

    /**
     * 是否自动审批:会员参与活动中奖时,是否把对应奖励额度自动发放,或者需要运营人员审核之后才能发放.
     */
    @TableField("auto_check")
    private Integer autoCheck;

    /**
     * 参与活动规则.(会员需要满足参与活动的条件)
     */
    @TableField("partition_rule")
    private Long partitionRule;

    /**
     * 领奖规则
     */
    @TableField("reward_rule")
    private Long rewardRule;

    /**
     * 结算周期(1h,单位小时)
     */
    @TableField("settle_cycle")
    private Integer settleCycle;

    /**
     * 状态(0,关闭;1,开启)
     */
    @TableField("status")
    private String status;

    /**
     * 玩家参与开始时间
     */
    @TableField("in_start_time")
    private Long inStartTime;

    /**
     * 玩家参与结束时间
     */
    @TableField("in_end_time")
    private Long inEndTime;

    /**
     * pc活动图片地址
     */
    @TableField("pc_url")
    private String pcUrl;

    /**
     * h5活动图片地址
     */
    @TableField("h5_url")
    private String h5Url;

    /**
     * h5活动维护图片地址
     */
    @TableField("h5_maintain_url")
    private String h5MaintainUrl;

    /**
     * pc活动维护图片地址
     */
    @TableField("pc_maintain_url")
    private String pcMaintainUrl;

    /**
     * 0关闭维护,开启活动;1开启维护
     */
    @TableField("maintain_status")
    private Integer maintainStatus;

    /**
     * 维护结束时间
     */
    @TableField("maintain_end_time")
    private Long maintainEndTime;

    /**
     * 维护结束时间
     */
    @TableField("title")
    private String title;

    /**
     * 维护结束时间
     */
    @TableField("content")
    private String content;


}
