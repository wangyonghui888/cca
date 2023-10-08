package com.panda.sport.merchant.common.enums;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.sport.merchant.common.utils.StringUtil;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 * */
public enum MerchantLocalCacheEnum {
    //MERCHANT_LOCAL_CACHE_+接口名称大写
    MERCHANT_LOCAL_CACHE_GET_LIGHT_NEWS("MERCHANT_LOCAL_CACHE_GET_LIGHT_NEWS",60, 30, TimeUnit.MINUTES)

    ;

    MerchantLocalCacheEnum(String key, int maximumSize, int duration, TimeUnit unit) {
        this.key = key;
        this.maximumSize = maximumSize;
        this.duration = duration;
        this.unit = unit;
    }

    private String key;
    private int maximumSize;

    private int duration;

    private TimeUnit unit;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public static MerchantLocalCacheEnum getMerchantLocalCacheUnit(String key) {
        if(StringUtil.isNotBlank(key)){
            return null;
        }

        for (MerchantLocalCacheEnum springCacheEnum : MerchantLocalCacheEnum.values()) {
            if (key.equals(springCacheEnum.getKey())) {
                return springCacheEnum;
            }
        }
        return null;
    }

    //获取本地缓存
    public static Cache<?, ?> getLocalCache(MerchantLocalCacheEnum springCacheEnum){
        return Caffeine.newBuilder()
                .maximumSize(springCacheEnum.getMaximumSize())
                .expireAfterWrite(springCacheEnum.getDuration(),springCacheEnum.getUnit())
                .recordStats()
                .build();
    }
}
