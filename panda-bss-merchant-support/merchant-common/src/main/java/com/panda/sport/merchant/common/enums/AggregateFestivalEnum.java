package com.panda.sport.merchant.common.enums;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum AggregateFestivalEnum {
    zh("zh", "中文简体"),
    tw("tw", "中文繁体"),
    en("en", "英文"),
    vi("vi", "越南语"),
    th("th", "泰语"),
    ms("ms", "马来语"),
    ad("ad", "印尼语"),
    pt("pt", "葡萄牙语"),
    ko("ko", "韩语"),
    es("es", "印尼语"),
    mya("mya", "西班牙语"),
    oth("oth", "其他语种"),
    ;

    private final String code;
    private final String value;

    AggregateFestivalEnum(String code, String value) {
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

    public static final Map<String, AggregateFestivalEnum> festivalEnumMap = Maps.newHashMap();

    static {
        for (AggregateFestivalEnum festivalEnum : AggregateFestivalEnum.values()) {
            festivalEnumMap.put(festivalEnum.getCode(), festivalEnum);
            JSONObject obj = JSONUtil.createObj();
            obj.put("code", festivalEnum.getCode());
            obj.put("value", festivalEnum.getValue());
            list.add(obj);
        }
    }

    public static AggregateFestivalEnum getInstance(String code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().orElse(AggregateFestivalEnum.oth);
    }
}
