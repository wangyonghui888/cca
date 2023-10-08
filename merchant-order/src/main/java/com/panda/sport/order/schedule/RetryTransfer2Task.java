package com.panda.sport.order.schedule;


import com.panda.sport.order.service.UserTransferService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobHandler(value = "RetryTransfer2Task")
public class RetryTransfer2Task extends IJobHandler {
    @Autowired
    private UserTransferService transferService;


    //@Scheduled(cron = "*/5 * * * * ?")
    //@Scheduled(cron = "0 0 1 1/1 * ?")
    //@Scheduled(cron = "0 0 * * *  ?")//每小时
/*
    @Scheduled(cron = "0 1 * * *  ?")
    public void execute() {
        long start = System.currentTimeMillis();
        log.info("开始执行重试免转钱包任务:" + start);
        transferService.executeRetry();
        log.info("重试免转钱包任务执行结束:" + (System.currentTimeMillis() - start));
    }
*/

    @Override
    public ReturnT<String> execute(String s) throws Exception {

        long start = System.currentTimeMillis();
        log.info("开始执行重试免转钱包任务:" + start);
        transferService.executeRetry2();
        log.info("重试免转钱包任务执行结束:" + (System.currentTimeMillis() - start));
        return SUCCESS;
    }
}