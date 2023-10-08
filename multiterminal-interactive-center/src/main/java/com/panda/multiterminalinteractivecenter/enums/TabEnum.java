package com.panda.multiterminalinteractivecenter.enums;

/**
 * @author :  istio
 * @Description :  三端
 * --------  ---------  --------------------------
 */
public enum TabEnum {

    TY(1, "ty"),
    DJ(2, "dj"),
    CP(3, "cp"),
    ;

    private Integer code;

    private String name;

    private TabEnum(Integer code, String name) {
        this.code = code;
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
}
