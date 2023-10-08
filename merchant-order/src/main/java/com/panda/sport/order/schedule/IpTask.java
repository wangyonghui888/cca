package com.panda.sport.order.schedule;

import com.panda.sport.order.service.UserLogHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class IpTask {
    @Autowired
    private UserLogHistoryService userService;

    //@Scheduled(cron = "0 0/2 0/1 * * ? ")
    //"0 0 * * *  ?"
    //@Scheduled(cron = "0 0 * * *  ?")
    public void execute() {
        log.info("开始执行采集用户IP" + new Date());
        userService.collectIp();
        log.info("执行采集用户IP end" + new Date());
    }
}