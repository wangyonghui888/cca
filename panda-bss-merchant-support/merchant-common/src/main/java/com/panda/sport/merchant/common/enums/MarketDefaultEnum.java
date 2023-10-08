package com.panda.sport.merchant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 *  默认赔率类型
 */
@Getter
@AllArgsConstructor
public enum MarketDefaultEnum {

    /**
     * 欧洲盘
     */
    EUREKA(1, "欧洲盘"),

    /**
     * 香港
     */
    HONGKONG(2, "香港盘")
    ;

    private final Integer code;
    private final String label;


    public static MarketDefaultEnum getInstance(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().get();
    }
}
