package com.panda.center.result;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 黄金
 */
@Setter
@Getter
@Accessors(chain = true)
public class SDailyLuckyBoxNumberVo {

    // 类型 盲盒类型，1：白银盲盒 2：黄金盲盒 3：钻石盲盒

    // 白银每日出现次数
    private Integer silverDailyNumber;

    // 黄金每日出现次数
    private Integer goldDailyNumber;

    // 钻石每日出现次数
    private Integer diamondDailyNumber;

    // 白银奖券数
    private Integer silverToken;

    // 黄金奖券数
    private Integer goldToken;

    // 钻石奖券数
    private Integer diamondToken;

    // 白银 时间间隔单位：分钟  供应
    private Integer showRate;


    // 白银指定的时间间隔内开放的数量
    private Integer silverShowNumber;

    // 黄金指定的时间间隔内开放的数量
    private Integer goldShowNumber;

    // 钻石指定的时间间隔内开放的数量
    private Integer diamondShowNumber;

}
