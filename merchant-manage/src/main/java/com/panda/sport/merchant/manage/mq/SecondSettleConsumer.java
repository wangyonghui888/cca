package com.panda.sport.merchant.manage.mq;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.manage.entity.vo.OrderRepeatSettleVo;
import com.panda.sport.merchant.manage.feign.MerchantReportClient;
import com.panda.sport.merchant.manage.service.IMongoService;
import com.panda.sport.merchant.manage.service.impl.MongoServiceImpl;
import com.panda.sport.merchant.manage.service.impl.SecondSettleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
@RocketMQMessageListener(
        topic = "STANDARD_SECOND_SETTLE_REFRESH_REPORT",
        messageModel = MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadMax = 2,
        consumerGroup = "MERCHANT_MANAGE")
public class SecondSettleConsumer implements RocketMQListener<String> {


    @Autowired
    private SecondSettleService secondSettleService;

    @Override
    public void onMessage(String message) {
        log.info("二次结算增量报表 收到商户预警消息，{}",message);
        OrderRepeatSettleVo orderRepeatSettleVo = JSONObject.parseObject(message, OrderRepeatSettleVo.class);
        log.info("二次结算增量报表 消息消费完成！{}", JSONObject.toJSONString(orderRepeatSettleVo));
        if (orderRepeatSettleVo != null && orderRepeatSettleVo.getSettleTime() != null) {
            String date = DateUtil.format(DateUtil.date(orderRepeatSettleVo.getSettleTime()), "yyyy-MM-dd");
            secondSettleService.executeFinanceDay(date);
        }else {
            log.info("二次结算增量报表 消费数据为空！");
        }
    }
}
