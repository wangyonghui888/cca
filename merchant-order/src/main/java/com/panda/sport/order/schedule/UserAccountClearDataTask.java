package com.panda.sport.order.schedule;


import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.order.service.UserAccountClearDataService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobHandler(value = "UserAccountClearDataTask")
public class UserAccountClearDataTask extends IJobHandler {

    @Autowired
    private UserAccountClearDataService userAccountClearDataService;

    @Override
    public ReturnT<String> execute(String num) throws Exception {
        try {
            long startTime = System.currentTimeMillis();
            if (StringUtil.isBlankOrNull(num)) {
                num = "50000";
            }
            XxlJobLogger.log("UserAccountClearDataTask任务开始的时间为:" + startTime);
            userAccountClearDataService.userAccountClearDataTask(Integer.valueOf(num));
            XxlJobLogger.log("UserAccountClearDataTask任务结束,共消耗时间:" + (System.currentTimeMillis() - startTime));
            return SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log("UserAccountClearDataTask异常!", e);
            return IJobHandler.FAIL;
        }
    }

}