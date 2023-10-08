package com.panda.sport.merchant.manage.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/24 12:03:17
 */
@Configuration
@MapperScan(basePackages = "com.panda.sport.trader.mapper", sqlSessionTemplateRef = "eSportTraderSessionTemplate")
public class ESportTraderSessionConfig {

    @Autowired
    @Qualifier(DataSourceConfig.E_SPORT_TRADER_NAME)
    private DataSource eSportTrader;

    @Primary
    @Bean("eSportTraderSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(eSportTrader);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:trader/*.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean("eSportTraderSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(getSqlSessionFactory());
    }
}
