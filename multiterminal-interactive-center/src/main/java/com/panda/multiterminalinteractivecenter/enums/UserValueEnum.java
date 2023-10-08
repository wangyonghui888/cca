package com.panda.multiterminalinteractivecenter.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  ifan
 * @Package Name :  com.panda.sport.merchant.common.enums
 * @Description :  用户价值枚举
 * @Date: 2022-07-02 11:15
 * --------  ---------  --------------------------
 */
public enum UserValueEnum {

    USER_VALUE_1(1,  "1"),
    USER_VALUE_2(2,  "2"),
    USER_VALUE_3(3,  "3"),
    USER_VALUE_4(4,  "4"),
    USER_VALUE_5(5,  "5"),
    USER_VALUE_6(6,  "6"),
    USER_VALUE_7(7,  "7"),
    USER_VALUE_8(8,  "8"),
    USER_VALUE_9(9,  "9"),
    USER_VALUE_10(10,  "10"),


    ;

    /**
     * 用户价值code
     */
    private Integer code;

    /**
     * 用户价值名称
     */
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    UserValueEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer,UserValueEnum> ENUM_MAP = new HashMap<>();
    static {
        for(UserValueEnum userValueEnum : UserValueEnum.values()){
            ENUM_MAP.put(userValueEnum.getCode(),userValueEnum);
        }
    }

    public static UserValueEnum getByCode(Integer code){
        return ENUM_MAP.get(code);
    }
}
