package com.panda.sport.order.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ExecutorInstance {
    public static ExecutorService executorService = new ThreadPoolExecutor(8, 32,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
}
