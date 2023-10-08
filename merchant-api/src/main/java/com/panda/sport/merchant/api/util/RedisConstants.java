package com.panda.sport.merchant.api.util;

/**
 * 定义服务缓存模块常量
 */
public class RedisConstants {
    /**
     * 用户中心缓存family key
     */
    public static final String USERCENTER_FAMILY_KEY = "usercenter:";

    /**
     * 禁用用户 key
     */
    public static final String MULTITERMINAL_USER_DISABLE_KEY = "disabled:";
    /**
     * 用户登录次数
     */
    public static final String LOGIN_TIMES = "loginTimes:";

    public static final String CACHE = "cache:";

    public static final String Colon = ":";

    public static final String USER_TOKEN_PREFIX = "token:";//用户Token

    public static final String USER_S_TOKEN_PREFIX = "stoken:";//用户Token

    public static final String HOT_PLAY_NAME = "playList";//玩法列表

    public static final String USER_ID_KEY_PREFIX = "id:";//用户Id

    public static final String USER_NAME_KEY_PREFIX = "userName:";//用户Id

    public static final String MERCHANT_FAMILY = "merchant:";

    public static final String BUSSINESS = "bet:merchantCode:";

    public static final String USER_BUSSINESS = "userCenter:merchantCode:";

    public static final String IN_ACTIVITY = "inactivity:";

    public static final String MAINTAIN_TIME = "maintainTime:";
    /**
     * 赛程缓存family key
     */
    public static final String SCHEDULE_FAMILY_KEY = "schedule:";
    /**
     * 订单缓存family key:
     */
    public static final String ORDER_FAMILY_KEY = "order:";
    /**
     * 支付缓存family key:
     */
    public static final String PAY_FAMILY_KEY = "pay:";

    /**
     * 支付重试缓存family key:
     */
    public static final String RETRY_FAMILY_KEY = "retry:";
    /**
     * 创建family key:
     */
    public static final String CREAT_FAMILY_KEY = "creat_user:";
    /**
     * 派彩缓存family key
     */
    public static final String LOTTERY_FAMILY_KEY = "lottery:";
    /**
     * job缓存 family key
     */
    public static final String JOB_FAMILY_KEY = "job:";
    /**
     * 结算缓存 family key
     */
    public static final String CLEARING_FAMILY_KEY = "clearing:";
    /**
     * match缓存 family key
     */
    public static final String MATCH_KEY = "match:";
    /**
     * 三端域名缓存 family key
     */
    public static final String MULTITERMINAL_DOMAIN_KEY = "multiterminal:domain:";
    /**
     * 三端域名缓存 family key
     */
    public static final String MULTITERMINAL_VIP_DOMAIN_KEY = "multiterminal:vip:domain:";

    /**
     * 商户后台聊天室消息拉取频率key
     */
    public static final String MERCHANT_CHAT = "merchant:switch:";


    public static final String BSS_DICT_CACHE = "bss_dict_cache";

    public static final Integer EXPIRE_TIME_FIVE_MIN = 300;  //过期时间300秒 5分钟

    public static final Integer EXPIRE_TIME_TEN_MIN = 600;  //过期时间300秒 5分钟

    public static final Integer EXPIRE_TIME_ONE_MIN = 60;  //过期时间300秒 1分钟

    public static final Integer EXPIRE_TIME_TWO_MIN = 120;  //过期时间300秒 1分钟

    public static final Integer EXPIRE_TIME_TOW_HOUR = 7200;  //过期时间2小时

    public static final Integer EXPIRE_TIME_ONE_HOUR = 3600;  //过期时间1小时

    public static final Integer EXPIRE_TIME_TWELVE_HOUR = 3600 * 12;  //过期时间12小时

    public static final Integer EXPIRE_TIME_THIRTEEN_HOUR = 3600 * 13;  //过期时间13小时

    public static final Integer EXPIRE_TIME_ONE_WEEK = 3600 * 24 * 7;  //过期时间7天

    public static final Integer EXPIRE_TIME_TWO_WEEK = 3600 * 24 * 14;  //过期时间14天

    public static final Integer EXPIRE_TIME_HALF_HOUR = 1800;  //过期时间1小时

    public static final Integer EXPIRE_TIME_ONE_MONTH = 3600 * 24 * 30;  //过期时间1个月


    public static final String MERCHANT_CODE_PREFIX = "merchantCode:";//用户Id

    public static final String MERCHANT_REPORT_PREFIX = "merchantReport:";//用户Id

    public static final String BALANCE = "balance:";

    public static final String TRANSFER_ID_PRE = "transferId:";

    /**
     * 前端h5域名
     */
    public static final String H5_DOMAIN = "h5_domain";

    /**
     * 前端pc域名
     */
    public static final String PC_DOMAIN = "pc_domain";
}
