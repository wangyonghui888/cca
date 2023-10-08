package com.panda.sport.merchant.manage.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RefreshScope
public class PubMQProducer {

    public static DefaultMQProducer producer;

    @Value("${pub.rocketmq.producer.groupName:producer-merchant-group}")
    private String producerGroup;

    @Value("${pub.rocketmq.name-server:172.18.199.83:9876;172.18.199.84:9876;172.18.199.85:9876}")
    private String nameServer;

    @Value("${pub.rocketmq.instance:merchant_producer}")
    private String instanceName;

    @Value("${pub.rocketmq.accessKey:administrator}")
    private String accessKey;

    @Value("${pub.rocketmq.secretKey:Q73Ew360qc}")
    private String secretKey;

    @Value("${pub.rocketmq.delayTimeLevel:16}")
    private Integer delayTimeLevel;

    /*
        accessKey: administrator    secretKey: Q73Ew360qc
     */
    @PostConstruct
    private void initMQProducer() throws MQClientException {

        producer = new DefaultMQProducer(producerGroup, new AclClientRPCHook(new SessionCredentials(accessKey, secretKey)));

        producer.setNamesrvAddr(nameServer);

        producer.setInstanceName(instanceName);

        producer.setRetryTimesWhenSendFailed(5);

        producer.start();

        log.info("Pub生产者MQ启动成功----------------------");
    }

    public boolean send(String data, String topic, String tag, String key) {
        Message msg = getMessage(data, topic, tag, key);
        try {
            SendResult result = producer.send(msg);
            if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("发送失败!!!>>key:" + key + "data:" + data);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("MQProducer send error info:" + data, e);
            return false;
        }
    }

    public boolean sendDelayed(String data, String topic, String tag, String key) {
        Message msg = getDelayedMessage(data, topic, tag, key);
        try {
            SendResult result = producer.send(msg);
            if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("发送失败!!!>>key:" + key + "data:" + data);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("MQProducer send error info:" + data, e);
            return false;
        }
    }

    /**
     * 设置消息体
     *
     * @param data
     * @param topic
     * @param tag
     * @return
     */
    private Message getMessage(String data, String topic, String tag, String key) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setBody(data.getBytes());
        msg.setKeys(key);
        if (StringUtils.isNotBlank(tag)) {
            msg.setTags(tag);
        }
        return msg;
    }

    /**
     * 延迟推送  "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
     * */
    private Message getDelayedMessage(String data, String topic, String tag, String key) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setBody(data.getBytes());
        msg.setKeys(key);
        msg.setDelayTimeLevel(delayTimeLevel);//30min
//        msg.setDelayTimeLevel(16);//30min
        if (StringUtils.isNotBlank(tag)) {
            msg.setTags(tag);
        }
        return msg;
    }
}