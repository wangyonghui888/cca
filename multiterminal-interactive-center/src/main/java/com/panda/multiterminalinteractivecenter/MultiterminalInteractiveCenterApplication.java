package com.panda.multiterminalinteractivecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Administrator
 */
@SpringBootApplication
//启用服务发现注解
@EnableDiscoveryClient
//Feign开启注解，basePackages和value功能相同，随意一个都可以。里面是feign扫描包路径
@EnableFeignClients
@EnableScheduling
public class MultiterminalInteractiveCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiterminalInteractiveCenterApplication.class, args);
    }

}
