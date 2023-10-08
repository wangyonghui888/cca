package com.panda.sport.order.schedule;


import com.panda.sport.order.service.ActivityService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobHandler(value = "ActivityClearDataTask")
public class ActivityClearDataTask extends IJobHandler {

    @Autowired
    private ActivityService activityService;

/*

    @Scheduled(cron = "1 0 0 * * ?")//每天00:01
    public void executeClear() {
        try {
            long start = System.currentTimeMillis();
            log.info("1clear任务:" + start);
            activityService.clearDailyTask();
            log.info("1clear任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("1任务执行结束ERROR!", e);
        }
    }*/

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        activityService.clearDailyTask();
        return SUCCESS;
    }
}