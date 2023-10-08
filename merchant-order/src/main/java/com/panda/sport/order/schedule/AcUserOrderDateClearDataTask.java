package com.panda.sport.order.schedule;


import com.panda.sport.order.service.UserAccountClearDataService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@JobHandler(value = "acUserOrderDateClearDataTask")
public class AcUserOrderDateClearDataTask extends IJobHandler {

    @Autowired
    private UserAccountClearDataService userAccountClearDataService;

    @Override
    public ReturnT<String> execute(String num) throws Exception {
        try {
            Date date = new Date();
            Long nowL = Long.parseLong(DateFormatUtils.format(date, "yyyyMMdd"));
            long startTime = System.currentTimeMillis();

            XxlJobLogger.log("acUserOrderDateClearDataTask任务开始的时间为:" + startTime);
            userAccountClearDataService.acUserOrderDateClearDataTask(nowL);
            XxlJobLogger.log("acUserOrderDateClearDataTask任务结束,共消耗时间:" + (System.currentTimeMillis() - startTime));
            return SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log("acUserOrderDateClearDataTask异常!", e);
            return IJobHandler.FAIL;
        }
    }

}