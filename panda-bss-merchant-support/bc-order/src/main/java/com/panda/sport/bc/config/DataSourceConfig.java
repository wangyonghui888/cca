package com.panda.sport.bc.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {


    private static final String BC_DATASOURCE_PREFIX = "spring.datasource.druid.bc";

    public static final String BC_DATASOURCE_BEAN_NAME = "bc";

    private static final String BSS_DATASOURCE_PREFIX = "spring.datasource.druid.bss";

    public static final String BSS_DATASOURCE_BEAN_NAME = "bss";

    @Primary
    @Bean(name = BC_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BC_DATASOURCE_PREFIX)
    public DruidDataSource getBc() {
        return new DruidDataSource();
    }


    @Bean(name = BSS_DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = BSS_DATASOURCE_PREFIX)
    public DruidDataSource getBss() {
        return new DruidDataSource();
    }


}
