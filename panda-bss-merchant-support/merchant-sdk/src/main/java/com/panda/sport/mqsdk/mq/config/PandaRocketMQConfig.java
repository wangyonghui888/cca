package com.panda.sport.mqsdk.mq.config;

import com.panda.sport.api.util.PropertyUtil;
import com.panda.sport.mqsdk.mq.base.MqConsumer1;
import com.panda.sport.mqsdk.mq.base.MqConsumer2;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Properties;

/**
 * @author : Jeffrey
 * @Date: 2019-12-25 17:19
 * @Description : MQ消费者配置
 */
@Configuration
@Data
public class PandaRocketMQConfig {

    @Value("${panda.rocketmq.consumer.groupName}")
    private String groupName;

    @Value("${panda.rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;

    @Value("${panda.rocketmq.consumer.topic}")
    private String topic;

    @Value("${panda.rocketmq.consumer.messageModel:CLUSTERING}")
    private String messageModel;

    @Value("${panda.rocketmq.consumer.consumerThreadNums:64}")
    private String consumerThreadNums;

    @Value("${panda.rocketmq.consumer.maxReconsumeTimes:16}")
    private String maxReconsumeTimes;

    @Value("${panda.rocketmq.consumer.consumeTimeout:15}")
    private String consumeTimeout;

    @Value("${panda.rocketmq.consumer.accessKey:ag}")
    private String accessKey;

    @Value("${panda.rocketmq.consumer.secretKey:123456}")
    private String secretKey;


    @Bean
    @Order(1)
    @ConditionalOnMissingBean(MqConsumer2.class)
    @ConditionalOnProperty(name = "panda.rocketmq.springboot.enabled", havingValue = "true")
    public MqConsumer1 mqConsumer1() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ConsumerId, groupName);
        if (StringUtils.isNotEmpty(accessKey))
            properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        if (StringUtils.isNotEmpty(secretKey))
            properties.setProperty(PropertyKeyConst.SecretKey, secretKey);

        if (StringUtils.isNotEmpty(messageModel))
            properties.setProperty(PropertyKeyConst.MessageModel, messageModel);

        if (StringUtils.isNotEmpty(consumerThreadNums))
            properties.setProperty(PropertyKeyConst.ConsumeThreadNums, consumerThreadNums);

        if (StringUtils.isNotEmpty(maxReconsumeTimes))
            properties.setProperty(PropertyKeyConst.MaxReconsumeTimes, maxReconsumeTimes);

        if (StringUtils.isNotEmpty(consumeTimeout))
            properties.setProperty(PropertyKeyConst.ConsumeTimeout, consumeTimeout);

        properties.setProperty(PropertyKeyConst.ONSAddr, namesrvAddr);
        properties.setProperty(PropertyKeyConst.Topic, topic);
        return new MqConsumer1(properties);
    }

    @Bean
    @Order(2)
    @ConditionalOnMissingBean(MqConsumer1.class)
    public MqConsumer2 mqConsumer2() throws Exception {
        Properties pro = PropertyUtil.getConfig("/panda-sdk.properties");
        if (pro == null) {
            return null;
        }
        String group = pro.getProperty("panda.rocketmq.consumer.groupName");
        String namesrv = pro.getProperty("panda.rocketmq.consumer.namesrvAddr");
        String topic = pro.getProperty("panda.rocketmq.consumer.topic");
        String access = pro.getProperty("panda.rocketmq.consumer.accessKey");
        String secret = pro.getProperty("panda.rocketmq.consumer.secretKey");

        String model = pro.getProperty("panda.rocketmq.consumer.messageModel");
        String threadNums = pro.getProperty("panda.rocketmq.consumer.consumerThreadNums");
        String reconsumeTimes = pro.getProperty("panda.rocketmq.consumer.maxReconsumeTimes");
        String timeout = pro.getProperty("panda.rocketmq.consumer.consumeTimeout");

        if (StringUtils.isNotBlank(access))
            pro.setProperty(PropertyKeyConst.AccessKey, access);

        if (StringUtils.isNotBlank(secret))
            pro.setProperty(PropertyKeyConst.SecretKey, secret);

        if (StringUtils.isNotBlank(model))
            pro.setProperty(PropertyKeyConst.MessageModel, model);

        if (StringUtils.isNotBlank(threadNums))
            pro.setProperty(PropertyKeyConst.ConsumeThreadNums, threadNums);

        if (StringUtils.isNotBlank(reconsumeTimes))
            pro.setProperty(PropertyKeyConst.MaxReconsumeTimes, reconsumeTimes);

        if (StringUtils.isNotBlank(timeout))
            pro.setProperty(PropertyKeyConst.ConsumeTimeout, timeout);

        pro.setProperty(PropertyKeyConst.ConsumerId, group);
        pro.setProperty(PropertyKeyConst.ONSAddr, namesrv);
        pro.setProperty(PropertyKeyConst.Topic, topic);
        return new MqConsumer2(pro);
    }

}
