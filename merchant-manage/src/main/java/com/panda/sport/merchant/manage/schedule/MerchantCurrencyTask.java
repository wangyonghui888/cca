package com.panda.sport.merchant.manage.schedule;

import com.panda.sport.merchant.manage.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MerchantCurrencyTask{

    @Autowired
    private MerchantService merchantService;

    @Scheduled(cron = "0 0 01 * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute(){
        log.info("MerchantCurrencyTask开始执行----");
        merchantService.executeMerchantCurrency();
    }
}
