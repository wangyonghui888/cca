package com.panda.sport.merchant.manage.mq;


import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.vo.merchant.MerchantKeyVO;
import com.panda.sport.merchant.manage.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = "MERCHANT_KEY_ALARM",
        messageModel = MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadMax = 2,
        consumerGroup = "MERCHANT_KEY_ALARM_GROUP")
public class MerchantKeyConsumer implements RocketMQListener<String> {


    @Resource
    private MerchantService merchantService;

    @Override
    public void onMessage(String message) {
        log.info("收到MQ消息时间，{}",System.currentTimeMillis());
        MerchantKeyVO merchantKeyVO = JSONObject.parseObject(message, MerchantKeyVO.class);
        merchantService.sendMongoMsg(merchantKeyVO);
    }
}
