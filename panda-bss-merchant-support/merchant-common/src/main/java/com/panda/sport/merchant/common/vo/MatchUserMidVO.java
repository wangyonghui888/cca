package com.panda.sport.merchant.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author streams
 * @version 1.0
 * @date 2023/5/7 11:57:27
 */
@Getter
@Setter
@ToString
@Slf4j
public class MatchUserMidVO implements Serializable {


    /**
     * 注单数
     */
    private Long orderCount;

    /**
     * 活动ID
     */
    private Long matchId;

    /**
     * 活动名称
     */
    private Long userId;


    private Long beginTime;

}
