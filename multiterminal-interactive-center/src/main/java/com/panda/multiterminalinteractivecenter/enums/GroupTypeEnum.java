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
public enum GroupTypeEnum {
    C_GROUP(1,  "公共组","common"),
    Y_GROUP(2,  "Y组(Y系)","y"),
    S_GROUP(3,  "S组(S系)","s"),
    B_GROUP(4,  "B组(B系)","b"),

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

    GroupTypeEnum(Integer groupType, String groupName,String code) {
        this.groupType = groupType;
        this.groupName = groupName;
        this.code = code;
    }

    private static final Map<Integer,GroupTypeEnum> ENUM_MAP = new HashMap<>();
    static {
        for(GroupTypeEnum groupTypeEnum : GroupTypeEnum.values()){
            ENUM_MAP.put(groupTypeEnum.getGroupType(),groupTypeEnum);
        }
    }

    private static final Map<String,GroupTypeEnum> ENUM_CODE_MAP = new HashMap<>();
    static {
        for(GroupTypeEnum groupTypeEnum : GroupTypeEnum.values()){
            ENUM_CODE_MAP.put(groupTypeEnum.getCode(),groupTypeEnum);
        }
    }
    private static final Map<Integer,GroupTypeEnum> ENUM_TYPE_MAP = new HashMap<>();
    static {
        for(GroupTypeEnum groupTypeEnum : GroupTypeEnum.values()){
            ENUM_TYPE_MAP.put(groupTypeEnum.getGroupType(),groupTypeEnum);
        }
    }

    public static GroupTypeEnum getByGroupType(Integer groupType){
        return ENUM_MAP.get(groupType);
    }


    public static GroupTypeEnum getByCode(Integer code){
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
