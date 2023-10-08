package com.panda.center;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/24 11:34:39
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@EnableFeignClients
@Slf4j
@ComponentScan(basePackages = {"com.panda.center.*","com.panda.sports.auth.*"})
public class ActivityCenterServer {
    public static void main(String[] args) {
        SpringApplication.run(ActivityCenterServer.class, args);
    }
}
