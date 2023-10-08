package com.panda.sport.admin.en;

/**
 * @author YK
 * @Description:
 * @date 2020/2/29 21:13
 */
public enum ReportNamesEnum {

    MERCHANT_REPORT("merchant-report", "商户报表"),
    USER_REPORT("user-report", "用户订单"),
    ;
    private String code;
    private String desc;

    ReportNamesEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
