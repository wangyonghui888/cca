package com.panda.multiterminalinteractivecenter.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :  daniel
 * 域名是否可以使用
 */
@Getter
@AllArgsConstructor
public enum DomainEnableEnum {

    // 未使用废弃 统一使用待使用状态
    // NOT_USE(0, "未使用"),
    USED(1, "已使用"),
    WAIT_USE(2, "待使用"),
    ATTACK(3,"被攻击") ,
    HIJACK(4,"被劫持") ,
    ;

    @JsonValue
    private final Integer code;
    private final String value;


    private static final Map<Integer, DomainEnableEnum> map =
            Arrays.stream(DomainEnableEnum.values()).collect(Collectors.toMap(DomainEnableEnum::getCode, e -> e));

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DomainEnableEnum resolve(Integer code) {
        if (!map.containsKey(code)) throw  new RuntimeException("没有找到对应枚举");
        return map.get(code);
    }

    public static String getValueByCode(Integer code){
        if(null == code) return "";
        for (DomainEnableEnum value : DomainEnableEnum.values()) {
            if(value.getCode().equals(code)) {
                return value.getValue();
            }
        }
        return "";
    }

}
