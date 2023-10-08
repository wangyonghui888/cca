package com.panda.multiterminalinteractivecenter.schedule;

import com.panda.multiterminalinteractivecenter.service.DomainService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * 域名自检切换，核心逻辑 (每min)
 */
@Slf4j
@Component
@JobHandler(value = "checkInDomainEnableTask")
public class CheckInDomainEnableTask extends IJobHandler {

    @Autowired
    private DomainService domainService;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            log.info("checkInDomainEnableTask.checkMerchantDomainToggle,域名自检切换,任务开始");
            domainService.checkMerchantDomainToggle();

            log.info("checkInDomainEnableTask.checkMerchantDomainToggle,域名自检切换,任务结束");
        } catch (Exception e) {
            log.error("checkInDomainEnableTask.checkMerchantUse 异常!", e);
            return IJobHandler.FAIL;
        }
        return IJobHandler.SUCCESS;
    }
}
