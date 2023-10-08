package com.panda.sport.merchant.common.enums;

public enum LanguageEnum {
    ZS("zs", "中文"),
    EN("en", "英语"),
    JP("jp", "日本"),
    ;
    private String code;
    private String describe;

    LanguageEnum(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

}
