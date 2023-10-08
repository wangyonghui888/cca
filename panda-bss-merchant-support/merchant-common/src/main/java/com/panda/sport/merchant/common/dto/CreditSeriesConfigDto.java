package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 串关每日投注限额
 * @Author : Paca
 * @Date : 2021-04-27 15:39
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class CreditSeriesConfigDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 串关类型：
     * <li>2-2串1</li>
     * <li>3-3串N</li>
     * <li>4-4串N</li>
     * <li>5-5串N</li>
     * <li>6-6串N</li>
     * <li>7-7串N</li>
     * <li>8-8串N</li>
     * <li>9-9串N</li>
     * <li>10-10串N</li>
     */
    private Integer seriesType;

    /**
     * 限额值，单位元
     */
    private BigDecimal value;
}
