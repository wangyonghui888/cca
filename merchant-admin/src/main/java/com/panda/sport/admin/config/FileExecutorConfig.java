package com.panda.sport.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.order.config
 * @Description :  文件线程池
 * @Date: 2020-12-11 10:55:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Configuration
@EnableAsync
@Slf4j
public class FileExecutorConfig {

    @Value("${merchant.file.core.pool.size:1}")
    private Integer fileCorePoolSize;

    @Value("${merchant.file.max.pool.size:3}")
    private Integer fileMaxPoolSize;

    @Bean("asyncServiceExecutor")
    public Executor asyncServiceExecutor() {

        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(fileCorePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(fileMaxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
