package com.panda.sport.merchant.manage.schedule;

import com.panda.sport.merchant.manage.service.MerchantService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@JobHandler(value = "MerchantKeyExportTask")
public class MerchantKeyExportTask extends IJobHandler {

    @Autowired
    private MerchantService merchantService;
    //860153,489637,676408,892233,494846
    @Value("${merchant.code:622285}")
    private String merchantCode;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<String> codeList = Arrays.asList(merchantCode.split(","));
        merchantService.exportMerchantKey(codeList);
        return SUCCESS;
    }
}
