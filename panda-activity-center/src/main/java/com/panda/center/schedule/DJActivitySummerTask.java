package com.panda.center.schedule;
import com.panda.center.service.BonusService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobHandler(value = "DJActivitySummerTask")
public class DJActivitySummerTask extends IJobHandler {

    @Autowired
    private BonusService bonusService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        bonusService.executeSumTask();
        return SUCCESS;
    }
}