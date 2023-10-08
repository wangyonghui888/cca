package com.panda.center.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/24 11:49:56
 */
@Configuration
public class DataSourceConfig {

    private static final String E_SPORT_ACTIVITY_DATASOURCE_PREFIX = "spring.datasource.druid.esport.activity";
    public static final String E_SPORT_ACTIVITY = "eSportActivity";

    private static final String E_SPORT_TRADER_DATASOURCE_PREFIX = "spring.datasource.druid.esport.trader";
    public static final String E_SPORT_TRADER = "eSportTrader";

    private static final String MERCHANT_DATASOURCE_PREFIX = "spring.datasource.druid.merchant";
    public static final String MERCHANT_DATASOURCE_BEAN_NAME = "merchant";

    // 活动库
    @Primary
    @Bean(name = E_SPORT_ACTIVITY)
    @ConfigurationProperties(prefix = E_SPORT_ACTIVITY_DATASOURCE_PREFIX)
    public DruidDataSource geteSportActivity() {
        return new DruidDataSource();
    }
    // 电竞备份库
    @Bean(name = E_SPORT_TRADER)
    @ConfigurationProperties(prefix = E_SPORT_TRADER_DATASOURCE_PREFIX)
    public DruidDataSource geteSportTrader() {
        return new DruidDataSource();
    }
    // 商户后台库
    @Bean(name = MERCHANT_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = MERCHANT_DATASOURCE_PREFIX)
    public DruidDataSource getMerchant() {
        return new DruidDataSource();
    }
}
