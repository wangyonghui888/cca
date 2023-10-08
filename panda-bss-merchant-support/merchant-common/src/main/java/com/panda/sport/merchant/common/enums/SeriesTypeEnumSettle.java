package com.panda.sport.merchant.common.enums;

/**
 * @Author: Joken
 * @Description: 串关类型
 * @Date: 2019/10/10 16:17
 * @Param:
 * @Return:
 */
public enum SeriesTypeEnumSettle {

    OTHERS(1, "串1"),
    SINGER(01, "单关"),
    DOUBLE(2, "2串1"),
    THREE(3, "3串1"),
    THREE_4(34, "3串4"),
    FOUR(4, "4串1"),
    FOUR_11(411, "4串11"),
    FIVE(5, "5串1"),
    FIVE_26(526, "5串26"),
    SIX(6, "6串1"),
    SIX_57(657, "6串57"),
    SEVEN(7, "7串1"),
    SEVEN_120(7120, "7串120"),
    EIGHT(8, "8串1"),
    EIGHT_247(8247, "8串247"),
    NINE(9, "9串1"),
    NINE_502(9502, "9串502"),
    TEN(10, "10串1"),
    TEN_1013(101013, "10串1013"),

    ;
    private Integer code;
    private String describe;

    SeriesTypeEnumSettle(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
