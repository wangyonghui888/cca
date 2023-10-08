package com.panda.sport.merchant.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 域名类型
 */
public enum DomainTypeEnum {

    H5(1,"h5","H5"),
    PC(2,"pc","PC"),
    APP(3,"api","API"),
    IMAGE(4,"img","图片"),
    OTHER(5,"other","其他"),
    VIDEO_ALL(6,"video_all","全量域名"),
    VIDEO_Exciting_Editing(7,"Exciting Editing","精彩剪辑"),

    ;

    private Integer code;

    private String value;
    private String name;

    private DomainTypeEnum(Integer code, String value, String name){
        this.code = code;
        this.value = value;
        this.name = name;
    }

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private static final Map<Integer, DomainTypeEnum> ENUM_CODE_MAP = new HashMap<>();
    static {
        for(DomainTypeEnum domainTypeEnum : DomainTypeEnum.values()){
            ENUM_CODE_MAP.put(domainTypeEnum.getCode(),domainTypeEnum);
        }
    }

    public static DomainTypeEnum getByCode(Integer code){
        return ENUM_CODE_MAP.get(code);
    }

    public static Boolean paramCanInDifferentGroupType(Integer domainType){
        List<Integer> list = Arrays.asList(H5.code, PC.code,IMAGE.code);
        return list.contains(domainType);
    }

    public static String getNameByCode(Integer code){
        if(null == code) return "";
        for (DomainTypeEnum value : DomainTypeEnum.values()) {
            if(value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
