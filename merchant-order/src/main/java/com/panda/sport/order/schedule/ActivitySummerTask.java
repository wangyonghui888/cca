package com.panda.sport.order.schedule;

import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.order.feign.MerchantApiClient;
import com.panda.sport.order.service.ActivityService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobHandler(value = "ActivitySummerTask")
public class ActivitySummerTask extends IJobHandler {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private MerchantMapper merchantMapper;

    //@Scheduled(cron = "0 5 * * * ?")//每H
/*
    @Scheduled(cron = "0 25/30 * * * ?")//每30Mins
    public void executeSum() {
        try {
            long start = System.currentTimeMillis();
            log.info("2开始执行成长任务:" + start);
            activityService.executeSumTask();
            log.info("2任务执行成长结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("2任务执行结束ERROR!", e);
        }
    }
*/

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        activityService.executeSumTask();
        return SUCCESS;
    }
}