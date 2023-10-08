package com.panda.sport.merchant.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: kiu
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.enums
 * @Description:
 * @Date: 2019/11/11 16:12
 * @Version: v1.3
 */
public enum SeriesTypeEnum {

    ONE(1, "串1"),
    TWO(2, "2串1"),
    THREE(3, "3串1"),
    FOUR(4, "4串1"),
    FIVE(5, "5串1"),
    SIX(6, "6串1"),
    SEVEN(7, "7串1"),
    EIGHT(8, "8串1"),
    NINE(9, "9串1"),
    TEN(10, "10串1"),

            ;
    private Integer code;
    private String describe;

    SeriesTypeEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    private static Map<Integer, SeriesTypeEnum> seriesTypeCodeMap = new HashMap<>();
    private static Map<String, SeriesTypeEnum> seriesTypeDescMap = new HashMap<>();

    //通过盘口描述查询对应枚举对象
    public static SeriesTypeEnum getSeriesTypesEnumByCode(Integer code) {
        if (seriesTypeCodeMap == null && seriesTypeCodeMap.size() == 0) {
            for (SeriesTypeEnum obj : values()) {
                seriesTypeCodeMap.put(obj.getCode(), obj);
            }
        }
        if (seriesTypeCodeMap.get(code) == null) {
            for (SeriesTypeEnum obj : values()) {
                seriesTypeCodeMap.put(obj.getCode(), obj);
            }
        }
        return seriesTypeCodeMap.get(code);
    }

    //通过盘口描述查询对应枚举对象
    public static SeriesTypeEnum getSeriesTypesEnumByDesc(String desc) {
        if (seriesTypeDescMap == null && seriesTypeDescMap.size() == 0) {
            for (SeriesTypeEnum obj : values()) {
                seriesTypeDescMap.put(obj.getDescribe(), obj);
            }
        }
        if (seriesTypeDescMap.get(desc) == null) {
            for (SeriesTypeEnum obj : values()) {
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
}
