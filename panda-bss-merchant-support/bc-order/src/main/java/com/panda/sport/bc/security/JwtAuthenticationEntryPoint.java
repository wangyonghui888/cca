package com.panda.sport.bc.security;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @ClassName JwtAuthenticationEntryPoint
 * @auth YK
 * @Description 用来解决匿名用户访问无权限资源时的异常，AuthenticationEntryPoint的实现类:
 * @Date 2020-09-01 11:47
 * @Version
 */
@Component
public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint, Serializable {


    /**
    * @description:用来解决匿名用户访问无权限资源时的异常
    * @Param: [request, response, authException]
    * @return: void
    * @author: YK
    * @date: 2020/9/11 12:08
    */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(APIResponse.returnFail(ResponseEnum.TOKEN_IS_REQUIRED.getId(),
                authException == null ? "Unauthorized" : authException.getMessage())));
    }
}
