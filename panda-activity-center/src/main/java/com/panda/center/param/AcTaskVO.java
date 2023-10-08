package com.panda.center.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/25 11:57:27
 */
@Getter
@Setter
@ToString
@Slf4j
public class AcTaskVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 5858357210923120818L;

    private static AcTaskVO acTaskVO = new AcTaskVO();

    private AcTaskVO() {
        super();
    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 活动ID
     */
    private Integer actId;

    /**
     * 活动名称
     */
    private String actName;

    /**
     * 任务名字
     */
    private String taskName;

    /**
     * 条件ID
     * 每日任务：
     * 1：每日投注x笔
     * 2：当日单笔有效投注 >= x 元
     * 3：当日投注注单数 >= x 笔
     * 4：当日完成 x 笔串关玩法
     * 5：当日完成 x 场VR体育赛事
     * 成长任务：
     * 1：本月累计投注天数 x 天
     * 2：本周累计有效投注 >= x 元
     * 3：本月累计有效投注 >= x 元
     */
    private Integer conditionId;

    /**
     * 任务条件1
     */
    private String taskCondition;

    /**
     * 任务条件2
     */
    private String taskCondition2;

    /**
     * 任务条件3
     */
    private String taskCondition3;

    /**
     * 去完成路径
     */
    private String forwardUrl;

    /**
     * 0：每日任务	1：成长任务	2：抽奖任务
     */
    private Integer type;

    /**
     * 奖券数量
     */
    private Integer ticketNum;

    /**
     * 0：隐藏 1：显示 2：删除
     */
    private Integer status;

    /**
     * 0：失效 1：有效
     */
    private Integer invalidation;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序值
     */
    private Integer orderNo;

    public static AcTaskVO getInstance() {
        try {
            return (AcTaskVO) acTaskVO.clone();
        } catch (CloneNotSupportedException e) {
            log.error("SportAndPlayTreeVO clone error", e);
        }
        return new AcTaskVO();
    }

}
