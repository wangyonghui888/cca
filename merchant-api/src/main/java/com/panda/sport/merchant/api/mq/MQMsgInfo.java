package com.panda.sport.merchant.api.mq;

import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.base
 * @Description: 基本消息体
 * @Date: 2019/12/30 18:28
 * @Version: v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MQMsgInfo {

    @FieldExplain("消息key")
    private String mqKey;
    @FieldExplain("消息tag")
    private String mqTag;
    @FieldExplain("消息队列")
    private String topic;
    @FieldExplain("消息对象")
    private Object obj;
}
