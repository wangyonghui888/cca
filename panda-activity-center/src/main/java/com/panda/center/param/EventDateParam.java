package com.panda.center.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/29 21:49:59
 */
@Setter
@Getter
@ToString
public class EventDateParam {
    private Long id;
    /**
     * 活动类型( 1:常规活动 2:自定义活动 3:特殊活动)
     */
    private Integer type;
    /**
     * 开始时间
     */
    private Long inStartTime;
    /**
     * 结束时间
     */
    private Long inEndTime;
}
