package com.panda.sport.mqsdk.mq.base;


import com.panda.sport.mqsdk.annotation.RocketMQMessageListener;
import com.panda.sport.mqsdk.mq.config.PropertyKeyConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Properties;

/**
 * @Description:
 * @Author: valar so handsome
 */

@Slf4j
public class MqConsumer2 implements BeanPostProcessor {

    private Properties properties;


    public MqConsumer2(Properties properties) throws Exception {
        if (properties == null || properties.get(PropertyKeyConst.ConsumerId) == null
/*                || properties.get(PropertyKeyConst.AccessKey) == null
                || properties.get(PropertyKeyConst.SecretKey) == null*/
                || properties.get(PropertyKeyConst.Topic) == null
                || properties.get(PropertyKeyConst.ONSAddr) == null) {
            throw new Exception("consumer properties not set properly.");
        }
        this.properties = properties;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = AopUtils.getTargetClass(bean);
        RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);
        if (null != annotation) {
            @SuppressWarnings("rawtypes")
            BasePandaRocketMQListener listener = (BasePandaRocketMQListener) bean;
            log.info("MqConsumer2 Consumer start ");
            try {
                DefaultMQPushConsumer consumer = assemblyConsumer(properties);
                //在此监听中消费信息，并返回消费的状态信息
                consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                    //所以子类调用(建议只被继承一次)
                    try {
                        listener.consumerExecute(msgs);
                    } catch (Exception e) {
                        log.error("MqConsumer2 Consumer get msg error:", e);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                });
                consumer.start();
                log.info("MqConsumer2 Consumer end");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    private DefaultMQPushConsumer assemblyConsumer(Properties properties) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer((String) properties.get(PropertyKeyConst.ConsumerId));
        consumer.setNamesrvAddr((String) properties.get(PropertyKeyConst.ONSAddr));
        // 订阅PushTopic下Tag为push的消息,都订阅消息
        consumer.subscribe((String) properties.get(PropertyKeyConst.Topic), "*");
        consumer.setInstanceName("consumer");
        // 程序第一次启动从消息队列头获取数据
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //可以修改每次消费消息的数量，默认设置是每次消费一条
        consumer.setConsumeMessageBatchMaxSize(100);
        String messageModel = (String) properties.get(PropertyKeyConst.MessageModel);
        if (StringUtils.isNotBlank(messageModel)) {
            if (MessageModel.BROADCASTING.getModeCN().equals(messageModel)) {
                consumer.setMessageModel(MessageModel.BROADCASTING);
            } else if (MessageModel.CLUSTERING.getModeCN().equals(messageModel)) {
                consumer.setMessageModel(MessageModel.CLUSTERING);
            }
        }
        String threadNums = (String) properties.get(PropertyKeyConst.ConsumeThreadNums);
        if (StringUtils.isNotBlank(threadNums)) {
            consumer.setConsumeThreadMax(Integer.valueOf(threadNums));
        }
        String reconsumeTimes = (String) properties.get(PropertyKeyConst.MaxReconsumeTimes);
        if (StringUtils.isNotBlank(threadNums)) {
            consumer.setMaxReconsumeTimes(Integer.valueOf(reconsumeTimes));
        }
        String timeout = (String) properties.get(PropertyKeyConst.ConsumeTimeout);
        if (StringUtils.isNotBlank(timeout)) {
            consumer.setConsumeTimeout(Integer.valueOf(timeout));
        }
        return consumer;
    }
}
