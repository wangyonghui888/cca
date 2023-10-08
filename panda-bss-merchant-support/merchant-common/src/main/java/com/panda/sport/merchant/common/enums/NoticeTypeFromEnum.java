package com.panda.sport.merchant.common.enums;

/**
 * 公告来源
 */
public enum NoticeTypeFromEnum {

    MERCHANT_MANAGEMENT(1, "B端商户管理"),
    MERCHANT_BACKGROUND(2, "B端商户后台"),
    SCHEDULE_MANAGEMENT(3, "赛程管理平台"),
    ACTIVITY_MANAGEMENT(4, "活动管理平台"),
    TRADING_MANAGEMENT(5,"操盘工具平台"),
    RISK_MANAGEMENT(6,"风控工具平台"),
    ;

    private Integer code;
    private String describe;

    NoticeTypeFromEnum(Integer code, String describe) {
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
