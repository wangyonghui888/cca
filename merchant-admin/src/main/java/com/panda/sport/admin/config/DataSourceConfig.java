package com.panda.sport.admin.config;

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

    private static final String Config_DATASOURCE_PREFIX = "spring.datasource.druid.config";

    public static final String Config_DATASOURCE_BEAN_NAME = "config";

    private static final String BACKUP_DATASOURCE_PREFIX = "spring.datasource.druid.backup";

    private static final String BACKUP_83_DATASOURCE_PREFIX = "spring.datasource.druid.backup83";

    public static final String BACKUP_DATASOURCE_BEAN_NAME = "backup";

    public static final String BACKUP_83_DATASOURCE_BEAN_NAME = "backup83";

    @Primary
    @Bean(name = MERCHANT_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = MERCHANT_DATASOURCE_PREFIX)
    public DruidDataSource getMerchant() {
        return new DruidDataSource();
    }


    @Bean(name = BSS_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BSS_DATASOURCE_PREFIX)
    public DruidDataSource getBss() {
        return new DruidDataSource();
    }


    @Bean(name = BACKUP_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BACKUP_DATASOURCE_PREFIX)
    public DruidDataSource getBackup() {
        return new DruidDataSource();
    }

    @Bean(name = BACKUP_83_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BACKUP_83_DATASOURCE_PREFIX)
    public DruidDataSource getBackup83() {
        return new DruidDataSource();
    }

    @Bean(name = Config_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = Config_DATASOURCE_PREFIX)
    public DruidDataSource getConfig() {
        return new DruidDataSource();
    }


}
