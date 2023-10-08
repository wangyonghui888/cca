package com.panda.sport.admin.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.LRUMap;

@Slf4j
public class IdempotentUtils {

    private static LRUMap<String, Integer> reqCache = new LRUMap<>(100);

    /**
     * 幂等性判断
     *
     * @return
     */
    public static boolean repeat(String id, Object lockClass) {
        synchronized (lockClass) {
            if (reqCache.containsKey(id)) {
                // 重复请求
                log.warn("请勿重复提交!!!!!!!!" + id);
                return true;
            }
            // 非重复请求，存储请求 ID
            reqCache.put(id, 1);
        }
        return false;
    }

    public static void remove(String id, Object lockClass) {
        synchronized (lockClass) {
            reqCache.remove(id);
        }
    }
}
