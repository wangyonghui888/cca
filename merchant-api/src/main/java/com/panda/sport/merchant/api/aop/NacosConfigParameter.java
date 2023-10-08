package com.panda.sport.merchant.api.aop;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Configuration
@Component
public class NacosConfigParameter {

    @Value("${rate.limit.api_merchant_login_switch:off}")
    private String loginSwitch;

    @Value("${rate.limit.api_merchant_login_range:10}")
    private Integer loginRange;

    @Value("${rate.limit.api_merchant_login_rate:1000}")
    private Integer loginRate;
    /**
     * 111111_100_1;oubao_111_2
     */
    @Value("${rate.limit.api_merchant_login_merchant_rate_range:null}")
    private String merchantLoginRangeRate;


    @Value("${rate.limit.api_merchant_create_switch:off}")
    private String createSwitch;

    @Value("${rate.limit.api_merchant_create_range:10}")
    private Integer createRange;

    @Value("${rate.limit.api_merchant_create_rate:1000}")
    private Integer createRate;
    /**
     * 111111_100_1;oubao_111_2
     */
    @Value("${rate.limit.api_merchant_create_rate_range:null}")
    private String merchantCreateRangeRate;


}
