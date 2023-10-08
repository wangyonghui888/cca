package com.panda.sport.merchant.api.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@MapperScan(basePackages = "com.panda.sport.bss.mapper")
public class DruidConfig {

    @Bean(name = "commonDataSource")
    @ConfigurationProperties("spring.datasource.druid.merchant.common")
    public DataSource commonDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "yDataSource")
    @ConfigurationProperties("spring.datasource.druid.merchant.y")
    //@ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource yDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "sDataSource")
    @ConfigurationProperties("spring.datasource.druid.merchant.s")
    public DataSource sDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "bDataSource")
    @ConfigurationProperties("spring.datasource.druid.merchant.b")
    public DataSource bDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(@Qualifier("commonDataSource") DataSource commonDataSource, @Qualifier("yDataSource") DataSource yDataSource,
                                        @Qualifier("sDataSource") DataSource sDataSource, @Qualifier("bDataSource") DataSource bDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.common.name(), commonDataSource);
        targetDataSources.put(DataSourceType.y.name(), yDataSource);
        targetDataSources.put(DataSourceType.s.name(), sDataSource);
        targetDataSources.put(DataSourceType.b.name(), bDataSource);
        return new DynamicDataSource(commonDataSource, targetDataSources);
    }

    @Primary
    @Bean("sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:bss/*.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    /**
     * Transaction Manager
     *
     * @param dynamicDataSource
     * @return DataSourceTransactionManager
     */
    @Bean(name = DataSourceConstant.MERCHANT_MASTER_TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }
}
