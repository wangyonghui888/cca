package com.panda.sport.merchant.manage.mq;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.vo.SportDutyTraderMqVO;
import com.panda.sport.merchant.manage.service.SportDutyTraderService;
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
        topic = "RCS_UPDATE_SHIFT",
        messageModel = MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadMax = 2,
        consumerGroup = "MERCHANT_SPORT_DUTY_TRADER")
public class SportDutyTraderConsumer implements RocketMQListener<String> {
    @Resource
    private SportDutyTraderService sportDutyTraderService;
    @Override
    public void onMessage(String message) {
        log.info("收到排班信息，{}",message);
        SportDutyTraderMqVO mqVO = JSONObject.parseObject(message, SportDutyTraderMqVO.class);
        sportDutyTraderService.syncTrader(mqVO.getData());
    }
}
