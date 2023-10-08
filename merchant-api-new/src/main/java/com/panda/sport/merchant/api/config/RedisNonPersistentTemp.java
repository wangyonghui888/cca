package com.panda.sport.merchant.api.config;//package com.panda.sport.merchant.api.config;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.panda.sport.merchant.api.util.MerchantUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.Cursor;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ScanOptions;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class RedisNonPersistentTemp {
//	 	@Autowired
//	 	@Qualifier("nonPersistentRedisTemplate")
//	 	private RedisTemplate<String, String> redisTemplate;
//
//	    /**
//	     * 设置缓存
//	     *
//	     * @param key   缓存key
//	     * @param value 缓存value
//	     */
//	    public void set(String key, Object value) {
//	        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
//	        log.debug("RedisUtil:set cache key={},value={}", key, value);
//	    }
//
//	    /**
//	     * 设置缓存对象
//	     *
//	     * @param key        缓存key
//	     * @param obj        缓存value
//	     * @param expireTime 过期时间:second
//	     */
//	    public <T> void setObject(String key, T obj, int expireTime) {
//	        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), expireTime, TimeUnit.SECONDS);
//	    }
//
//	    public <T> void setListObject(String key, T obj, int expireTime) {
//	        redisTemplate.opsForValue().set(key, JSONArray.toJSONString(obj), expireTime, TimeUnit.SECONDS);
//	    }
//
//
//	    /**
//	     * 设置缓存对象
//	     *
//	     * @param key 缓存key
//	     * @param obj 缓存value
//	     */
//	    public <T> void setObject(String key, T obj) {
//	        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
//	    }
//
//	    /**
//	     * 设置系统缓存，并且自己指定过期时间
//	     *
//	     * @param key
//	     * @param obj
//	     * @param expireTime 过期时间
//	     * @Param familyKey
//	     */
//	    public <T> void setObject(String familyKey, String key, T obj, int expireTime) {
//	        setObject(familyKey + key, JSON.toJSONString(obj), expireTime);
//	    }
//
//	    /**
//	     * 设置系统缓存，并且自己指定过期时间
//	     *
//	     * @param key
//	     * @param obj
//	     * @Param familyKey
//	     */
//	    public <T> void setObject(String familyKey, String key, T obj) {
//	        setObject(familyKey + key, JSON.toJSONString(obj));
//	    }
//
//
//	    public <T> void setKey(String key, String obj, int expireTime) {
//	        redisTemplate.opsForValue().set(key, obj, expireTime, TimeUnit.SECONDS);
//
//	    }
//
//	    public String getKey(String key) {
//	        return redisTemplate.opsForValue().get(key);
//	    }
//
//
//	    /**
//	     * 获取指定key的缓存
//	     *
//	     * @param key---JSON.parseObject(value, User.class);
//	     */
//	    public Object getObject(String key, Class clazz) {
//	        String value = redisTemplate.opsForValue().get(key);
//	        return StringUtils.isEmpty(value) ? null : JSON.parseObject(value, clazz);
//	    }
//
//	    public Object getListObject(String key, Class clazz) {
//	        String value = redisTemplate.opsForValue().get(key);
//	        log.info("getListObject.value=" + value);
//
//	        return StringUtils.isEmpty(value) ? null : JSONArray.parseObject(value, clazz);
//	    }
//
//	    /**
//	     * 判断当前key值 是否存在
//	     *
//	     * @param key
//	     */
//	    public boolean hasKey(String key) {
//	        return StringUtils.isNotEmpty(key) ? redisTemplate.hasKey(key) : false;
//	    }
//
//
//	    /**
//	     * 有争议:是否使用setIfPresent方法
//	     * 设置缓存，并且自己指定过期时间
//	     * 原jedis为setex方法:创建或替换旧值,并设置过期时间
//	     *
//	     * @param key
//	     * @param value
//	     * @param expireTime 过期时间
//	     */
//	    public void setWithExpireTime1(String key, String value, int expireTime) {
//	        redisTemplate.opsForValue().setIfPresent(key, value, Duration.ofMinutes((long) expireTime));
//	    }
//
//	    public void setWithExpireTime(String key, String value, int expireTime) {
//	        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
//	    }
//
//	    /**
//	     * 设置系统缓存，并且自己指定过期时间
//	     *
//	     * @param key
//	     * @param value
//	     * @param expireTime 过期时间
//	     * @Param familyKey
//	     */
//	    public void setWithExpireTime(String familyKey, String key, String value, int expireTime) {
//	        setWithExpireTime(familyKey + key, value, expireTime);
//	    }
//
//	    /**
//	     * 获取指定key的缓存
//	     *
//	     * @param key
//	     */
//	    public String get(String key) {
//	        return redisTemplate.opsForValue().get(key);
//	    }
//
//	    /**
//	     * 获取指定系统key的缓存
//	     *
//	     * @param familyKey
//	     * @param key
//	     */
//	    public String get(String familyKey, String key) {
//	        return get(familyKey + key);
//	    }
//
//	    /**
//	     * 删除指定key的缓存
//	     *
//	     * @param key
//	     */
//	    public void delete(String key) {
//	        redisTemplate.delete(key);
//	    }
//
//	    /**
//	     * @Author: baker
//	     * @Description: 判断指定的key是否存在
//	     * @Date: 2019/10/2 16:14
//	     * @Param: []
//	     * @Return:
//	     */
//	    public Boolean exists(String key) {
//	        redisTemplate.hasKey(key);
//	        return true;
//	    }
//
//
//	    /**
//	     * pash Map数据
//	     *
//	     * @param key
//	     * @param putTempMap
//	     */
//	    public void pashMap(String key, Map<String, String> putTempMap) {
//	        try {
//	            redisTemplate.opsForHash().putAll(key, putTempMap);
//	        } catch (Exception e) {
//	            log.error("redis异常，生成redisUUID失败！");
//	        }
//	    }
//
//	    /** -------------------hash相关操作------------------------- */
//
//	    /**
//	     * 获取存储在哈希表中指定字段的值
//	     *
//	     * @param key
//	     * @param field
//	     * @return
//	     */
//	    public <V> V hGet(String key, Object field) {
//	        return (V) redisTemplate.opsForHash().get(key, field);
//	    }
//
//	    /**
//	     * 获取所有给定字段的值
//	     *
//	     * @param key
//	     * @return
//	     */
//	    public <K, V> Map<K, V> hGetAll(String key) {
//	        return (Map<K, V>) redisTemplate.opsForHash().entries(key);
//	    }
//
//	    public <V> List<V> hGetAllPipelined(List<String> keys) {
//	        return (List<V>) redisTemplate.executePipelined(new RedisCallback() {
//	            @Override
//	            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//	                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
//	                for (Object key : keys) {
//	                    byte[] key1 = keySerializer.serialize(key);
//	                    redisConnection.hGetAll(key1);
//	                }
//	                return null;
//	            }
//	        });
//	    }
//
//	    /**
//	     * 获取所有给定字段的值
//	     *
//	     * @param key
//	     * @param fields
//	     * @return
//	     */
//	    public List<Object> hMultiGet(String key, Collection<Object> fields) {
//	        return redisTemplate.opsForHash().multiGet(key, fields);
//	    }
//
//	    public void hPut(String key, Object hashKey, Object value) {
//	        redisTemplate.opsForHash().put(key, hashKey, value);
//	    }
//
//	    public void hPutTimeOut(String key, Object hashKey, Object value) {
//	        redisTemplate.opsForHash().put(key, hashKey, value);
//	        redisTemplate.expire(key, MerchantUtils.getExpireNumber(), TimeUnit.HOURS);
//	    }
//
//	    public void hPutTimeOutWeek(String key, Object hashKey, Object value) {
//	        redisTemplate.opsForHash().put(key, hashKey, value);
//	        redisTemplate.expire(key, MerchantUtils.getExpireNumWeek(), TimeUnit.HOURS);
//	    }
//
//	    public <K, V> void hPutAll(String key, Map<K, V> maps) {
//	        redisTemplate.opsForHash().putAll(key, maps);
//	    }
//
//	    public <K, V> void hPutAllTimeOutWeek(String key, Map<K, V> maps) {
//	        redisTemplate.opsForHash().putAll(key, maps);
//	        redisTemplate.expire(key, MerchantUtils.getExpireNumWeek(), TimeUnit.HOURS);
//	    }
//
//	    /**
//	     * 仅当hashKey不存在时才设置
//	     *
//	     * @param key
//	     * @param hashKey
//	     * @param value
//	     * @return
//	     */
//	    public Boolean hPutIfAbsent(String key, String hashKey, String value) {
//	        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
//	    }
//
//	    /**
//	     * 删除一个或多个哈希表字段
//	     *
//	     * @param key
//	     * @param fields
//	     * @return
//	     */
//	    public Long hDelete(String key, Object... fields) {
//	        return redisTemplate.opsForHash().delete(key, fields);
//	    }
//
//	    /**
//	     * 查看哈希表 key 中，指定的字段是否存在
//	     *
//	     * @param key
//	     * @param field
//	     * @return
//	     */
//	    public boolean hExists(String key, String field) {
//	        return redisTemplate.opsForHash().hasKey(key, field);
//	    }
//
//	    /**
//	     * 为哈希表 key 中的指定字段的整数值加上增量 increment
//	     *
//	     * @param key
//	     * @param field
//	     * @param increment
//	     * @return
//	     */
//	    public Long hIncrBy(String key, Object field, int increment) {
//	        return redisTemplate.opsForHash().increment(key, field, increment);
//	    }
//
//	    /**
//	     * 为哈希表 key 中的指定字段的整数值加上增量 increment
//	     *
//	     * @param key
//	     * @param field
//	     * @param delta
//	     * @return
//	     */
//	    public Double hIncrByFloat(String key, Object field, double delta) {
//	        return redisTemplate.opsForHash().increment(key, field, delta);
//	    }
//
//	    /**
//	     * 获取所有哈希表中的字段
//	     *
//	     * @param key
//	     * @return
//	     */
//	    public Set<Object> hKeys(String key) {
//	        return redisTemplate.opsForHash().keys(key);
//	    }
//
//	    /**
//	     * 获取哈希表中字段的数量
//	     *
//	     * @param key
//	     * @return
//	     */
//	    public Long hSize(String key) {
//	        return redisTemplate.opsForHash().size(key);
//	    }
//
//	    /**
//	     * 获取哈希表中所有值
//	     *
//	     * @param key
//	     * @return
//	     */
//	    public List<Object> hValues(String key) {
//	        return redisTemplate.opsForHash().values(key);
//	    }
//
//	    /**
//	     * 迭代哈希表中的键值对
//	     *
//	     * @param key
//	     * @param options
//	     * @return
//	     */
//	    public Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options) {
//	        return redisTemplate.opsForHash().scan(key, options);
//	    }
//
//}