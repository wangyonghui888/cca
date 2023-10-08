package com.panda.sport.merchant.common.enums;

public enum BizTypeEnum {

    ADD_AMOUNT(1, "Transfer In", "转入"),
    SUB_AMOUNT(2, "Transfer Out", "转出"),
    BET(3, "Bet", "下注"),
    SETTLEMENT(4, "Settle", "结算"),
    REFUND(5, "Refund", "退款"),
    FREEZE(6, "Frozen", "冻结"),
    JACKPOT(7, "Promotion", "彩金"),
    REJECT_ORDER(8, "Refuse", "拒单"),
    REFUND_SETTLEMENT(9, "SettleRollback", "结算回滚"),
    BET_CANCEL(10, "Cancel", "下注取消"),
    BET_CANCEL_ROLLBACK(11, "CancelRollback", "下注取消回滚"),
    ADD_MONEY_MANUALLY(12,"AddMoneyManually","手动加款"),
    MANUAL_DEBIT(13,"ManualDebit","手动扣款");

    private Integer code;
    private String en;
    private String describe;

    BizTypeEnum(Integer code, String en, String describe) {
        this.code = code;
        this.describe = describe;
        this.en = en;
    }

    public static String getDescByCode(Integer code) {
        for (BizTypeEnum menuTypeEnum : values()) {
            if (menuTypeEnum.getCode().equals(code)) {
                return menuTypeEnum.getDescribe();
            }
        }
        return null;
    }

    public static String getEnByCode(Integer code) {
        for (BizTypeEnum menuTypeEnum : values()) {
            if (menuTypeEnum.getCode().equals(code)) {
                return menuTypeEnum.getEn();
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

    public String getEn() {
        return en;
    }
}
