package com.panda.sport.merchant.manage.controller;

import com.panda.sport.merchant.manage.service.MerchantGroupService;
import com.panda.sport.merchant.manage.service.impl.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ManageFeignController
 *
 * 商户feign接口
 */
@RestController
@RequestMapping
@Slf4j
public class ManageFeignController {


    @Autowired
    private MerchantGroupService merchantGroupService;

    @Autowired
    private WebSocketService webSocketService;


    @GetMapping("/manage/task/changDomainEnable")
    public void changDomainEnable(){

        merchantGroupService.changDomainEnable();
    }

    @GetMapping("/manage/task/changDomainEnableThird")
    void changDomainEnableThird(){
        merchantGroupService.changDomainEnableThird();
    }

    @GetMapping("/manage/task/autoChangeDomain")
    void autoChangeDomain(){
        merchantGroupService.autoChangeDomain();
    }

    @GetMapping("/manage/task/autoChangeDomainThird")
    void autoChangeDomainThird(){
        merchantGroupService.autoChangeDomainThird();
    }

    @GetMapping("/manage/task/checkDomain")
    void checkDomain() throws InterruptedException {
        webSocketService.checkDomain();
    }

    @GetMapping("/manage/task/checkDomainThird")
    void checkDomainThird(){
        webSocketService.checkDomainThird();
    }

    @GetMapping("/manage/task/checkInDomainEnable")
    void checkInDomainEnable() throws InterruptedException {
        webSocketService.checkInDomainEnable();

    }

}
