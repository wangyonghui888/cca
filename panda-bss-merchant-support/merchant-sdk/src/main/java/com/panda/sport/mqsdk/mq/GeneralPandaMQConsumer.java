package com.panda.sport.mqsdk.mq;

import com.panda.sport.mqsdk.mq.base.BasePandaRocketConsumer;
import com.panda.sport.mqsdk.mq.base.BasePandaRocketMQListener;
import com.panda.sport.mqsdk.mq.config.PandaRocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.ApplicationContext;

@Slf4j
public class GeneralPandaMQConsumer extends BasePandaRocketConsumer {

    @Override
    public void messageListener(PandaRocketMQConfig config, ApplicationContext applicationContext) {
        log.info("GeneralPandaMQConsumer Consumer Start ");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(config.getGroupName());
        String[] beanNames = applicationContext.getBeanNamesForType(BasePandaRocketMQListener.class);

        if (beanNames.length == 0) {
            System.out.println("ERROR!初始化消费者异常!");
        }
        try {
            consumer.setNamesrvAddr(config.getNamesrvAddr());
            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumer.subscribe(config.getTopic(), "*");
            consumer.setInstanceName("consumer1");
            // 程序第一次启动从消息队列头获取数据
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费100条
            consumer.setConsumeMessageBatchMaxSize(100);

            consumer.setMessageModel(MessageModel.CLUSTERING);
            //在此监听中消费信息，并返回消费的状态信息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {

                // 会把不同的消息分别放置到不同的队列中
                try {
                    //所以子类调用(建议只被继承一次)
                    for (String beanName : beanNames) {
                        BasePandaRocketMQListener mqListener = (BasePandaRocketMQListener) applicationContext.getBean(beanName);
                        mqListener.consumerExecute(msgs);
                    }
                } catch (Exception e) {
                    log.info("GeneralPandaMQConsumer Consumer get msg error:", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            log.info("GeneralPandaMQConsumer Consumer End");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
