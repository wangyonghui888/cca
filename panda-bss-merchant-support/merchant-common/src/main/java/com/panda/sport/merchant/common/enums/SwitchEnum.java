package com.panda.sport.merchant.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 开关枚举
 */
@Getter
@AllArgsConstructor
public enum SwitchEnum {

    /**
     * 开
     */
    ON(1, "开"),

    /**
     * 关
     */
    OFF(0, "关")
    ;

    private final Integer code;
    private final String label;


    public static SwitchEnum getInstance(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().get();
    }
}
