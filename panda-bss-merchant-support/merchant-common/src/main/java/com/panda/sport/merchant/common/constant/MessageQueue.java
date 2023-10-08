package com.panda.sport.merchant.common.constant;

/**
 * @author :  christion
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.spi.webscolet
 * @Description :  TODO
 * @Date: 2019-10-11 17:33
 */
public class MessageQueue {
    //盘中赛事赛果队列
    public static final String STANDARD_MATCH_RESULT = "STANDARD_MATCH_RESULT";
    public static final String STANDARD_MATCH_RESULT_GROUP = "osmc_match_result_group";

    public static final String ORDER_INFO_GROUP = "osmc_order_info_group";
    public static final String ORDER_INFO = "que_order_bet";

    //盘中赛事赛果消费组
    public static final String OSMC_ORDER_SETTLE_GROUP="OSMC_ORDER_SETTLE_GROUP";

    // 盘中事件队列
    public static final String MATCH_EVENT_INFO = "MATCH_EVENT_INFO";
    public static final String MATCH_EVENT_INFO_GROUP = "OSMC_match_event_info_group";
    //标准盘口及投注项队列
    public static final String STANDARD_MARKET_ODDS_TOPIC = "STANDARD_MARKET_ODDS";
    public static final String STANDARD_MARKET_ODDS_GROUP = "OSMC_MARKET_ODDS_GROUP";

    //标准赛事状态
    public static final String STANDARD_MATCH_STATUS = "STANDARD_MATCH_STATUS";
    public static final String STANDARD_MATCH_STATUS_GROUP = "OSMC_standard_match_status_group";


}
