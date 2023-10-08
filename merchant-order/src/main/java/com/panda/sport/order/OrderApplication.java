package com.panda.sport.order;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@MapperScan(basePackages = {"com.panda.sport.merchant.mapper", "com.panda.sport.bss.mapper", "com.panda.sport.backup.mapper"})
@ComponentScan(basePackages = {"com.panda.sport*"})
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
@EnableCaching
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
