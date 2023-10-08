package com.panda.sport.merchant.common.po.merchant.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * @auth: YK
 * @Description:MQ消费外层数据对象
 * @Date:2020/6/24 19:36
 */
@Data
public class MqConsumerMessagePo<T> implements Serializable {

    /**
     * 上游linkId
     */
    private String linkId;

    /**
     * 具体数据对象
     */
    private T data;
}
