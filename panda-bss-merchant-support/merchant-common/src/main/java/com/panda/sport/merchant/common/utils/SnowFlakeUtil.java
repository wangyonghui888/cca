package com.panda.sport.merchant.common.utils;

import cn.hutool.core.lang.Snowflake;


public class SnowFlakeUtil {

    //TODO 每台服务实例workeId和datacenterId不能一样，后期优化
    //workeId <= 31  datacenterId<=31
    private static Snowflake snowflake = new Snowflake(1, 1);

    public static Long getId() {
        return snowflake.nextId();
    }

}
