package com.panda.sport.mqsdk.mq;

import com.panda.sport.mqsdk.mq.base.BasePandaRocketConsumer;
import com.panda.sport.mqsdk.mq.base.BasePandaRocketMQListener;
import com.panda.sport.mqsdk.mq.config.PandaRocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.context.ApplicationContext;

/**
 * @Description: spring boot模式 与 非spring 模式
 * @Author: valar so handsome
 * @Date: 2020/2/4
 */
@Slf4j
public class DefaultPandaRocketConsumer extends BasePandaRocketConsumer {
    @Override
    public void messageListener(PandaRocketMQConfig config, ApplicationContext applicationContext) {
        log.info("DefaultPandaRocketConsumer Consumer Start ");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(config.getGroupName(), getAclRPCHook(config), new AllocateMessageQueueAveragely());
        try {
            String[] beanNames = applicationContext.getBeanNamesForType(BasePandaRocketMQListener.class);
            if (beanNames.length == 0) {
                log.warn("ERROR!初始化消费者异常!");
            }
            consumer.setNamesrvAddr(config.getNamesrvAddr());
            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumer.subscribe(config.getTopic(), "*");
            consumer.setInstanceName("panda-sport-consumer");
            // 程序第一次启动从消息队列头获取数据
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费100条
            consumer.setConsumeMessageBatchMaxSize(100);

            consumer.setMessageModel(MessageModel.CLUSTERING);
            //在此监听中消费信息，并返回消费的状态信息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                //所以子类调用(建议只被继承一次)
                for (String beanName : beanNames) {
                    try {
                        BasePandaRocketMQListener mqListener = (BasePandaRocketMQListener) applicationContext.getBean(beanName);
                        mqListener.consumerExecute(msgs);
                    } catch (Exception e) {
                        log.error("DefaultPandaRocketConsumer Consumer get msg error:", e);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                // 会把不同的消息分别放置到不同的队列中
            });
            consumer.start();
            log.info("DefaultPandaRocketConsumer Consumer End");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static RPCHook getAclRPCHook(PandaRocketMQConfig config) {
        return new AclClientRPCHook(new SessionCredentials(config.getAccessKey(), config.getSecretKey()));
    }

}