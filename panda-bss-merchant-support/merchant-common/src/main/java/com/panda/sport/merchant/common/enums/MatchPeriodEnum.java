package com.panda.sport.merchant.common.enums;

/**
 * @author :  sklee
 * @Project Name :
 * @Package Name :
 * @Description :赛事阶段枚举类
 * @Date: 2019-09-26 20:35
 */
public enum MatchPeriodEnum {
    N0_START(0,"未开赛"),
    UP_P(1,"上半场"),
    PAUSED(2,"中场休息"),
    LOWER(3,"下半场"),
    ENDED(4,"全场结束"),
   AWAITING_OT (5,"即将加时"),
    OVER_TIME(6,"加时赛"),
    FP_OT(7,"加时赛上半场"),
    UP_OVER_TIME(8,"加时赛中休息"),
    LOWER_OVER_TIME(9,"加时赛下半场"),
    AFTER_OT(10,"加时赛结束"),
    AWAITING_PENa(11,"等待点球"),
    PEN(12,"点球大战"),
    PEN_OVER(13,"点球大战结束"),
    INTERRUPTED(14,"比赛中断"),
    ABANDONED(15,"比赛放弃"),
    GAME_OVER(16,"已完赛");

    private long code;
    private String  desc;

    private MatchPeriodEnum(long code, String  desc){

        this.code = code;
        this.desc = desc;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
