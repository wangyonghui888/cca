package com.panda.sport.merchant.common.po.bss;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SDailyLuckyBoxNumberPO {

    // 类型 盲盒类型，1：白银盲盒 2：黄金盲盒 3：钻石盲盒
    private Integer boxType;

    // 每日出现次数
    private Integer dailyNumber;

    // 时间间隔单位：分钟
    private Integer showRate;

    // 在指定的时间间隔内开放的数量
    private Integer showNumber;

    // 在指定的时间间隔内开放的数量
    private Integer token;

    // 创建时间
    private Long createTime;

    private Long modifyTime;

    private String createdBy;

    private String updatedBy;
}
