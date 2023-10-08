package com.panda.sport.merchant.common.enums;


/**
* @Description: 上游系统
* @Author: Jeffrey
* @Date: 2019/12/9
*/
public enum SysSourceEnum {

    MOBILE(1, "MOBILE"),
    PC(2, "PC"),
    H5(3, "H5")
    ;
    private Integer code;
    private String describe;

    SysSourceEnum(Integer code, String describe) {
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
