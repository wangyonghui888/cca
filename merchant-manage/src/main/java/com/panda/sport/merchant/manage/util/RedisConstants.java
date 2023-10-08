package com.panda.sport.merchant.manage.util;

/**
 * 定义服务缓存模块常量
 */
public class RedisConstants {

    public static final String HOT_PLAY_NAME ="playList";//玩法列表

    public static final String USER_ID_KEY_PREFIX = "id:";//用户Id

    public static final String MERCHANT_FAMILY = "merchant:";
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
     * 创建family key:
     */
    public static final String CREAT_FAMILY_KEY = "creat:";

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

    public static final String MERCHANT_CHAT = "merchant:switch:";


    public static final String BSS_DICT_CACHE = "bss_dict_cache";

    public static final Integer EXPIRE_TIME_FIVE_MIN = 300;  //过期时间300秒 5分钟

    public static final Integer EXPIRE_TIME_TOW_HOUR = 7200;  //过期时间2小时

    public static final Integer EXPIRE_TIME_ONE_HOUR = 3600;  //过期时间1小时
    public static final Integer EXPIRE_TIME_HALF_HOUR = 1800;  //过期时间1小时
    public static final Integer EXPIRE_TIME_ONE_MONTH = 3600*24*30;  //过期时间1个月


    public static final String MERCHANT_CODE_PREFIX = "merchantCode:";//用户Id
    public static final String MERCHANT_REPORT_PREFIX = "merchantReport:";//用户Id

    /**
     * 赛事文章模块
     */
    public static final String ARTICLE_ARTICLE_ONLINE = "article:online:";//上线文章缓存key
    public static final String ARTICLE_DELAY_QUEUE_INIT = "article:delay:queue:init";//节点加载数据lock key
    public static final String ARTICLE_DELAY_QUEUE_CONSUMER = "article:delay:queue:consumer:";//延迟队列消费key
    public final static String MATCH_ARTICLE_ID = "match:article:id"; //s_article表、t_article表主键

    /**
     * 商户配置缓存
     */
    public static final String merchant_default_video_domain = "merchant_defaultVideoDomain";//商户默认视频域名配置

    public static final String MERCHANT_DOMAIN = "frontend:merchantDomain:";//前端域名缓存key
}
