package com.panda.sport.merchant.api.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisTemp {

    @Autowired
//    @Qualifier("persistentRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    /* 锁的key前缀 */
    private static final String LOCK_PREFIX = "LOCK_";

    /* 是否获得锁的标志 */
    private boolean lock = false;
    /* 锁的有效时间(s) */
    private final int LOCK_EXPIRSE_TIME = 3;

    private final String GLOBAL_KEY = "SEQ1";


    /**
     * 设置缓存
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        log.info("RedisUtil:set cache key={},value={}", key, value);
    }

    /**
     * 设置缓存对象
     *
     * @param key        缓存key
     * @param obj        缓存value
     * @param expireTime 过期时间:second
     */

    public <T> void setObject(String key, T obj, int expireTime) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), expireTime, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存对象
     *
     * @param key 缓存key
     * @param obj 缓存value
     */

    public <T> void setObject(String key, T obj) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
    }

    /**
     * 设置缓存对象
     *
     * @param key        缓存key
     * @param obj        缓存value
     * @param expireTime 过期时间:second
     */

    public <T> void setObject(String key, T obj, int expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), expireTime, timeUnit);
    }


    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 设置系统缓存，并且自己指定过期时间
     *
     * @param key
     * @param obj
     * @param expireTime 过期时间
     * @Param familyKey
     */
    public <T> void setObject(String familyKey, String key, T obj, int expireTime) {
        setObject(familyKey + key, JSON.toJSONString(obj), expireTime);
    }

    /**
     * 获取指定key的缓存
     *
     * @param key---JSON.parseObject(value, User.class);
     */
    public Object getObject(String key, Class clazz) {
        String value = redisTemplate.opsForValue().get(key);
        return StringUtils.isEmpty(value) ? null : JSON.parseObject(value, clazz);
    }

    public Object getListObject(String key, Class clazz) {
        String value = redisTemplate.opsForValue().get(key);
        log.info("getListObject.value=" + value);

        return StringUtils.isEmpty(value) ? null : JSONArray.parseObject(value, clazz);
    }

    public <T> void setListObject(String key, T obj, int expireTime, TimeUnit timeUnit) {
        String jsonArray = JSONArray.toJSONString(obj);
        redisTemplate.opsForValue().set(key, jsonArray, expireTime, timeUnit);
    }

    /**
     * 判断当前key值 是否存在
     *
     * @param key
     */
    public boolean hasKey(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return redisTemplate.hasKey(key);
        }
        return false;
    }

    public String getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> void setKey(String key, String obj, int expireTime) {
        redisTemplate.opsForValue().set(key, obj, expireTime, TimeUnit.SECONDS);

    }

    /**
     * 有争议:是否使用setIfPresent方法
     * 设置缓存，并且自己指定过期时间
     * 原jedis为setex方法:创建或替换旧值,并设置过期时间
     *
     * @param key
     * @param value
     * @param expireTime 过期时间
     */
    public void setWithExpireTime1(String key, String value, int expireTime) {
//        this.jedisCluster().setex(key, expireTime, value);
        redisTemplate.opsForValue().setIfPresent(key, value, Duration.ofMinutes((long) expireTime));
        log.info("RedisUtil:setWithExpireTime cache key={},value={},expireTime={}", key, value, expireTime);
    }

    public void setWithExpireTime(String key, String value, int expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 设置系统缓存，并且自己指定过期时间
     *
     * @param key
     * @param value
     * @param expireTime 过期时间
     * @Param familyKey
     */
    public void setWithExpireTime(String familyKey, String key, String value, int expireTime) {
        setWithExpireTime(familyKey + key, value, expireTime);
    }

    /**
     * 获取指定key的缓存
     *
     * @param key
     */
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        log.info("RedisUtil:get cache key={},value={}", key, value);
        return value;
    }

    /**
     * 获取指定系统key的缓存
     *
     * @param familyKey
     * @param key
     */
    public String get(String familyKey, String key) {
        return get(familyKey + key);
    }

    /**
     * 删除指定key的缓存
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
        log.info("RedisUtil:delete cache key={}", key);
    }

    public void deleteKeys(Set<String> key) {
        redisTemplate.delete(key);
        log.info("RedisUtil:delete cache key={}", key);
    }

    /**
     * pash Map数据
     *
     * @param key
     * @param putTempMap
     */
    public void pashMap(String key, Map<String, String> putTempMap) {
        try {
            redisTemplate.opsForHash().putAll(key, putTempMap);
        } catch (Exception e) {
            log.error("redis异常，生成redisUUID失败！");
        }
    }


    /**
     * 获取指定 key 的值
     *
     * @param key
     * @param type
     * @return
     */
    public <T> T getObject(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }


    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key
     * @param unit
     * @return
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    public Set<String> keys(String keys) {
        return redisTemplate.keys(keys);
    }

    public <T> T execute(RedisScript<T> redisScript, String apiKey, String sec) {
        T obj = (T) redisTemplate.execute(redisScript, Collections.singletonList(apiKey), sec);
        return obj;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long lPush(String key, String value){
        Long count = 0L;
        try {
            count = redisTemplate.opsForList().leftPush(key, value);
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return count;
        }
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }
}