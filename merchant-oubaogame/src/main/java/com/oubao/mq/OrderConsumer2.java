
package com.oubao.mq;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.stereotype.Service;


//@Service
@Slf4j
public class OrderConsumer2 {
/*

    public static void main(String[] args) throws MQClientException {
        //定义消费者名称，MQ往消费者推送
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("valarSoHandsome",
                getAclRPCHook("administrator", "Q73Ew360qc"), new AllocateMessageQueueAveragely());
        //连接rocketMQ的namesrv地址（此次为集群）
        consumer.setNamesrvAddr("pre-sh-mq-1.sportxxx278gwf4.com:9876;pre-sh-mq-2.sportxxx278gwf4.com:9876;pre-sh-mq-3.sportxxx278gwf4.com:9876");
        //新订阅组第一次启动，从头消费到尾，后续从上次的消费进度继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);//订阅的主题和标签（*代表所有标签）
        consumer.subscribe("big_data", "*");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //消费者监听
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    String topic = msg.getTopic();
                    String msgbody = new String(msg.getBody(), "UTF-8");
                    String tag = msg.getTags();
                    log.info("topic:" + topic + " msgbody:" + msgbody + " tag:" + tag);
                    System.out.println("topic:" + topic + " msgbody:" + msgbody + " tag:" + tag);
                } catch (Exception e) {
                    log.error("MQ发送失败!", e);
                    //MQ发送失败重试机制，1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            //消息处理成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("DefaultPandaRocketConsumer Consumer End");
    }

    static RPCHook getAclRPCHook(String access, String secret) {
        return new AclClientRPCHook(new SessionCredentials(access, secret));
    }
*/


}
