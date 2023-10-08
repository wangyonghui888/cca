package com.panda.sport.merchant.common.enums;

/**
 * @author :  Spring
 * @Project Name :
 * @Package Name :
 * @Description : 
 * @Date: 2019-08-29 下午1:59:53
 */
public enum CommonParameterEnum implements BaseEnum {

    USER_SYSTEM_BG ("0","USER_SYSTEM_BG","用户系统默认背景图片")
    ;

    private String id;

    private String code;

    private String label;

    CommonParameterEnum(String id,String code,String label){
        this.id = id;
        this.code = code;
        this.label = label;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
