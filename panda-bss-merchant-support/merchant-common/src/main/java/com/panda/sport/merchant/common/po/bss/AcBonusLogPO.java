package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 优惠券领取日志表
 * </p>
 *
 * @author baylee
 * @since 2021-08-26
 */
@Data
@ToString
@TableName("t_ac_bonus_log")
public class AcBonusLogPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户code
     */
    @TableField("merchant_code")
    private String merchantCode;

    /**
     * 用户uid
     */
    @TableField("uid")
    private String uid;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 任务iD
     */
    @TableField("task_id")
    private Long taskId;

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
     * 活动的任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 劵的数量
     */
    @TableField("ticket_num")
    private Integer ticketNum;

    /**
     * t_ac_bonus的主键ID
     */
    @TableField("bonus_id")
    private Long bonusId;

    /**
     * 领取时间
     */
    @TableField("receive_time")
    private Long receiveTime;

    /**
     * 领取的时间，天
     */
    @TableField("bonus_time")
    private String bonusTime;
    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建源
     */
    @TableField("created_from")
    private int createdFrom;

    @TableField(exist = false)
    private int ticketNow;
}
