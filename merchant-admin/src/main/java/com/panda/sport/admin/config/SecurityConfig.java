package com.panda.sport.admin.config;

import com.panda.sport.admin.security.JwtAuthenticationEntryPoint;
import com.panda.sport.admin.security.JwtAuthorizationTokenFilter;
import com.panda.sport.admin.service.impl.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @auth: YK
 * @Description:安全配置类
 * @Date:2020/5/10 17:21
 */
@Configuration
@EnableWebSecurity
// 默认是禁用注解的，要想开启注解， 需要在继承，来判断用户对某个控制层的方法是否具有访问权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    /**
     * 自定义基于JWT的安全过滤器
     */
    @Autowired
    private JwtAuthorizationTokenFilter authenticationTokenFilter;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    // Remove the ROLE_ prefix
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }


    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

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
                .antMatchers(HttpMethod.POST, "/admin/auth/login").permitAll()
                .antMatchers(HttpMethod.GET, "//amountGrowthRateTop10").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/merchantReport/merchantFileExport").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/account/transferRecordExport").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/account/accountHistoryExport").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/userReport/exportUserOrderStatisticList").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/userReport/exportTicketAccountHistoryList").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/file/export").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/userReport/exportUserOrderReport").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/userReport/exportUserOrder").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/match/exportMatchStatistic").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/financeMonth/financeDayExport").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/merchant/file/download").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/userReport/exportTicketList").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/match//exportMatchPlayStatistic").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/match/getQueryTournamentUrl").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/merchantReport/amountGrowthRateTop10").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/merchantReport/merchantFileExport").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/account/transferRecordExport").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/account/accountHistoryExport").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/userReport/exportUserOrderStatisticList").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/userReport/exportTicketAccountHistoryList").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/file/export").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/userReport/exportUserOrderReport").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/userReport/exportUserOrder").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/match/exportMatchStatistic").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/financeMonth/financeDayExport").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/merchant/file/download").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/userReport/exportTicketList").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/match/exportMatchPlayStatistic").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/cache/getLocalCacheInfo").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/cache/invalidateCache").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/cache/invalidateAll").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/userRiskControl/exportUserRiskControlList").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/userRiskControl/importUserRiskControlList").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/userRiskControl/exportUserRiskControlList").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/userRiskControl/importUserRiskControlList").permitAll()
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
