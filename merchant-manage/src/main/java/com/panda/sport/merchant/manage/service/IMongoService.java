package com.panda.sport.merchant.manage.service;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service
 * @Description :  TODO
 * @Date: 2021-08-31 19:26:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface IMongoService {

    /**
     * 发送mongo消息 针对群组发送文本消息
     * @param text 文本内容
     * @param targetname 目标群id
     */
    public void send(String text ,String targetname);

    public void send(String text ,String targetname,String userId,String userToken);

    public void send(String text );
}
