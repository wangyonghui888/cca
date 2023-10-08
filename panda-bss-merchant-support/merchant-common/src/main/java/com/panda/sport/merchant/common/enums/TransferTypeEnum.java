package com.panda.sport.merchant.common.enums;

public enum TransferTypeEnum {

    ADD_AMOUNT("1", "加款"),
    SUB_AMOUNT("2", "扣款"),
    ;

    private String code;
    private String describe;

    TransferTypeEnum(String code, String describe) {
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
