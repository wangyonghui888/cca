package com.panda.sport.merchant.api.service.impl;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.sport.bss.mapper.MerchantConfigMapper;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service("CacheService")
public class CacheService {

    public static Cache<String, Object> merchantMap = null;
    public static Cache<String, String> systemMap = null;

    public static Cache<String, Object> imageMap = null;

    public static Cache<String, Integer> betCountMap = null;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    public RedisTemp redisTemp;
    @Autowired
    private MerchantConfigMapper merchantConfigMapper;
    public static final String DOMAIN = "domain";

    public static Cache<String, List<String>> domainMap = null;

    @PostConstruct
    private void init() {
        merchantMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        systemMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        setMerchantConfigCache();

        imageMap = Caffeine.newBuilder().maximumSize(500) // 设置缓存的最大容量
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats().build();


        betCountMap = Caffeine.newBuilder().maximumSize(100000) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.HOURS)
                .recordStats().build();


        domainMap = Caffeine.newBuilder()
                .maximumSize(5000) // 设置缓存的最大容量
                .expireAfterWrite(30000, TimeUnit.MILLISECONDS)
                .recordStats() // 开启缓存统计
                .build();


    }

/*    private void setImageCache() {
        try {

        }catch ()
    }*/

    private void setMerchantConfigCache() {
        try {
            List<MerchantConfig> merchantConfigList = merchantConfigMapper.queryConfigList();
            if (CollectionUtils.isNotEmpty(merchantConfigList)) {
                for (MerchantConfig merchantConfig : merchantConfigList) {
                    merchantMap.put(merchantConfig.getMerchantCode(), merchantConfig);
                }
            }
            log.info("商户配置加载结束!total=" + (CollectionUtils.isNotEmpty(merchantConfigList) ? merchantConfigList.size() : 0));
        } catch (Exception e) {
            log.error("初始化商户配置缓存结束!", e);
        }
    }

    public Response getLocalCacheInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("merchantMap", merchantMap);
        return Response.returnSuccess(result);
    }

    public Response invalidateCache(String key) {
        merchantMap.invalidate(key);
        return Response.returnSuccess();
    }

    public Response invalidateAll() {
        merchantMap.invalidateAll();
        return Response.returnSuccess();
    }


    public MerchantConfig getMerchantCache(String merchantCode) {
        MerchantConfig config = (MerchantConfig) merchantMap.getIfPresent(merchantCode);
        if (config == null) {
            this.setMerchantConfigCache();
            config = (MerchantConfig) merchantMap.getIfPresent(merchantCode);
        }
        return config;
    }

    public UserPO getUserCheckBalanceCache(String merchantCode, String userName) {
        String key = merchantCode + userName;
        try {
            UserPO userPO =  (UserPO) redisTemp.getObject(key, UserPO.class);
            log.info("redis缓存获取用户:" + userPO);
            if (userPO == null) {
                userPO = userMapper.getUserByUserName(merchantCode, userName);
                log.info("database查询数据库获取用户:" + userPO);
                if (userPO != null) {
                    redisTemp.setObject(key, userPO, 3600 * 72);
                }
            }
            return userPO;
        } catch (Exception e) {
            log.error(key + "getUserCheckBalanceCache:", e);
            return null;
        }
    }

    public String getSystemConfig(String redisKey) {
        String systemConfig = systemMap.getIfPresent(redisKey);
        if (StringUtils.isEmpty(systemConfig)) {
            systemConfig = redisTemp.get(redisKey);
            if (StringUtils.isNotEmpty(systemConfig)) {
                systemMap.put(redisKey, systemConfig);
            }
        }
        if (StringUtils.isEmpty(systemConfig)) {
            systemConfig = merchantConfigMapper.getSystemConfig("tagMarketLevelStatus");
            log.info("database.getSystemConfig=" + systemConfig);
            if (StringUtils.isNotEmpty(systemConfig)) {
                redisTemp.setKey(redisKey, systemConfig, 3600);
            }
        }
        return systemConfig;
    }
}
