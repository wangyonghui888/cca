package com.panda.sport.merchant.common.enums;

public enum TransferRecordBizTypeEnum {

    ADD_AMOUNT(1, "bet order", "投注"),
    SUB_AMOUNT(2, "Settlement payout", "结算派彩"),
    BET(3, "Bet Cancel", "注单取消"),
    SETTLEMENT(4, "Settle Cancel return", "注单取消回滚"),
    REFUND(5, "Settle return", "结算回滚"),
    FREEZE(6, "Reject order", "拒单"),
    JACKPOT(7, "Transfer in", "转入"),
    REJECT_ORDER(8, "Transfer out", "转出");

    private Integer code;
    private String en;
    private String describe;

    TransferRecordBizTypeEnum(Integer code, String en, String describe) {
        this.code = code;
        this.describe = describe;
        this.en = en;
    }

    public static String getDescByCode(Integer code) {
        for (TransferRecordBizTypeEnum menuTypeEnum : values()) {
            if (menuTypeEnum.getCode().equals(code)) {
                return menuTypeEnum.getDescribe();
            }
        }
        return null;
    }

    public static String getEnByCode(Integer code) {
        for (TransferRecordBizTypeEnum menuTypeEnum : values()) {
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
