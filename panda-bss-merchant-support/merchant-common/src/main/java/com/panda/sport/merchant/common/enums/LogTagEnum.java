package com.panda.sport.merchant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 *  日志端枚举
 */
@Getter
@AllArgsConstructor
public enum LogTagEnum {

    /**
     * 商户端
     */
    MERCHANT_END(0, "商户端"),

    /**
     * 后端
     */
    BACK_END(1, "后端")
    ;

    private final Integer code;
    private final String label;


    public static LogTagEnum getInstance(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().get();
    }
}
