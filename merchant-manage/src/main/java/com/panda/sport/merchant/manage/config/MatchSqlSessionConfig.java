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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.panda.sport.match.mapper", sqlSessionTemplateRef = "matchSqlSessionTemplate")
public class MatchSqlSessionConfig {


    @Autowired
    @Qualifier(DataSourceConfig.BSS_MATCH_DATASOURCE_BEAN_NAME)
    private DataSource match;

    @Bean("matchSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();

        factoryBean.setDataSource(match);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:match/*.xml"));
        return factoryBean.getObject();
    }

    @Bean("matchSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(getSqlSessionFactory());
        return template;
    }

}
