package com.panda.multiterminalinteractivecenter;

import com.panda.multiterminalinteractivecenter.service.DomainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MultiterminalInteractiveCenterApplicationTests {

    @Autowired
    private DomainService domainService;


    @Test
    void contextLoads() {
    }


    @Test
    void testCheckDomainValidTask() throws InterruptedException {

        domainService.checkMerchantDomainToggle();
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    void testCheckDomainValid() throws InterruptedException {
       domainService.checkDomainValid();
        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    void testCheckDomainThreshold() throws InterruptedException {

        domainService.checkDomainThreshold();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
