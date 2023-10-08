package com.oubao.service.impl;

/**
 * @author :  kiu
 * @version: V1.1.0
 * @Project Name :panda-bss
 * @Package Name :com.panda.sports.bss.lottery.controller
 * @Description : 赛程 模块redis 配置key
 * @since: 2019-10-16 17:32
 */
public class UsercenterRedisConstant {

    public static final Integer EXPIRE_TIME_FIVE_MIN = 300;  //过期时间300秒 5分钟

    public static final Integer EXPIRE_TIME_TOW_HOUR = 7200;  //过期时间2小时

    public static final Integer EXPIRE_TIME_ONE_HOUR = 3600;  //过期时间1小时

    public static final String USER_TOKEN_KEY_PREFIX =  "USER_TOKEN_KEY";//用户Token

}
