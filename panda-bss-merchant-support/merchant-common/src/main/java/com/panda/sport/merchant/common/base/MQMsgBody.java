package com.panda.sport.merchant.common.base;

import com.panda.sport.merchant.common.annotation.FieldExplain;
import com.panda.sport.merchant.common.enums.MQMessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MQMsgBody {
    @FieldExplain("消息key")
    private String key;
    @FieldExplain("消息tag")
    private String tag;
    @FieldExplain("消息队列")
    private String topic;
    @FieldExplain("消息对象")
    private Object obj;
}
