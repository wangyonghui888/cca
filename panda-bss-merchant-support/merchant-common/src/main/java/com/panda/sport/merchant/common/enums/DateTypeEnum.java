package com.panda.sport.merchant.common.enums;

/**
 * 商户报表查询
 * 日期类型对应表
 */
public enum DateTypeEnum {

    DAY(1,"merchant_sport_order_day"),
    WEEK(2,"merchant_sport_order_week"),
    MONTH(3,"merchant_sport_order_month"),
    YEAR(4,"merchant_sport_order_month");


    private Integer code;

    private String desc;

     DateTypeEnum(Integer codeType, String desc){
        this.code = codeType;
        this.desc = desc;
    }

    /**
     *
     * @param codeType
     * @return SportMenuTypeEnum
     * @throws Exception
     */
    public static DateTypeEnum getDateTypeEnum(Integer codeType){
        DateTypeEnum[] allEnum = DateTypeEnum.values();
        for (int i = 0; i < allEnum.length; i++) {
            DateTypeEnum allDatum = allEnum[i];
            if(allDatum.getCode().equals(codeType)){
                return  allDatum;
            }
        }
        return null;

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
