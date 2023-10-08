package com.panda.multiterminalinteractivecenter.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.config
 * @Description :  TODO
 * @Date: 2022-02-19 11:20:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Configuration
@MapperScan(basePackages = MerchantDruidConfig.PACKAGE, sqlSessionTemplateRef = "merchantSqlSessionTemplate")
public class MerchantDruidConfig {

    static final String PACKAGE = "com.panda.multiterminalinteractivecenter.mapper";

    static final String MAPPER_LOCATION = "classpath*:xml/*.xml";

    @Autowired
    @Qualifier(DruidConfig.MERCHANT_DATASOURCE_BEAN_NAME)
    private DataSource merchant;

    @Bean("merchantSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(merchant);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MerchantDruidConfig.MAPPER_LOCATION));
        return factoryBean.getObject();
    }

    @Bean("merchantSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(getSqlSessionFactory());
        return template;
    }


}
