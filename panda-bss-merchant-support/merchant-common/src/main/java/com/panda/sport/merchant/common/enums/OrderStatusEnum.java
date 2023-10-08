package com.panda.sport.merchant.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Joken
 * @Description: 订单状态枚举
 * @Date: 2019/10/9 17:23
 * @Param:
 * @Return:
 */
public enum OrderStatusEnum {
    WAIT_DEAL(0, "待处理"),
    FIXED(1, "已处理"),
    CANCEL(2, "已取消"),
    WAIT_CONFIRM(3, "待确认"),
    REFUSE(4, "已拒绝"),

    ;
    private Integer code;
    private String describe;

    OrderStatusEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    private static Map<Integer, OrderStatusEnum> orderStatusMap = new HashMap<>();

    //通过订单状态码查询对应枚举对象
    public static OrderStatusEnum getOrderStatusEnumByCode(Integer code) {
        if (orderStatusMap == null && orderStatusMap.size() == 0) {
            for (OrderStatusEnum obj : values()) {
                orderStatusMap.put(obj.getCode(), obj);
            }
        }
        if (orderStatusMap.get(code) == null) {
            for (OrderStatusEnum obj : values()) {
                orderStatusMap.put(obj.getCode(), obj);
            }
        }
        return orderStatusMap.get(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

}
