package com.panda.center.mq.producer;

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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class PubMQProducer {

    public static DefaultMQProducer producer;

    @Value("${pub.rocketmq.producer.groupName}")
    private String producerGroup;

    @Value("${pub.rocketmq.name-server}")
    private String nameServer;

    @Value("${pub.rocketmq.instance}")
    private String instanceName;

    @Value("${pub.rocketmq.accessKey}")
    private String accessKey;

    @Value("${pub.rocketmq.secretKey}")
    private String secretKey;

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

    private Message getDelayedMessage(String data, String topic, String tag, String key) {
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setBody(data.getBytes());
        msg.setKeys(key);
        msg.setDelayTimeLevel(2);//5S
        if (StringUtils.isNotBlank(tag)) {
            msg.setTags(tag);
        }
        return msg;
    }
}