package com.panda.multiterminalinteractivecenter.schedule;

import com.panda.multiterminalinteractivecenter.service.DomainService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 检测每个域名组的 可用域名数量 是否足够并告警 (每10分钟一次)
 */
@Slf4j
@Component
@JobHandler(value = "checkDomainThresholdTask")
public class CheckDomainThresholdTask extends IJobHandler {

    @Autowired
    private DomainService domainService;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            log.info("检测每个域名组的 可用域名数量 是否足够并告警 开始");
            // domainService.checkDomainThreshold();
            log.info("检测每个域名组的 可用域名数量 是否足够并告警 结束");
        } catch (Exception e) {
            log.error("checkDomainThreshold", e);
            return IJobHandler.FAIL;
        }
        return IJobHandler.SUCCESS;
    }
}
