package com.panda.sport.merchant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 启用状态枚举
 */
@Getter
@AllArgsConstructor
public enum EnableStatusEnum {

    /**
     * 启用
     */
    ENABLE(1, "开"),

    /**
     * 禁用
     */
    DISABLE(0, "关")
    ;

    private final Integer code;
    private final String label;


    public static EnableStatusEnum getInstance(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().get();
    }
}
