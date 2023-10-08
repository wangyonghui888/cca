package com.panda.sport.bc.security;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CustomAccessDeniedHandler
 * @auth YK
 * @Description: 不允许访问的提示和json输出
 * @Date 2020-09-01 11:39
 * @Version
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
    * @description: 没有权限返回路径的返回
    * @Param: [request, response, e]
    * @return: void
    * @author: YK
    * @date: 2020/9/11 12:07
    */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        response.setContentType("application/json; charset=utf-8");
        String ex = JSON.toJSONString(APIResponse.returnFail(ResponseEnum.PERMISSION_ERROR.getId(), "token is not permissions"));
        response.getWriter().write(ex);
    }
}
