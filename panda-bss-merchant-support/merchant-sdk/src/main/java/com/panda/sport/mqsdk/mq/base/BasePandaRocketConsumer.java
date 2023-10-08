package com.panda.sport.mqsdk.mq.base;

import com.panda.sport.api.util.PropertyUtil;
import com.panda.sport.mqsdk.mq.config.PandaRocketMQConfig;
import org.springframework.context.ApplicationContext;

import java.util.Properties;

/**
 * @Description: 消费者基类
 * @Author: Jeffrey
 * @Date: 2020/1/4
 */
public abstract class BasePandaRocketConsumer {
    /**
     * 注册消费者监听器
     *
     * @Description: spring boot模式 与 非spring 模式
     * @Author: Jeffrey
     * @Date: 2020/1/4
     */
    protected abstract void messageListener(PandaRocketMQConfig config, ApplicationContext applicationContext);

    public void init(PandaRocketMQConfig config, ApplicationContext applicationContext) {
        //判断配置是否为空
        config = new PandaRocketMQConfig();
        Properties pro = PropertyUtil.getConfig("/panda-sdk.properties");
        //3、获取配置文件.properties中的配置信息
        String groupName = pro.getProperty("panda.rocketmq.consumer.groupName");
        String namesrvAddr = pro.getProperty("panda.rocketmq.consumer.namesrvAddr");
        String topic = pro.getProperty("panda.rocketmq.consumer.topic");
        String consumerClass = pro.getProperty("panda.rocketmq.consumer.class");
        String accessKey = pro.getProperty("panda.rocketmq.consumer.accessKey");
        String secretKey = pro.getProperty("panda.rocketmq.consumer.secretKey");

        config.setGroupName(groupName);
        config.setNamesrvAddr(namesrvAddr);
        config.setTopic(topic);
        config.setAccessKey(accessKey);
        config.setSecretKey(secretKey);

        messageListener(config, applicationContext);
    }
}