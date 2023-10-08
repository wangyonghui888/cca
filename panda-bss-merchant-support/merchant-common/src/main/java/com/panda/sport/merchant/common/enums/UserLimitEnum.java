package com.panda.sport.merchant.common.enums;

/**
 * @author :  javier
 * 用户限额枚举
 */
public enum UserLimitEnum {

    /**
     * 用户限额枚举
     */
    LIMIT_TYPE_1(1, "无","None"),
    LIMIT_TYPE_2(2, "特殊百分比限额","Special Percentage Limit"),
    LIMIT_TYPE_3(3, "特殊单注单场限额","Special single bet single game limit"),
    LIMIT_TYPE_4(4, "特殊VIP限额","Special VIP Limit"),

    LIMIT_TYPE_5(5, "Credit限额","Credit limit");


    private Integer code;

    private String remark;

    private String enRemark;

    UserLimitEnum(Integer code, String remark,String enRemark) {
        this.code = code;
        this.remark = remark;
        this.enRemark = enRemark;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getEnRemark() {
        return enRemark;
    }

    public void setEnRemark(String enRemark) {
        this.enRemark = enRemark;
    }
}
