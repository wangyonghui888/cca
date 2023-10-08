package com.panda.sport.order.schedule;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.panda.sport.order.service.MerchantOrderService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@JobHandler(value = "merchantKeyExportTask")
public class MerchantKeyExportTask extends IJobHandler {
    @Autowired
    private MerchantOrderService orderService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<String> codeList = Arrays.asList(s.split(","));
        orderService.exportMerchantKey(codeList);
        return SUCCESS;
    }
}