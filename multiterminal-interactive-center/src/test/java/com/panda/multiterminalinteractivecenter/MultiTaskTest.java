package com.panda.multiterminalinteractivecenter;

import com.panda.multiterminalinteractivecenter.schedule.MaintenanceTask;
import com.panda.multiterminalinteractivecenter.service.DomainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MultiTaskTest {

    @Autowired
    private MaintenanceTask maintenanceTask;
    @Autowired
    private DomainService domainService;


    @Test
    void contextLoads() {
    }


    @Test
    void checkInDomainEnableTask() throws InterruptedException {
        domainService.checkMerchantDomainToggle();
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    void testCloseDJSport() throws InterruptedException {
        maintenanceTask.handleCloseSportTask();
        Thread.sleep(Integer.MAX_VALUE);
    }

}
