package com.panda.sport.admin.config;

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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.panda.sport.bss.mapper", sqlSessionTemplateRef = "bssSqlSessionTemplate")
public class BSSSqlSessionConfig {


    @Autowired
    @Qualifier(DataSourceConfig.BSS_DATASOURCE_BEAN_NAME)
    private DataSource bss;

    @Primary
    @Bean("bssSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(bss);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:bss/*.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean("bssSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(getSqlSessionFactory());
    }

}
