package com.panda.sport.bc.security;

import com.alibaba.fastjson.JSON;
import com.panda.sport.bc.utils.JwtTokenUtil;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JwtAuthorizationTokenFilter
 * @auth YK
 * @Description : jwt过滤器
 * @Date 2020-09-01 12:02
 * @Version
 */
@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    private final String tokenHeader;

    /**
     * @description:构造函数
     * @Param: [userDetailsService, jwtTokenUtil, tokenHeader]
     * @return:
     * @author: YK
     * @date: 2020/9/11 12:10
     */
    public JwtAuthorizationTokenFilter(@Qualifier("bcJwtUserDetailsService") UserDetailsService userDetailsService,
                                       JwtTokenUtil jwtTokenUtil, @Value("${jwt.header}") String tokenHeader) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
    }

    /**
     * @description: 进行拦截验证
     * @Param: [request, response, chain]
     * @return: void
     * @author: YK
     * @date: 2020/9/11 12:08
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String requestHeader = request.getHeader(this.tokenHeader);

        String username = null;

        String authToken = null;

        if (requestHeader != null) {
            authToken = requestHeader;
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (Exception e) {
                log.warn("异常!token 过期");
                String ex = JSON.toJSONString(APIResponse.returnFail(ResponseEnum.TOKEN_IS_EXPIRED.getId(), "token is expired"));
                response.getWriter().write(ex);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(authToken)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                String ex = JSON.toJSONString(APIResponse.returnFail(ResponseEnum.TOKEN_IS_EXPIRED.getId(), "token is expired"));
                response.getWriter().write(ex);
                return;
            }
        }
        chain.doFilter(request, response);

    }
}
