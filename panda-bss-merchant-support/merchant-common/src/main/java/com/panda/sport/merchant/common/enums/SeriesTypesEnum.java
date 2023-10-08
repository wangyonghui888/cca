package com.panda.sport.merchant.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.enums
 * @Description:
 * @Date: 2019/11/9 16:12
 * @Version: v1.0
 */
public enum SeriesTypesEnum {

    SINGLE(1, "单关","Single"),
    DOUBLE(2001, "2串1","Double"),
    THREE(3001, "3串1","Treble"),
    THREE_4(3004, "3串4","Trixie"),
    FOUR(4001, "4串1","4-Fold"),
    FOUR_11(40011, "4串11","Yankee"),
    FIVE(5001, "5串1","5-Fold"),
    FIVE_26(50026, "5串26","Super Ya"),
    SIX(6001, "6串1","6-Fold"),
    SIX_57(60057, "6串57","Heinz"),
    SEVEN(7001, "7串1","7-Fold"),
    SEVEN_120(700120, "7串120","Super Hei"),
    EIGHT(8001, "8串1","8-Fold"),
    EIGHT_247(800247, "8串247","Goliath"),
    NINE(9001, "9串1","9-Fold"),
    NINE_502(900502, "9串502","Block (9)"),
    TEN(10001, "10串1","10-Fold"),
    TEN_1013(10001013, "10串1013","Block (10)"),

    ;
    private Integer code;
    private String describe;
    private String enDesc;

    SeriesTypesEnum(Integer code, String describe, String enDesc) {
        this.code = code;
        this.describe = describe;
        this.enDesc = enDesc;
    }

    private static Map<Integer,SeriesTypesEnum> seriesTypeCodeMap = new HashMap<>();
    private static Map<String,SeriesTypesEnum> seriesTypeDescMap = new HashMap<>();

    //通过盘口描述查询对应枚举对象
    public static SeriesTypesEnum getSeriesTypesEnumByCode(Integer code) {
        if (seriesTypeCodeMap == null || seriesTypeCodeMap.size() == 0) {
            for (SeriesTypesEnum obj : values()) {
                seriesTypeCodeMap.put(obj.getCode(), obj);
            }
        }
        if (seriesTypeCodeMap.get(code) == null) {
            for (SeriesTypesEnum obj : values()) {
                seriesTypeCodeMap.put(obj.getCode(), obj);
            }
        }
        return seriesTypeCodeMap.get(code);
    }

    //通过盘口描述查询对应枚举对象
    public static SeriesTypesEnum getSeriesTypesEnumByDesc(String desc) {
        if (seriesTypeDescMap == null && seriesTypeDescMap.size() == 0) {
            for (SeriesTypesEnum obj : values()) {
                seriesTypeDescMap.put(obj.getDescribe(), obj);
            }
        }
        if (seriesTypeDescMap.get(desc) == null) {
            for (SeriesTypesEnum obj : values()) {
                seriesTypeDescMap.put(obj.getDescribe(), obj);
            }
        }
        return seriesTypeDescMap.get(desc);
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

    public String getEnDesc() {
        return enDesc;
    }
}
