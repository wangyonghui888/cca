package com.panda.multiterminalinteractivecenter.service;

/**
 * @author :  ifan
 * @Description :  发送mongo消息
 * @Date: 2022-07-4
 * --------  ---------  --------------------------
 */
public interface IMongoService {

    /**
     * 发送mongo消息 针对群组发送文本消息
     *
     * @param text       文本内容
     * @param targetName 目标群id
     */

    void send(String text, String targetName, int sendMongoSwitch, String userId, String userToken);

    void send(String text );
}
