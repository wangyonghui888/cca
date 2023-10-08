package com.panda.sport.merchant.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.sport.merchant.common.base.MQMsgBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Async
public class MQProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(MQMsgBody msgInfo) {
        try {
            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("KEYS", msgInfo.getKey());
            Message<?> message = null;
            try {
                String content = rocketMQTemplate.getObjectMapper().writeValueAsString(msgInfo.getObj());
                MessageBuilder<?> builder = MessageBuilder.withPayload(content);
                builder.copyHeaders(headers);
                builder.setHeaderIfAbsent("contentType", MimeTypeUtils.TEXT_PLAIN);
                message = builder.build();
            } catch (JsonProcessingException e) {
                log.error(msgInfo.getObj().toString(), e);
            }
            rocketMQTemplate.asyncSendOrderly(msgInfo.getTopic() + ":" + msgInfo.getTag(), message, msgInfo.getKey(), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                        // log.info("消息发送成功！" + msgInfo.getObj().toString());
                    }
                }
                @Override
                public void onException(Throwable e) {
                    log.error("消息发送异常!" + msgInfo, e);
                }
            });
        } catch (Exception e) {
            log.error("异步发送消息异常", msgInfo);
        }
    }

}
