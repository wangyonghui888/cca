package com.panda.sport.admin.security;

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
 * @auth: YK
 * @Description:AccessDeniedHandler
 * @Date:2020/7/4 13:57
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        response.setContentType("application/json; charset=utf-8");
        String ex = JSON.toJSONString(APIResponse.returnFail(ResponseEnum.PERMISSION_ERROR.getId(), "token is not permissions"));
        response.getWriter().write(ex);
    }


}
