package com.panda.sport.merchant.api.util;

import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.merchant.common.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LogHistoryBlockQueue {

    /**
     * 处理登陆的消息队列
     */
    public static final BlockingQueue<Message> loginHistoryQueue = new LinkedBlockingDeque<>();

    @Bean
    public BlockingQueue<Message> getQueue() {
        return loginHistoryQueue;
    }

    @Bean
    public void startConsumer() {
        ExecutorInstance.executorService.submit(new Consumer(loginHistoryQueue));
    }

    /**
     * 消费者,将队列中uid最后登陆时间去重后落库
     */
    @Component
    public static class Consumer implements Runnable {

        @Autowired
        private TUserMapper userMapper;

        /**
         * 批量操作的最大条数
         */
        private final static int MAX_BATCH_SIZE = 100;

        /**
         * 等待时长
         */
        private final static long MAX_WAIT_TIME = 5_000;

        /**
         * 需要处理的queue
         */
        private BlockingQueue<Message> queue;

        public Consumer(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                loop();
            } catch (InterruptedException e) {
                log.error("消费登陆时间入库失败！", e);
            }
        }
        private void loop() throws InterruptedException {
            Message message;
            List<Message> messages;
            while (true) {
                messages = new ArrayList<>(MAX_BATCH_SIZE);
                message = this.queue.take();
                addMessage(messages, message);
                int i = 0;
                message = queue.poll(MAX_WAIT_TIME, TimeUnit.MILLISECONDS);
                while (++i < MAX_BATCH_SIZE && message != null) {
                    addMessage(messages, message);
                }
                log.info("处理{}条登陆日志落库", messages.size());
                userMapper.batchUpdateLastLogin(messages);
            }
        }

        private void addMessage(List<Message> messages, Message message) {
            if (message == null) {
                log.info("暂无登陆日志处理...");
                return;
            }
            if (messages.stream().map(Message::getUid).anyMatch(uid -> uid.equals(message.getUid()))) {
                messages.removeIf(msg -> msg.getUid().equals(message.getUid()));
            }
            messages.add(message);
        }
    }
}
