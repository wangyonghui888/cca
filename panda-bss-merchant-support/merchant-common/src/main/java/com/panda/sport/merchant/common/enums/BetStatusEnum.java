package com.panda.sport.merchant.common.enums;

public enum BetStatusEnum {
    NO_EVENTUATE(0, "无结果"),
    DRAW(2, "走水"),
    LOSE(3, "输"),
    WIN(4, "赢"),
    HALF_WIN(5, "赢一半"),
    HALF_lOSE(6, "输一半"),
    CANCELLED(7, "赛事取消"),
    POSTPONEMENT(8, "赛事延期"),

    ;
    private Integer code;
    private String describe;

    BetStatusEnum(Integer code, String describe) {
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
