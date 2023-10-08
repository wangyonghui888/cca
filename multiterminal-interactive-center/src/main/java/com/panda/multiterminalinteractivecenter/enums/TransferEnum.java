package com.panda.multiterminalinteractivecenter.enums;

import java.util.HashMap;
import java.util.Map;

public enum TransferEnum {
    TY("ty",  "ty" , 0),
    DJ("dj",  "dj_cp", 1),
    CP("cp",  "dj_cp", 2)

    ;
    private String code;

    private String value;

    private Integer type;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    TransferEnum(String code, String value,Integer type) {
        this.code = code;
        this.value = value;
        this.type = type;
    }

    private static final Map<String, TransferEnum> ENUM_MAP = new HashMap<>();
    static {
        for(TransferEnum groupTypeEnum : TransferEnum.values()){
            ENUM_MAP.put(groupTypeEnum.getCode(),groupTypeEnum);
        }
    }

    public static String getByCode(String code){
        return ENUM_MAP.get(code).getValue();
    }

    public static Integer getByType(String code){
        return ENUM_MAP.get(code).getType();
    }


}
