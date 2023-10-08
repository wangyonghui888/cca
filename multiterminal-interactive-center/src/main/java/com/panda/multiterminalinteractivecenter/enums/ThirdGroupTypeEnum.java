package com.panda.multiterminalinteractivecenter.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  ifan
 * @Package Name :  com.panda.sport.merchant.common.enums
 * @Description :  分组类型枚举
 * @Date: 2022-07-02 11:15
 * --------  ---------  --------------------------
 */
public enum ThirdGroupTypeEnum {
    YUNWEI_GROUP(1,  "运维组","yunwei"),
    YEWU_GROUP(2,  "业务组","yewu"),
    COMMON_GROUP(3,  "公共组","common"),
    ;

    /**
     * 分组类型
     */
    private Integer groupType;

    /**
     * 分组名称
     */
    private String groupName;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    ThirdGroupTypeEnum(Integer groupType, String groupName, String code) {
        this.groupType = groupType;
        this.groupName = groupName;
        this.code = code;
    }

    private static final Map<Integer, ThirdGroupTypeEnum> ENUM_MAP = new HashMap<>();
    static {
        for(ThirdGroupTypeEnum groupTypeEnum : ThirdGroupTypeEnum.values()){
            ENUM_MAP.put(groupTypeEnum.getGroupType(),groupTypeEnum);
        }
    }

    private static final Map<String, ThirdGroupTypeEnum> ENUM_CODE_MAP = new HashMap<>();
    static {
        for(ThirdGroupTypeEnum groupTypeEnum : ThirdGroupTypeEnum.values()){
            ENUM_CODE_MAP.put(groupTypeEnum.getCode(),groupTypeEnum);
        }
    }
    private static final Map<Integer, ThirdGroupTypeEnum> ENUM_TYPE_MAP = new HashMap<>();
    static {
        for(ThirdGroupTypeEnum groupTypeEnum : ThirdGroupTypeEnum.values()){
            ENUM_TYPE_MAP.put(groupTypeEnum.getGroupType(),groupTypeEnum);
        }
    }

    public static ThirdGroupTypeEnum getByGroupType(Integer groupType){
        return ENUM_MAP.get(groupType);
    }


    public static ThirdGroupTypeEnum getByCode(Integer code){
        return ENUM_CODE_MAP.get(code);
    }

    public static Integer getTypeIntByCode(String code){
        if(null == code) return null;
        return ENUM_CODE_MAP.get(code).getGroupType();
    }

    public static String getStrByType(Integer type){
        if(null == type) return null;
        return ENUM_TYPE_MAP.get(type).getGroupName();
    }

}
