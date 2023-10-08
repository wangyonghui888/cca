package com.panda.center.enums;

/**
 * 盲盒类型
 */
public enum  BoxTypeEnum {

    SILVER(1, "白银"),
    GOLD(2, "黄金"),
    DIAMOND(3, "砖石"),
    ;
    private Integer code;
    private String describe;

    BoxTypeEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
