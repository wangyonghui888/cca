package com.panda.sport.merchant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 *  UI类型枚举
 */
@Getter
@AllArgsConstructor
public enum UIThemeEnum {

    /**
     * 白色版
     */
    WHITE(1, "白色版"),

    /**
     * 深色版
     */
    DARK(2, "深色版"),
    ;

    private final Integer code;
    private final String label;


    public static UIThemeEnum resolve(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().get();
    }
}
