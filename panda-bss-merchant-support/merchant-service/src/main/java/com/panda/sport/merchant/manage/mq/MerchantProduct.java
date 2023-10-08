package com.panda.sport.merchant.manage.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
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


/**
 * @author :  butr
 * @Project Name :商户信息MQ发送
 * @Package Name :
 * @Description :
 * @Date: 2020-09-24 16:28
 */
@Slf4j
@Component
public class MerchantProduct {

    private static String topic = "MERCHANT_INFO_RESULT";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Async
    public void sendMessage(MerchantPO msgInfo) {
        if (msgInfo != null) {
            msgInfo.setLogo("");
        }
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("KEYS", msgInfo.getMerchantCode());
        Message<?> message = null;
        try {
            msgInfo.setMerchantName(null);
            msgInfo.setMerchantAdmin(null);
            msgInfo.setMerchantKey(null);
            msgInfo.setPassword(null);
            msgInfo.setAdminPassword(null);
            msgInfo.setParentName(null);
            msgInfo.setUrl(null);
            msgInfo.setCallbackUrl(null);
            msgInfo.setBalanceUrl(null);
            msgInfo.setWhiteIp(null);
            msgInfo.setChildAmount(null);
            msgInfo.setStatusDescription(null);
            msgInfo.setComputingStandard(null);
            msgInfo.setTerraceRate(null);
            msgInfo.setVipAmount(null);
            msgInfo.setVipPaymentCycle(null);
            msgInfo.setTechniqueAmount(null);
            msgInfo.setTechniquePaymentCycle(null);
            msgInfo.setPhone(null);
            msgInfo.setContact(null);
            msgInfo.setEmail(null);
            msgInfo.setLogo(null);
            msgInfo.setTopic(null);
            msgInfo.setTransferMode(null);
            msgInfo.setOpenVrSport(null);
            msgInfo.setSettleSwitchAdvance(null);
            msgInfo.setPaymentCycle(null);
            String content = rocketMQTemplate.getObjectMapper().writeValueAsString(msgInfo);
            MessageBuilder<?> builder = MessageBuilder.withPayload(content);
            builder.copyHeaders(headers);
            builder.setHeaderIfAbsent("contentType", MimeTypeUtils.TEXT_PLAIN);
            message = builder.build();
        } catch (JsonProcessingException e) {
            log.error("mq消息发送时json转换失败，消息体：{}", msgInfo, e);
        }
        rocketMQTemplate.asyncSendOrderly(topic, message, msgInfo.getMerchantCode(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    log.info("消息发送成功！{}", msgInfo);
                }
            }

            @Override
            public void onException(Throwable e) {
                log.error("消息发送异常!", e);
                log.error("异常消息体！{}", msgInfo.toString());
            }
        });
    }

}
