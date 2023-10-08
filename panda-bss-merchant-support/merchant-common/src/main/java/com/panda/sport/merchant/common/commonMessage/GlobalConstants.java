package com.panda.sport.merchant.common.commonMessage;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.commonMessage
 * @Description: 公共常量定义
 * @Date: 2019/10/5 19:21
 * @Version: 1.0
 */
public class GlobalConstants {

    //队列头前缀
    public static final String QUE = "que_";
    //结算队列【用于结算时异步发给风控】
    public static final String QUE_SETTLE_MONEY = QUE + "settle_accounts";
    //用户信息前缀
    public static final String REDIS_USER_INFO = "user_info";
    //系统用户定义
    public static final String SYSTEM_USER = "系统";
    //用户下注
    public static final String USER_BET = "用户下注";
    public static final String RCS_BET = "风控操作订单状态";
    //redis唯一id生成，定义key
    public static final String ORDER_UUID = "order:uuid";
    //redis唯一id生成，定义注单key
    public static final String ORDER_DETAIL_UUID = "order_detail:uuid";


    //投注消息队列
    public static final String ORDER_BET = QUE + "order_bet";
    //接受注单结算消息队列
    public static final String OSMC_BET_NO_SETTLE_RESULT = "OSMC_BET_NO_SETTLE_RESULT";
    //接受注单结算消息队列
    public static final String OSMC_BET_NO_SETTLE_RESULT_GROUP = OSMC_BET_NO_SETTLE_RESULT + "_GROUP";
    //投注定单结算消息队列
    public static final String OSMC_ORDER_SETTLE_RESULT = "OSMC_SETTLE_RESULT";
    //投注定单结算消息队列
    public static final String OSMC_ORDER_SETTLE_RESULT_GROUP = OSMC_ORDER_SETTLE_RESULT + "_GROUP";
    //串关对象key
    public static final String SERIES_KEY = "A,B,C,D,E,F,G,H,I,J,K,M,L";
    //结算中心生成的盘口结果队列
    public static final String OSMC_MARKET_RESULT = "OSMC_MARKET_RESULT";
    //结算中心生成的盘口结果队列(本地测试)
    public static final String OSMC_MARKET_RESULT_TEST = "OSMC_MARKET_RESULT_TEST";
    //结算中心生成的盘口结果队列消费组
    public static final String OSMC_MARKET_RESULT_GROUP = OSMC_MARKET_RESULT + "_GROUP";
    //用户注册给风控发送消息
    public static final String PANDA_RCS_RPC_USER = "panda_rcs_rpc_user";
    //风控发送注单状态消息
    public static final String PANDA_RCS_QUEUE_MTS_ORDER= "queue_mts_order";
    //风控发送注单状态消息tag
    public static final String PANDA_RCS_QUEUE_MTS_ORDER__GROUP= PANDA_RCS_QUEUE_MTS_ORDER + "_GROUP";
    //业务发送注单状态消息->push给前端
    public static final String PANDA_BUS_ORDER_STATUS= "BUS_ORDER_STATUS";
}
