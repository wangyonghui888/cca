package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 活动任务表
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_ac_task")
public class AcTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    @TableField("act_id")
    private Integer actId;

    /**
     * 活动名称
     */
    @TableField("act_name")
    private String actName;

    /**
     * 任务名字
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 条件ID
每日任务：
1：每日投注x笔
2：当日单笔有效投注 >= x 元
3：当日投注注单数 >= x 笔
4：当日完成 x 笔串关玩法
5：当日完成 x 场VR体育赛事
成长任务：
1：本月累计投注天数 x 天
2：本周累计有效投注 >= x 元
3：本月累计有效投注 >= x 元
     */
    @TableField("condition_id")
    private Integer conditionId;

    /**
     * 任务条件1
     */
    @TableField("task_condition")
    private String taskCondition;

    /**
     * 任务条件2
     */
    @TableField("task_condition2")
    private String taskCondition2;

    /**
     * 任务条件3
     */
    @TableField("task_condition3")
    private String taskCondition3;

    /**
     * 去完成路径
     */
    @TableField("forward_url")
    private String forwardUrl;

    /**
     * 0：每日任务
1：成长任务
2：抽奖任务
     */
    @TableField("type")
    private Integer type;

    /**
     * 奖券数量
     */
    @TableField("ticket_num")
    private Integer ticketNum;

    /**
     * 0：隐藏 1：显示
     */
    @TableField("status")
    private Integer status;

    /**
     * 0：失效 1：有效
     */
    @TableField("invalidation")
    private Integer invalidation;

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
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 排序值
     */
    @TableField("order_no")
    private Integer orderNo;


}
