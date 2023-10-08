package com.panda.sport.merchant.common.enums;

/**
 * @Author: butr
 * @Description: 结算类型
 * @Date: 2019/10/23 16:17
 * @Param:
 * @Return:
 */
public enum SettleTypeEnum {

    AUTO(1, "自动结算"),
    HAND(2, "手动结算"),

    ;
    private Integer code;
    private String describe;

    SettleTypeEnum(Integer code, String describe) {
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
