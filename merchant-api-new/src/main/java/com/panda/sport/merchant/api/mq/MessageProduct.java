package com.panda.sport.merchant.api.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.sport.merchant.api.util.ExecutorInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.order.mq
 * @Description: 消息统一发送
 * @Date: 2019/12/30 18:16
 * @Version: v1.0
 */
@Component
@Slf4j
//@Async("asyncServiceExecutor")
public class MessageProduct {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(MQMsgInfo msgInfo, String logPrefix) {
        ExecutorInstance.executorService.submit(() -> {
            if (msgInfo != null) {
                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put("KEYS", msgInfo.getMqKey());
                Message<?> message = null;
                try {
                    String content = rocketMQTemplate.getObjectMapper().writeValueAsString(msgInfo.getObj());
                    MessageBuilder<?> builder = MessageBuilder.withPayload(content);
                    builder.copyHeaders(headers);
                    builder.setHeaderIfAbsent("contentType", MimeTypeUtils.TEXT_PLAIN);
                    message = builder.build();
                } catch (JsonProcessingException e) {
                    log.error("{},mq消息发送时json转换失败，消息体：{}", logPrefix, msgInfo.getObj().toString(), e);
                }
                rocketMQTemplate.asyncSendOrderly(msgInfo.getTopic() + ":" + msgInfo.getMqTag(), message, msgInfo.getMqKey(), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            log.info("{},消息发送成功！{}", logPrefix, msgInfo.getObj().toString());
                        }
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("{},消息发送异常!", logPrefix, e);
                        //这里框架本身也会重新重试发送3次
                        //放入我们自己的队列再次发送3次,重发策略：1分钟重发，失败则3分钟之后再次重发，失败则5分钟再次重发，失败则删除丢列，丢弃消息或者放入异常投注表
                    }
                });

            } /*else {
            throw new ServiceException("消息发送体不能为空！");
        }*/
        });
    }
}
