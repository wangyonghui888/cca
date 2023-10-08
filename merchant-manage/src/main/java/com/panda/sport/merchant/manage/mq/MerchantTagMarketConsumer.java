package com.panda.sport.merchant.manage.mq;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.manage.mq.vo.TagMarketMsg;
import com.panda.sport.merchant.manage.service.MerchantService;
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
        topic = "${dynamic.risk.control.odds.grouping.merchant.config.topic:RCS_BUSINESS_TAG_MARKET_STATUS_TOPIC}",
        messageModel = MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadMax = 2,
        consumerGroup = "${dynamic.risk.control.odds.grouping.merchant.config.group:MERCHANT_TAT_MARKET_GROUP}")
public class MerchantTagMarketConsumer implements RocketMQListener<String> {
    @Resource
    private MerchantService merchantService;

    @Override
    public void onMessage(String message) {
        log.info("收到Tag信息，{}", message);
        TagMarketMsg msg = JSONObject.parseObject(message, TagMarketMsg.class);
        if (msg != null) {
            log.info("收到Tag信息msg，{}", msg);
            merchantService.updateMerchantMarketData(msg);
        }
    }
}
