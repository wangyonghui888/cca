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
@JobHandler(value = "userSummaryLibraryDataTask")
public class UserSummaryLibraryDataTask extends IJobHandler {

    @Autowired
    private UserAccountClearDataService userAccountClearDataService;

    @Override
    public ReturnT<String> execute(String num) throws Exception {
        try {
            long startTime = System.currentTimeMillis();
            if (StringUtil.isBlankOrNull(num)) {
                num = "30000";
            }
            XxlJobLogger.log("userSummaryLibraryDataTask任务开始的时间为:" + startTime);
            userAccountClearDataService.userSummaryLibraryDataTask(Integer.valueOf(num));
            XxlJobLogger.log("userSummaryLibraryDataTask任务结束,共消耗时间:" + (System.currentTimeMillis() - startTime));
            return SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log("userSummaryLibraryDataTask异常!", e);
            return IJobHandler.FAIL;
        }
    }

}