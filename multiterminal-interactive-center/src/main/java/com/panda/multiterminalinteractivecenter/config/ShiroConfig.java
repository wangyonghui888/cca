package com.panda.multiterminalinteractivecenter.config;

import com.panda.multiterminalinteractivecenter.auth.AuthFilter;
import com.panda.multiterminalinteractivecenter.auth.ShiroRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.config
 * @Description :  TODO
 * @Date: 2022-03-13 16:16:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Configuration
public class ShiroConfig {

    private boolean auth = true;

    //配置拦截器
    @Bean
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //启用认证
        String openAuth = auth ? "auth" : "anon";

        //自定义过滤器链
        Map<String, javax.servlet.Filter> filters = new HashMap<>();
        //指定拦截器处理
        filters.put("auth", new AuthFilter());
        shiroFilterFactoryBean.setFilters(filters);
        Map<String, String> filterMap = new LinkedHashMap<>();

        //登录请求不拦截
        filterMap.put("/auth/login", "anon");
        filterMap.put("/auth/cleanCache", "anon");
        filterMap.put("/auth/getSecretKey", "anon");
        filterMap.put("/auth/test", "anon");
        filterMap.put("/auth/querySystemInfo", "anon");
        filterMap.put("/maintenanceRecord/queryList", "anon");
        filterMap.put("/merchant/domain/getDomainByMerchantAndArea", "anon");
        filterMap.put("/merchant/domain/getFrontDomainByTerminal", "anon");
        filterMap.put("/multi/api/v1/getDomainByMerchantGroup", "anon");
        filterMap.put("/multi/api/v2/getDomainByMerchantGroup", "anon");
        filterMap.put("/multi/api/test", "anon");
        filterMap.put("/merchant/domain/getNewH5PcDomain", "anon");
        filterMap.put("/merchant/group/getMerchantGroupDomain", "anon");
        filterMap.put("/merchant/group/queryAnimation", "anon");
        filterMap.put("/domain/area/list/simple", "anon");
        filterMap.put("/v2/frontend/domain/queryFrontendMerchantDomain", "anon");
        filterMap.put("/v1/video/domain/queryVideoMerchantDomain", "anon");


        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/v2/**", "anon");
        filterMap.put("/null/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/doc.html*", "anon");
        filterMap.put("//swagger-ui.html*", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/cp/**", "anon");
        //拦截所有接口请求，做权限判断
        filterMap.put("/**", openAuth);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    //自定义身份认证realm
    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    // SecurityManager 安全管理器；Shiro的核心
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }
    @Bean("lifecycleBeanPostProcessor")
    //管理shiro生命周期
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //Shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
