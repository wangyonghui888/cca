package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 活动奖励表
 * </p>
 *
 * @author Baylee
 * @since 2021-07-01
 */
@Data
@ToString
public class AcBonusPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("uid")
    private Long uid;

    /**
     * 活动ID
     */
    @TableField("act_id")
    private Long actId;

    /**
     * 活动名称
     */
    @TableField("act_name")
    private String actName;

    /**
     * 任务ID
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Long updateTime;

    /**
     * 奖励数量
     */
    @TableField("ticket_num")
    private Integer ticketNum;

    /**
     * 0：未领取	1：已领取
     */
    @TableField("bonus_type")
    private Integer bonusType;

    /**
     * 0：每日任务	1：成长任务
     */
    @TableField("task_type")
    private Integer taskType;

    /**
     * 修改时间
     */
    @TableField("remark")
    private String remark;

    /**
     * 修改时间
     */
    @TableField("last_update")
    private Long lastUpdate;


}
