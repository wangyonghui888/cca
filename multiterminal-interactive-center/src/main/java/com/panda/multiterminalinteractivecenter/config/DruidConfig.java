package com.panda.multiterminalinteractivecenter.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.config
 * @Description :  TODO
 * @Date: 2022-02-19 14:12:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Configuration
public class DruidConfig {

    private static final String MERCHANT_DATASOURCE_PREFIX = "spring.datasource.druid.merchant";

    public static final String MERCHANT_DATASOURCE_BEAN_NAME = "merchant";

    // 商户后台库
    @Primary
    @Bean(name = MERCHANT_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = MERCHANT_DATASOURCE_PREFIX)
    public DruidDataSource getMerchant() {
        return new DruidDataSource();
    }
}
