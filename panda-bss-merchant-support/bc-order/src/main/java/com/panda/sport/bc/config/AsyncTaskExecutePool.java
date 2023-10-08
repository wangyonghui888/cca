package com.panda.sport.bc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName AsyncTaskExecutePool
 * @auth YK
 * @Description: 线程池配置
 * @Date 2020-09-01 15:44
 * @Version
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncTaskExecutePool implements AsyncConfigurer {

    //活跃时间/秒
    private final int keepAliveSeconds = 60;

    /**
    * @description: 线程池配置
    * @Param: []
    * @return: java.util.concurrent.Executor
    * @author: YK
    * @date: 2020/9/11 12:06
    */
    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //核心线程池大小
        executor.setCorePoolSize(corePoolSize*3);
        //最大线程数
        executor.setMaxPoolSize(corePoolSize*4);
        //队列容量
        executor.setQueueCapacity(9999);
        //活跃时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //线程名字前缀
        executor.setThreadNamePrefix("bc-order-async-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        return executor;
    }


    /**
    * @description:   异步任务异常处理
    * @Param: []
    * @return: org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
    * @author: YK
    * @date: 2020/9/11 12:07
    */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("===="+throwable.getMessage()+"====", throwable);
            log.error("exception method:"+method.getName());
        };
    }
}
