package com.panda.sport.merchant.common.constant;

/**
 * @author :  ives
 * @Description :  TODO
 * @Date: 2022-05-11 16:31
 */
public class RedisKeyNameConstant {

    public static final String BIG_DATA_USER_DYNAMICS_RISK_CONTROL_STATISTICS = "bigDataUserDynamicsRiskControlStatistics:";

    public static final String USER_LEVEL_RELATION_STATISTICS = "userLevelRelationStatistics:";

    /**
     * 赔率分组动态风控开关-系统配置
     */
    public static final String SystemControlConfig_Dynamic_MarketGroup = "SystemControlConfig_Dynamic_MarketGroup";

    /**
     * 赔率分组动态风控开关-商户配置
     */
    public static final String SystemControlConfig_Dynamic_Merchant_MarketGroup = "SystemControlConfig_Dynamic_Merchant_MarketGroup";

    /**
     * 赔率分组动态风控开关-用户配置
     */
    public static final String SystemControlConfig_Dynamic_User_MarketGroup = "SystemControlConfig_Dynamic_User_MarketGroup";

    /**
     * 用户更新时间Key
     */
    public static final String  REDIS_HISTORY_UNIQUE_ID  = "Redis_History_Unique_Id:" ;

    /**
     * 全局币种列表Key(HASH)
     */
    public static final String  SYSTEM_CURRENCY_LIST_KEY  = "system_currency:" ;

}
