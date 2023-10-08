package com.panda.sport.merchant.common.enums;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.enums
 * @Description: 消息发送类型
 * @Date: 2019/12/30 18:37
 * @Version: v1.0
 */
public enum MQMessageTypeEnum {

    ORDER_SETTLE(1, "订单结算"),
    ORDER_STATUS(2, "订单状态"),

    ;
    private Integer code;
    private String describe;

    MQMessageTypeEnum(Integer code, String describe) {
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
