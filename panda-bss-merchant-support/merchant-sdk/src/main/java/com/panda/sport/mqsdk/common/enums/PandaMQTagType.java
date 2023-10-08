package com.panda.sport.mqsdk.common.enums;

/**
 * @author : Jeffrey
 * @Date: 2020-01-06 16:24
 * @Description : 消费者tag类型
 */
public enum PandaMQTagType {
    BET_TAG("bet", "注单"),
    SETTLE_TAG("settle", "结算"),
    CANCEL_BET_TAG("cancelBet", "撤单");

    private String name;
    private String val;

    PandaMQTagType(String val, String name) {
        this.name = name;
        this.val = val;
    }
}
