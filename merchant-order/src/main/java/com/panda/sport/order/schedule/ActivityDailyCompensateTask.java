package com.panda.sport.order.schedule;

import com.panda.sport.order.service.ActivityService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@JobHandler(value = "activityDailyCompensateTask")
public class ActivityDailyCompensateTask extends IJobHandler {

    @Autowired
    private ActivityService activityService;

    /**
     * @param dateStr(1639238400000-1639245600000)
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String dateStr) throws Exception {
        /**
         * 定时任务每天跑之前1小时前8分钟的数据
         * 每天的第一个小时不需要跑数据
         */
        Date now = new Date();
        String hour = DateFormatUtils.format(now, "yyyyMMddHH");
        if (hour.endsWith("01") || hour.endsWith("00")) {
            log.info("ActivityDailyCompensateTask跳过当前时间为:" + hour);
            return SUCCESS;
        }
        long startL = now.getTime() - 1000 * 60 * 65;
        long endL = now.getTime() - 1000 * 60 * 60;
        Long nowL = Long.parseLong(DateFormatUtils.format(now, "yyyyMMdd"));
        activityService.executeDailyTask(startL, endL, nowL);
        return SUCCESS;
    }
}