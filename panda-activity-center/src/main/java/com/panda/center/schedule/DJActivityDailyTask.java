package com.panda.center.schedule;

import com.panda.center.service.BonusService;
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
@JobHandler(value = "DJActivityDailyTask")
public class DJActivityDailyTask extends IJobHandler {

    @Autowired
    private BonusService bonusService;

    /**
     * @param dateStr(1639238400000-1639245600000)
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String dateStr) throws Exception {
        Date now = new Date();
        long endL = now.getTime();
        long startL = now.getTime() - 1000 * 60 * 20;
        Long nowL = Long.parseLong(DateFormatUtils.format(now, "yyyyMMdd"));
        if (StringUtils.isNotEmpty(dateStr)) {
            log.info("dateStr=" + dateStr);
            String[] dateRange = dateStr.split("-");
            String a = dateRange[0];
            String b = dateRange[1];
            long startTimeStampL = Long.parseLong(a);
            long endTimeStampL = Long.parseLong(b);
            long bias = endTimeStampL - startTimeStampL;
            int totalTimes = bias > 1000 * 60 * 5 ? (int) Math.ceil((float) bias / 1000 * 60 * 5) : 1;
            for (int i = 0; i < totalTimes; i++) {
                startL = (long) i * 1000 * 60 * 5 + startTimeStampL;
                endL = (long) (i + 1) * 1000 * 60 * 5 + startTimeStampL;
                bonusService.executeDailyTask(startL / 1000, endL / 1000, nowL);
            }
        } else {
            bonusService.executeDailyTask(startL / 1000, endL / 1000, nowL);
        }
        return SUCCESS;
    }
}