package com.panda.sport.bc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.panda.sport.order*", "com.panda.sport.bc.*"})
@EnableDiscoveryClient
@EnableFeignClients
public class BcApplication {
    public static void main(String[] args) {
        SpringApplication.run(BcApplication.class, args);
    }
}
