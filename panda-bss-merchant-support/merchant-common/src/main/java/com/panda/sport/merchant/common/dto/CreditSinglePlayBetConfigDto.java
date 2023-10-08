package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 信用玩法单注限额
 * @Author : Paca
 * @Date : 2021-07-17 13:10
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class CreditSinglePlayBetConfigDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赛种，1-足球，2-篮球，5-网球，-1-其它赛种
     */
    private Integer sportId;

    /**
     * 玩法分类
     */
    private Integer playClassify;

    /**
     * 投注阶段，pre-早盘，live-滚球
     */
    private String betStage;

    /**
     * 联赛等级，1-一级联赛，2-二级联赛，3-三级联赛，-1-其它联赛
     */
    private Integer tournamentLevel;

    /**
     * 限额值，单位元
     */
    private BigDecimal value;
}
