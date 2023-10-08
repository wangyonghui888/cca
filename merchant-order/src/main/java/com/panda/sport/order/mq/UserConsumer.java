package com.panda.sport.order.mq;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.bo.UserInfoBO;
import com.panda.sport.order.service.UserLogHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("UserConsumer")
public class UserConsumer {

    @Value("${merchant.rocketmq.consumer.groupName:merchantOrder}")
    private String consumerGroup;

    @Value("${merchant.rocketmq.consumer.topic:panda_user_info}")
    private String topic;

    @Value("${merchant.rocketmq.name-server:172.21.185.83:9876;172.21.185.84:9876;172.21.185.85:9876}")
    private String nameServer;

    @Autowired
    private UserLogHistoryService userLogHistoryService;


    @Bean
    private DefaultMQPushConsumer initUserMQConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        consumer.setNamesrvAddr(nameServer);

        consumer.subscribe(topic, "*");

        // 设置每次消息拉取的时间间隔，单位毫秒
        consumer.setPullInterval(1000);

        // 设置每个队列每次拉取的最大消息数
        consumer.setPullBatchSize(1000);

        // 设置消费者单次批量消费的消息数目上限
        consumer.setConsumeMessageBatchMaxSize(1024);

        consumer.setConsumeThreadMax(64);

        consumer.setConsumeThreadMin(8);

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            log.info(topic + "消费用户数据共:" + msgs.size());
            List<UserInfoBO> userInfoList = new ArrayList<>(msgs.size());
            msgs.forEach(msg -> {
                userInfoList.add(JSONObject.parseObject(msg.getBody(), UserInfoBO.class));
            });
            Map<String, List<UserInfoBO>> collectMap = userInfoList.stream().collect(Collectors.groupingBy(UserInfoBO::getMerchantCode));

            for (Map.Entry entry : collectMap.entrySet()) {

                String merchantCode = (String) entry.getKey();

                List<UserInfoBO> uList = (List<UserInfoBO>) entry.getValue();

                userLogHistoryService.modifyUserProperties(merchantCode, uList);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        return consumer;
    }
}