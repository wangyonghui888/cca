package com.panda.sport.merchant.api.util;

import cn.hutool.core.util.RandomUtil;

public class MerchantUtils {

    /**
     * 缓存模块失效时间设置
     *
     * @return
     */
    public static Integer getExpireNumber() {
        return getExpireNumber(12, 24);
    }
    public static Integer getExpireNumWeek() {
        return getExpireNumber(168, 192);
    }
    /**
     * 缓存模块失效时间设置
     *
     * @return
     */
    public static Integer getExpireNumber(int min,int max) {
        return RandomUtil.randomInt(min, max);
    }


}
