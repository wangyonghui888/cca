package com.panda.multiterminalinteractivecenter.enums;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.enums
 * @Description :  踢出用户类别方式
 * @Date: 2022-03-16 11:52:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public enum KickUserTypeEnum {

    device(1,"设备"),
    user(2,"用户"),
    merchant(3,"商户"),
    allUser(4,"全部");

    private Integer code;

    private String desc;

    KickUserTypeEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
