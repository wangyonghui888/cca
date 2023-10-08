package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.manage.schedule.DelayTask;

import java.util.concurrent.Delayed;

/**
 * 延迟队列
 * @param <T>
 */
public interface IDelayQueueService<T extends Delayed> {
    /**
     * 添加元素
     * @param item
     * @return
     */
    boolean add(T item);

    /**
     * 取消元素
     * @param item
     */
    boolean tryCancel(T item);

    /**
     * 消费元素
     */
    void consumer() throws InterruptedException;

    /**
     * 初始化
     */
    void init();
}
