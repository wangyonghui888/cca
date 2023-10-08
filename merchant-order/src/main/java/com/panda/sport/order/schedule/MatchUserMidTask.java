package com.panda.sport.order.schedule;

import com.panda.sport.order.service.ActivityService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@JobHandler(value = "matchUserMidTask")
public class MatchUserMidTask extends IJobHandler {

    @Autowired
    private ActivityService activityService;




    /**
     * @param dateStr(1639238400000-1639245600000)
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String dateStr) throws Exception {
        Date now = new Date();
        long endL = now.getTime();
        long startL = now.getTime() - 1000 * 60 * 10;
        if(StringUtils.isNotEmpty(dateStr)){
            startL = now.getTime() - Long.valueOf(dateStr);
        }
        log.info("开始处理赛事用户中间表数据:" + endL);
            activityService.executeMatchUserMidTask(startL, endL);
        log.info("处理赛事用户中间表数据结束:" + (System.currentTimeMillis()-endL));
        return SUCCESS;
    }
}