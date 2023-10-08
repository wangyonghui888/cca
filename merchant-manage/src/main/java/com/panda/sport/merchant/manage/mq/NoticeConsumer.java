package com.panda.sport.merchant.manage.mq;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.po.merchant.mq.MarketResultMessagePO;
import com.panda.sport.merchant.manage.service.impl.MerchantNoticeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @auth: YK
 * @Description:公告消费
 * @Date:2020/6/24 11:43
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "{merchant.notice.topic}",
        consumerGroup = "${merchant.notice.group}",
        messageModel= MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadMax = 16
)
public class NoticeConsumer implements RocketMQListener<String> {

    @Autowired
    private MerchantNoticeServiceImpl merchantNoticeService;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        MarketResultMessagePO marketResultMessagePO = JSONObject.parseObject(message, MarketResultMessagePO.class);
        if (StringUtils.isEmpty(marketResultMessagePO)) {
            return;
        }
        if (!checkParameter(marketResultMessagePO)) {
            return;
        }
        try {
            setData(marketResultMessagePO);
        } catch (Exception e) {
            log.info("公告消费数据(NoticeConsumer)消费出错" + message);
            log.error("消费异常!", e);
        }
    }


    /**
     * 统一处理函数
     *
     * @param marketResultMessagePO
     * @param
     */
    public void setData(MarketResultMessagePO marketResultMessagePO) {
        merchantNoticeService.addOsmcMqNotice(marketResultMessagePO);
    }

    /**
     * 检验参数
     *
     * @param marketResultMessagePO
     * @return
     */
    private Boolean checkParameter(MarketResultMessagePO marketResultMessagePO) {
        if (StringUtils.isEmpty(marketResultMessagePO.getMatchId())) {
            return false;
        }
        if (StringUtils.isEmpty(marketResultMessagePO.getMarketId())) {
            return false;
        }
        return !StringUtils.isEmpty(marketResultMessagePO.getMarketOptionsResults());
    }

}
