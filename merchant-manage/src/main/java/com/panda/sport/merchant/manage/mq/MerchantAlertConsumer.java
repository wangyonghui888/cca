package com.panda.sport.merchant.manage.mq;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.vo.MerchantAmountAlertVO;
import com.panda.sport.merchant.manage.service.MerchantNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author javier
 * 操盘手值班消费
 * 2021/2/11 11:43
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "MERCHANT_ACCOUNT_ALERT",
        messageModel = MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadMax = 2,
        consumerGroup = "MERCHANT_MANAGE_ACCOUNT_ALERT")
public class MerchantAlertConsumer implements RocketMQListener<String> {

    @Resource
    private MerchantNewsService merchantNewsService;
    @Override
    public void onMessage(String message) {
        log.info("收到商户预警消息，{}",message);
        MerchantAmountAlertVO merchantAmountAlert = JSONObject.parseObject(message, MerchantAmountAlertVO.class);
        merchantNewsService.addMerchantAlertFromUsedAmount(merchantAmountAlert);
    }
}
