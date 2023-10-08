package com.panda.sport.mqsdk.mq.base;


import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消费监听基类
 *
 * @author : Jeffrey
 * @Date: 2019-12-26 14:47
 * @Description :
 */
public abstract class BasePandaRocketMQListener {

    public abstract void consumerExecute(List<MessageExt> msg) throws Exception;

}
