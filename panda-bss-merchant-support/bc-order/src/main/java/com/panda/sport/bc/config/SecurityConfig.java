package com.panda.sport.bc.config;

import com.panda.sport.bc.security.JwtAuthenticationEntryPoint;
import com.panda.sport.bc.security.JwtAuthorizationTokenFilter;
import com.panda.sport.bc.service.impl.BcJwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @ClassName SecurityConfig
 * @auth YK
 * @Description : Security 的安全路由配置
 * @Date 2020-09-01 11:28
 * @Version
 */
@Configuration
@EnableWebSecurity
// 默认是禁用注解的，要想开启注解， 需要在继承，来判断用户对某个控制层的方法是否具有访问权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private BcJwtUserDetailsService bcJwtUserDetailsService;

    /**
     * 自定义基于JWT的安全过滤器
     */
    @Autowired
    private JwtAuthorizationTokenFilter authenticationTokenFilter;


    @Value("${jwt.header}")
    private String tokenHeader;


    /**
     * @description: 初始化配置
     * @Param: [auth]
     * @return: void
     * @author: YK
     * @date: 2020/9/11 11:56
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(bcJwtUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    /**
     * @description: 去除Role_
     * @Param: []
     * @return: org.springframework.security.config.core.GrantedAuthorityDefaults
     * @author: YK
     * @date: 2020/9/11 11:55
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }


    /**
     * @description: 加密方法
     * @Param: []
     * @return: org.springframework.security.crypto.password.PasswordEncoder
     * @author: YK
     * @date: 2020/9/11 11:56
     */
    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @description: 初始化
     * @Param: []
     * @return: org.springframework.security.authentication.AuthenticationManager
     * @author: YK
     * @date: 2020/9/11 11:56
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * @description: 配置路由
     * @Param: [httpSecurity]
     * @return: void
     * @author: YK
     * @date: 2020/9/11 11:57
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // 禁用 CSRF
                .csrf().disable()
                // 授权异常
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                // 不创建会话
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers(HttpMethod.POST, "/bc/auth/login").permitAll()
                .antMatchers(HttpMethod.GET, "/bc/order/exportTicketList").permitAll()
                // 接口限流测试
                .antMatchers("/test/**").anonymous()
                .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()

                .antMatchers("/druid/**").permitAll()
                // 所有请求都需要认证
                .anyRequest().authenticated()
                // 防止iframe 造成跨域
                .and().headers().frameOptions().disable()
        ;

        httpSecurity
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);


    }

}
