package com.panda.center.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 账户表
 * </p>
 *
 * @author Auto Generator
 * @since 2020-01-23
 */
@Data
public class TActivityMerchant implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 表ID，自增
     */
    private Long id;
    private String name;

    private Integer status;

    private Integer type;

    private Long activityId;

    private Long startTime;
    private Long endTime;

    private Long inStartTime;
    private Long inEndTime;

    private String pcUrl;

    private String h5Url;
    /**
     * 1 未开始
     * 2 进行中
     * 3 已结束
     */
    private Integer period;
}
