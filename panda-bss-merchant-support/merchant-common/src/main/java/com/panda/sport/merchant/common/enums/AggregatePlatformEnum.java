package com.panda.sport.merchant.common.enums;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public enum AggregatePlatformEnum {
    TY("TY", "体育"),
    DJ("DJ", "电竞"),
    CP("CP", "彩票"),
    ;

    private final String code;
    private final String value;

    AggregatePlatformEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static final List<JSONObject> list = Lists.newArrayList();

    public static final Map<String, AggregatePlatformEnum> platformEnumMap = Maps.newHashMap();

    static {
        for (AggregatePlatformEnum platformEnum : AggregatePlatformEnum.values()) {
            platformEnumMap.put(platformEnum.getCode(), platformEnum);
            JSONObject obj = JSONUtil.createObj();
            obj.put("code", platformEnum.getCode());
            obj.put("value", platformEnum.getValue());
            list.add(obj);
        }
    }
}
