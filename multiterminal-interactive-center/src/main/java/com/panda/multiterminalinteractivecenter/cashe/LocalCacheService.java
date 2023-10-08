package com.panda.multiterminalinteractivecenter.cashe;


import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.multiterminalinteractivecenter.entity.DomainArea;
import com.panda.multiterminalinteractivecenter.entity.Oss;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.mapper.DomainAreaMapper;
import com.panda.multiterminalinteractivecenter.mapper.DomainMapper;
import com.panda.multiterminalinteractivecenter.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service("LocalCacheService")
public class LocalCacheService {
    @Autowired
    private DomainAreaMapper domainAreaMapper;

    /**
     * 登录用户信息的本地缓存
     */
    public static Cache<String, LoginUserVo> userCacheMap = null;

    public static Cache<Long, DomainArea> areaMap = null;

    public static Cache<String, String> DOMAIN_MAP = null;

    public static Cache<String, Object> DOMAIN_COUNT_MAP = null;

    public static Cache<String, Oss> OSS_DOMAIN_MAP = null;

    public static Cache<String, JSONObject> OSS_JSON_OBJECT_MAP = null;

    public static Cache<String, List<String>> OSS_NEW_DOMAIN_MAP = null;

    public static Cache<String, Oss> FAILED_17CE_OSS_DOMAIN_MAP = null;

    public static Cache<String, JSONObject>  NEW_JSON_OBJECT  = null;

    @PostConstruct
    private void init() {
        userCacheMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        areaMap = Caffeine.newBuilder()
                .maximumSize(200) // 设置缓存的最大容量
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        DOMAIN_MAP = Caffeine.newBuilder()
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        DOMAIN_COUNT_MAP = Caffeine.newBuilder()
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        OSS_DOMAIN_MAP = Caffeine.newBuilder()
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        OSS_JSON_OBJECT_MAP = Caffeine.newBuilder()
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        OSS_NEW_DOMAIN_MAP = Caffeine.newBuilder()
                .maximumSize(500) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        FAILED_17CE_OSS_DOMAIN_MAP = Caffeine.newBuilder()
                .maximumSize(500) // 设置缓存的最大容量
                .expireAfterWrite(50, TimeUnit.SECONDS)
                .recordStats() // 开启缓存统计
                .build();


        NEW_JSON_OBJECT = Caffeine.newBuilder()
                .maximumSize(500) // 设置缓存的最大容量
                .expireAfterWrite(50, TimeUnit.SECONDS)
                .recordStats() // 开启缓存统计
                .build();

        List<DomainArea> domainAreaList = domainAreaMapper.selectList(null);
        domainAreaList.forEach(domainArea -> areaMap.put(domainArea.getId(), domainArea));
    }

    public DomainArea getDomainArea(Long areaId) {
        DomainArea  domainArea = LocalCacheService.areaMap.getIfPresent(areaId);
        if(Objects.isNull(domainArea)){
            return null;
        }
        return domainArea;
    }
}
