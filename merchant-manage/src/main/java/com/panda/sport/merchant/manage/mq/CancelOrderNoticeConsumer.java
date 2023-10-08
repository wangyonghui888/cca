package com.panda.sport.merchant.manage.mq;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.po.merchant.mq.CancelOrderNoticePO;
import com.panda.sport.merchant.manage.service.MultiTerminalNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @auth: ives
 * @Description:二次结算&取消注单自动公告
 * @Date:2022/4/11
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "${merchant.cancel.order.notice.topic}",
        consumerGroup = "${merchant.cancel.order.notice.group}",
        messageModel= MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadMax = 16
)
public class CancelOrderNoticeConsumer implements RocketMQListener<String> {

    @Resource
    private MultiTerminalNoticeService multiTerminalNoticeService;

    @Override
    public void onMessage(String message) {
        String title = "二次结算&取消注单自动公告";
        if (StringUtils.isEmpty(message)) {
            return;
        }
        log.info("二次结算MQ收到消息：【{}】",message);
        CancelOrderNoticePO cancelOrderNoticePO = JSONObject.parseObject(message, CancelOrderNoticePO.class);
        if (StringUtils.isEmpty(cancelOrderNoticePO)) {
            return;
        }
        try {
            saveCancelOrderNotice(cancelOrderNoticePO);
        } catch (Exception e) {
            log.error("{}消费异常,原因：{}", title, e);
        }
    }



    public void saveCancelOrderNotice(CancelOrderNoticePO cancelOrderNoticePO) {
        multiTerminalNoticeService.saveCancelOrderNotice(cancelOrderNoticePO);
    }

}
