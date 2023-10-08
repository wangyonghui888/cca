package com.panda.sport.merchant.common.constant;

/**
 * @author :  sklee
 * @Project Name :
 * @Package Name :
 * @Description : 消息队列常量类
 * @Date: 2019-10-22 13:11
 */
public class QueueTopicConstant {

    /**
     * 结算中心生成的盘口结果队列
     */
    public static final String OSMC_MARKET_RESULT = "OSMC_MARKET_RESULT";

    /**
     * 结算中心生成的注单结算结果队列
     */
    public static final String OSMC_BET_NO_SETTLE_RESULT = "OSMC_BET_NO_SETTLE_RESULT";

    /**
     * 结算中心生成的订单结算结果队列
     */
    public static final String OSMC_ORDER_SETTLE_RESULT = "OSMC_SETTLE_RESULT";

}
