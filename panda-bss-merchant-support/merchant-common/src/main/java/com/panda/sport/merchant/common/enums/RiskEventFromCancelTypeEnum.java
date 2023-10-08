package com.panda.sport.merchant.common.enums;

/**
 * 此枚举作用为riskEvent字段，如果cancelType对应的值在里面，则需要设置riskEvent字段值，
 * cancelType为0或18时 不需要设置riskEvent字段值
 * 1比赛取消，2比赛延期， 3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，10联赛错误，
 * 11比分错误，12电视裁判， 13主客场错误，14赛制错误，15赛程错误，16时间错误，17赛事提前，19数据源取消，20比赛延迟，
 * 21操盘手取消，22主动弃赛，23并列获胜，24中途弃赛，25统计错误， 40 盘口失效，41PA自动拒单，42业务拒单，43MTS拒单
 * @author javier
 */
public enum RiskEventFromCancelTypeEnum {

    ONE(1, "比赛取消"),
    TWO(2, "比赛延期"),
    THREE(3, "比赛中断"),
    FOUR(4, "比赛重赛"),
    FIVE(5, "比赛腰斩"),
    SIX(6, "比赛放弃"),
    SEVEN(7, "盘口错误"),
    EIGHT(8, "赔率错误"),
    NINE(9, "队伍错误"),
    TEN(10, "联赛错误"),
    ELEVEN(11, "比分错误"),
    TWELVE(12, "电视裁判"),
    THIRTEEN(13, "主客场错误"),
    FOURTEEN(14, "赛制错误"),
    FIFTEEN(15, "赛程错误"),
    SIXTEEN(16, "事件错误"),
    SEVENTEEN(17, "赛事提前"),
    NINETEEN(19, "数据源取消"),
    TWENTY(20, "比赛延迟"),
    TWENTY_ONE(21, "操盘手取消"),
    TWENTY_TWO(22, "主动弃赛"),
    TWENTY_THREE(23, "并列获胜"),
    TWENTY_FOUR(24, "中途弃赛"),
    TWENTY_FIVE(25, "统计错误"),
    FORTY(40, "盘口失效"),
    FORTY_ONE(41, "自动拒单"),
    FORTY_TWO(42, "业务拒单"),
    FORTY_THREE(43, "MTS拒单")
    ;

    private Integer code;
    private String describe;

    RiskEventFromCancelTypeEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static String getDescByCode(Integer code){
        for (RiskEventFromCancelTypeEnum menuTypeEnum : values()) {
            if (menuTypeEnum.getCode().equals(code)) {
                return menuTypeEnum.getDescribe();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
