package com.panda.multiterminalinteractivecenter.schedule;

import com.panda.multiterminalinteractivecenter.service.DomainService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 检测域名池里面所有的域名(失效,正在使用,可用的) (每天一次)
 */
@Slf4j
@Component
@JobHandler(value = "checkDomainValidTask")
public class CheckDomainValidTask extends IJobHandler {

    @Autowired
    private DomainService domainService;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            log.info("checkDomainValidTask.检测域名池里面所有的域名开始");
            domainService.checkDomainValid();
            log.info("checkDomainValidTask.检测域名池里面所有的域名结束");
        } catch (Exception e) {
            log.error("checkDomainValidTask.checkDomainValid异常!", e);
            return IJobHandler.FAIL;
        }
        return IJobHandler.SUCCESS;
    }
}
