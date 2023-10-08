package com.panda.sport.merchant.common.enums;

/**
 * @author :  sklee
 * @Project Name :
 * @Package Name :
 * @Description : 对应栏目体育编码枚举
 * @Date: 2019-09-02 19:10
 */
public enum SportMenuCodeEnum {

    IS_LIVE("IS_LIVE","滚球"),
    TODAY_MATCH("TODAY_MATCH","今日赛事"),
    FOOTBALL("FOOTBALL","足球"),
    FOOTBALL_LIVE("FOOTBALL_LIVE","足球滚球"),
    ALL_LIVE("ALL_LIVE","全部滚球"),
    ALL_SOON_MATCH("ALL_SOON_MATCH","即将开赛");
    private String sportCode;

    private String desc;

    private SportMenuCodeEnum(String sportCode, String desc){
        this.sportCode = sportCode;
        this.desc = desc;
    }

    public String getSportCode() {
        return sportCode;
    }

    public void setSportCode(String sportCode) {
        this.sportCode = sportCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
