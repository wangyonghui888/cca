package com.panda.multiterminalinteractivecenter.service;

import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.entity.Oss;

/**
 *
 * @param <T>
 */
public interface WebSocketTransfer<T> {
    void sendWebSocketMessage(T t, Oss oss, String domainName, Integer domainType, String testType);

}
