package com.panda.sport.merchant.common.enums;

/**
 * @Author: butr
 * @Description: 派彩状态
 * @Date: 2019/10/23 16:17
 * @Param:
 * @Return:
 */
public enum PayoutStatusEnum {

    NO_LOTTERY(0, "未派彩"),
    LOTTERY(1, "已派彩"),

    ;
    private Integer code;
    private String describe;

    PayoutStatusEnum(Integer code, String describe) {
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
