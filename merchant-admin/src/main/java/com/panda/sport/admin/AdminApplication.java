package com.panda.sport.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @auth: YK
 * @Description:初始入口类
 * @Date:2020/5/10 11:37
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.panda.*"})
//启用服务发现注解
@EnableDiscoveryClient
//Feign开启注解，basePackages和value功能相同，随意一个都可以。里面是feign扫描包路径
@EnableFeignClients
@EnableAsync
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
