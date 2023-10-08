package com.panda.sport.order.schedule;

import com.panda.sport.bss.mapper.TUserMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@JobHandler(value = "UserUnActiveTask")
public class UserUnActiveTask extends IJobHandler {


    @Autowired
    private TUserMapper userMapper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("UserUnActiveTask开始!" + s);
        try {
            int num = userMapper.updateUserActive();
            log.info("UserUnActiveTask! num" + num);
        } catch (Exception e) {
            log.error("UserUnActiveTask异常！", e);
        }
        log.info("UserUnActiveTask结束执行!");
        return SUCCESS;
    }
}