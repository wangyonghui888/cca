package com.panda.multiterminalinteractivecenter.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  ifan
 * @Package Name :  com.panda.sport.merchant.common.enums
 * @Description :  专属类型枚举
 * @Date: 2022-07-02 11:15
 * --------  ---------  --------------------------
 */
public enum ExclusiveEnum {

    AREA(1,  "区域专属"),
    VIP(2,  "VIP专属"),


    ;

    /**
     * 专属类型
     */
    private Integer exclusiveType;

    /**
     * 专属名称
     */
    private String exclusiveName;

    public Integer getExclusiveType() {
        return exclusiveType;
    }

    public void setExclusiveType(Integer exclusiveType) {
        this.exclusiveType = exclusiveType;
    }

    public String getExclusiveName() {
        return exclusiveName;
    }

    public void setExclusiveName(String exclusiveName) {
        this.exclusiveName = exclusiveName;
    }

    ExclusiveEnum(Integer exclusiveType, String exclusiveName) {
        this.exclusiveType = exclusiveType;
        this.exclusiveName = exclusiveName;
    }
    private static final Map<Integer,ExclusiveEnum> ENUM_MAP = new HashMap<>();
    static {
        for(ExclusiveEnum exclusiveEnum : ExclusiveEnum.values()){
            ENUM_MAP.put(exclusiveEnum.getExclusiveType(),exclusiveEnum);
        }
    }

    public static ExclusiveEnum getByExclusiveType(Integer exclusiveType){
        return ENUM_MAP.get(exclusiveType);
    }
}
