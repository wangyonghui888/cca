package com.panda.multiterminalinteractivecenter.schedule;

import com.panda.multiterminalinteractivecenter.service.DomainService;
import com.panda.multiterminalinteractivecenter.service.OssService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * 检测oss域名
 */
@Slf4j
@Component
@JobHandler(value = "CheckInOssDomainTask")
public class CheckInOssDomainTask extends IJobHandler {

    @Autowired
    private OssService ossService;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            long startTime = System.currentTimeMillis();
            log.info("CheckInOssDomainTask任务开始");
            XxlJobLogger.log("CheckInOssDomainTask任务开始的时间为:" + startTime);
            ossService.checkOssDomain(s);
            log.info("CheckInOssDomainTask任务结束");
            XxlJobLogger.log("CheckInOssDomainTask任务结束,共消耗时间:" + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            log.error("CheckInOssDomainTask.checkOssDomain异常!", e);
            XxlJobLogger.log("CheckInOssDomainTask.checkOssDomain异常!", e);
            return IJobHandler.FAIL;
        }
        return IJobHandler.SUCCESS;
    }
}
