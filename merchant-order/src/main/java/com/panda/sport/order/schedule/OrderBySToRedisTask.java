package com.panda.sport.order.schedule;

import com.panda.sport.order.service.UserOrderService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * S组 商户下用户 主动拉取注单 载入单独的缓存中
 */

@Slf4j
@Component
@JobHandler(value = "OrderBySToRedisTask")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderBySToRedisTask extends IJobHandler {

    private final UserOrderService userOrderService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        try {
            printLog("OrderBySToRedisTask--主动拉取组下用户注单 定时任务开始");
            userOrderService.execOrderBySToRedisTask(param);
            printLog("OrderBySToRedisTask--主动拉取组下用户注单 定时任务结束");
            return SUCCESS;
        }catch (Exception e){
            log.error("OrderBySToRedisTask--主动拉取组下用户注单 定时任务异常",e);
            return FAIL;
        }
    }

    private void printLog(Object... str){
        String logs = str[0].toString();
        int time = 1;
        while (logs.contains("{}")) {
            logs = logs.replaceFirst("\\{}", str[time++].toString());
        }
        XxlJobLogger.log(logs);
        log.info(logs);
    }


}
