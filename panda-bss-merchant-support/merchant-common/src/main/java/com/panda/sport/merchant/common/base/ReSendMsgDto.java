package com.panda.sport.merchant.common.base;

import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.base
 * @Description: 消息重发对象
 * @Date: 2019/10/21 21:51
 * @Version: 1.0
 */
@Data
@ToString
public class ReSendMsgDto {

    @FieldExplain("重试次数")
    private AtomicInteger tryTimes = new AtomicInteger(3);
    @FieldExplain("下一次重试时间")
    private AtomicLong sendTime;
    @FieldExplain("重发对象")
    private Object obj;

}
