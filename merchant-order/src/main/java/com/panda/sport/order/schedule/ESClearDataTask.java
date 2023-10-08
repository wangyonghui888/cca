package com.panda.sport.order.schedule;


import com.panda.sport.order.feign.MerchantReportClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@JobHandler(value = "ESClearDataTask")
public class ESClearDataTask extends IJobHandler {

    @Autowired
    private MerchantReportClient reportClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        reportClient.deleteByQuery(s);
        return SUCCESS;
    }
}