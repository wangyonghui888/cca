package com.panda.center.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/26 17:09:06
 */
@Getter
@Setter
@ToString
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcBonusLogVO implements Serializable ,Cloneable{

    private static final long serialVersionUID = 5012857075358803242L;

    private static AcBonusLogVO acBonusLogVO = new AcBonusLogVO();

    private AcBonusLogVO() {
        super();
    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 任务iD
     */
    private Long taskId;

    /**
     * 活动ID
     */
    private Long actId;

    /**
     * 活动名称
     */
    private String actName;

    /**
     * 活动的任务名称
     */
    private String taskName;

    /**
     * 已领取
     */
    private Integer status = 1;

    /**
     * 劵的数量
     */
    private Integer ticketNum;

    /**
     * t_ac_bonus的主键ID
     */
    private Long bonusId;

    /**
     * 领取时间
     */
    private Long receiveTime;

    /**
     * 领取的时间，天
     */
    private String bonusTime;

    public static AcBonusLogVO getInstance() {
        try {
            return (AcBonusLogVO) acBonusLogVO.clone();
        } catch (CloneNotSupportedException e) {
            log.error("SportAndPlayTreeVO clone error", e);
        }
        return new AcBonusLogVO();
    }
}
