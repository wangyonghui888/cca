package com.panda.center.vo;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityEntranceVO {

    /**
     * 每页显示条数(默认十条)
     */
    private Integer pageSize = 20;
    /**
     * 当前页数
     */
    private Integer pageNum = 1;
    /**
     * 商户名称
     */
    private String merchantCode;
    /**
     * 活动名称
     */
    private String activityName;

    private Integer start;

}
