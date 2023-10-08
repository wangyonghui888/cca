package com.panda.sport.order.schedule;

import com.panda.sport.order.service.UserLogHistoryService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobHandler(value = "UserLoginPreLoadTask")
public class UserLoginPreLoadTask extends IJobHandler {

    @Autowired
    private UserLogHistoryService userLogHistoryService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("UserLoginPreLoadTask用户预加载开始!" + s);
        try {
            userLogHistoryService.preLoadUserLogin(s);
        } catch (Exception e) {
            log.error("UserLoginPreLoadTask异常！", e);
        }
        log.info("UserLoginPreLoadTask结束执行!");
        return SUCCESS;
    }
}