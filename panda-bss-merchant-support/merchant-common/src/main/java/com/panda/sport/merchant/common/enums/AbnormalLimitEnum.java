package com.panda.sport.merchant.common.enums;

/**
 * @author :  javier
 * 用户限额枚举
 */
public enum AbnormalLimitEnum {

    /**
     * 用户限额类型枚举
     */
    LIMIT_TYPE_2(2, "限额百分比","Percentage of limit"),
    LIMIT_TYPE_3(3, "备注","Remark"),
    LIMIT_TYPE_4(4, "操作人","Operator"),
    LIMIT_TYPE_5(5, "投注特殊限额","Betting Special Limit"),
    LIMIT_TYPE_8(8, "冠军玩法限额比例","Championship game limit ratio"),

    LIMIT_TYPE_130(130, "单关单注赔付限额-全部","Payout Limit for Single Pass Single Bet - All"),
    LIMIT_TYPE_131(131, "单关单注赔付限额-足球","Payout Limit for Single Bet Single Bet-Football"),
    LIMIT_TYPE_132(132, "单关单注赔付限额-篮球","Payout Limit for Single Bet Single Bet-Basketball"),
    LIMIT_TYPE_133(133, "单关单注赔付限额-其他","Payout Limit for Single Pass Single Bet - Others"),
    LIMIT_TYPE_120(120, "单关单场赔付限额-全部","Payout Limit for Single Pass and Single Game - All"),
    LIMIT_TYPE_121(121, "单关单场赔付限额-足球","Payout Limit for Single Level Single Game-Football"),
    LIMIT_TYPE_122(122, "单关单场赔付限额-篮球","Single Level Single Game Payout Limit-Basketball"),
    LIMIT_TYPE_123(123, "单关单场赔付限额-其他","Payout Limit for Single Pass and Single Game-Others"),
    LIMIT_TYPE_210(210, "串关单日赔付限额-全部","Collusion Single-day Payout Limit - All"),
    LIMIT_TYPE_220(220, "串关单场赔付限额-全部","Collusion Single Game Payout Limit - All"),
    LIMIT_TYPE_230(230, "串关单注赔付限额-全部","Payout Limit for Single Bet - All");

    private Integer code;

    private String remark;

    private String enRemark;

    AbnormalLimitEnum(Integer code, String remark, String enRemark) {
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
