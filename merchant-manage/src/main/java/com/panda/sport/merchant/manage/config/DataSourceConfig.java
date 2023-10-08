package com.panda.sport.merchant.manage.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    private static final String MERCHANT_DATASOURCE_PREFIX = "spring.datasource.druid.merchant";

    public static final String MERCHANT_DATASOURCE_BEAN_NAME = "merchant";

    private static final String BSS_DATASOURCE_PREFIX = "spring.datasource.druid.bss";

    public static final String BSS_DATASOURCE_BEAN_NAME = "bss";

    private static final String BSS_BACKUP_DATASOURCE_PREFIX = "spring.datasource.druid.bss.backup";

    public static final String BSS_BACKUP_DATASOURCE_BEAN_NAME = "backup";

    private static final String BSS_MATCH_DATASOURCE_PREFIX = "spring.datasource.druid.bss.match";

    public static final String BSS_MATCH_DATASOURCE_BEAN_NAME = "match";

    private static final String E_SPORT_TRADER_DATASOURCE_PREFIX = "spring.datasource.druid.esport.trader";

    public static final String E_SPORT_TRADER_NAME = "eSportTrader";

    private static final String E_CP_DATASOURCE_PREFIX = "spring.datasource.druid.esport.cp";

    public static final String E_SPORT_CP_NAME = "eSportCp";


    // 商户后台库
    @Primary
    @Bean(name = MERCHANT_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = MERCHANT_DATASOURCE_PREFIX)
    public DruidDataSource getMerchant() {
        return new DruidDataSource();
    }

    // 商户订单库
    @Bean(name = BSS_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BSS_DATASOURCE_PREFIX)
    public DruidDataSource getBss() {
        return new DruidDataSource();
    }

    // 备份库（汇总库）
    @Bean(name = BSS_BACKUP_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BSS_BACKUP_DATASOURCE_PREFIX)
    public DruidDataSource getBackup() {
        return new DruidDataSource();
    }

    // 赛事基础库
    @Bean(name = BSS_MATCH_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BSS_MATCH_DATASOURCE_PREFIX)
    public DruidDataSource getMatch() {
        return new DruidDataSource();
    }

    // 电竞库
    @Bean(name = E_SPORT_TRADER_NAME)
    @ConfigurationProperties(prefix = E_SPORT_TRADER_DATASOURCE_PREFIX)
    public DruidDataSource geTrader() {
        return new DruidDataSource();
    }

    // 彩票库
    @Bean(name = E_SPORT_CP_NAME)
    @ConfigurationProperties(prefix = E_CP_DATASOURCE_PREFIX)
    public DruidDataSource geCp() {
        return new DruidDataSource();
    }

}
