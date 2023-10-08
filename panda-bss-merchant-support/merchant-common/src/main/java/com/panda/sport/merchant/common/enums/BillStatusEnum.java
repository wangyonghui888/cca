package com.panda.sport.merchant.common.enums;

/**
 * @Author: Joken
 * @Description: 结算状态枚举
 * @Date: 2019/10/9 17:23
 * @Param:
 * @Return:
 */
public enum BillStatusEnum {
    WAIT_DEAL(0, "未结算"),
    FIXED(1, "已结算"),
    EXCEPTION(2, "结算异常"),

    ;
    private Integer code;
    private String describe;

    BillStatusEnum(Integer code, String describe) {
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
