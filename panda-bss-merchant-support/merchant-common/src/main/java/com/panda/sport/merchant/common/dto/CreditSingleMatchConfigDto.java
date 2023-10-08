package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 单场最高投注限额
 * @Author : Paca
 * @Date : 2021-04-27 15:57
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class CreditSingleMatchConfigDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赛种，1-足球，2-篮球，5-网球，-1-其它赛种
     */
    private Integer sportId;


    /**
     * 联赛等级，1-一级联赛，2-二级联赛，3-三级联赛，-1-其它联赛
     */
    private Integer tournamentLevel;

    /**
     * 限额值，单位元
     */
    private BigDecimal value;
}
