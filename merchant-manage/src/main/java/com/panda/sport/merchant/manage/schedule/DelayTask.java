package com.panda.sport.merchant.manage.schedule;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@EqualsAndHashCode
public class DelayTask implements Delayed {
    private long id;
    //任务执行时间
    private long time ;
    //任务类型
    private int type;

    public DelayTask(long id,long time,int type) {
        this.id = id;
        this.time = time;
        this.type = type;
    }

    /**
     * 需要实现的接口，获得延迟时间   用过期时间-当前时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(time - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        DelayTask o1 = (DelayTask) o;
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}
