package com.panda.sport.merchant.common.enums;

public enum TransferModeEnum {


    NOT_TRANSFER(1, "免转"),
    TRANSFER(2, "转账"),
    ;

    private Integer code;
    private String describe;

    TransferModeEnum(Integer code, String describe) {
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
