package com.panda.multiterminalinteractivecenter.auth;

import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.auth
 * @Description :  TODO
 * @Date: 2022-03-13 16:55:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Component
public class AuthFilter extends AuthenticatingFilter {

    private APIResponse responseResult = APIResponse.returnFail(AuthConstant.AUTHENTICATE_FAIL);

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        return null;
    }

    /**
     * 在这里拦截所有请求
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String token = JWTUtil.getRequestToken((HttpServletRequest)request);

        if (!StringUtils.isBlank(token)){
            try {
                this.executeLogin(request, response);
            } catch (Exception e) {
                // 应用异常
                responseResult = APIResponse.returnFail(e.getMessage());
                return false;
            }
        } else {
            // cookie中未检查到token或token为空
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            String httpMethod = httpServletRequest.getMethod();
            String requestURI = httpServletRequest.getRequestURI();
            responseResult = APIResponse.returnFail(ApiResponseEnum.TOKEN_IS_EXPIRED);
            log.info("请求 {} 的Token为空 请求类型 {}", requestURI, httpMethod);
            return false;
        }
        return true;
    }

    /**
     * 请求失败拦截,请求终止，不进行转发直接返回客户端拦截结果
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception{
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //ReturnMessage returnMessage = ReturnMessage.failWithMsg(AuthConstant.AUTHENTICATE_FAIL);

        APIResponse apiResponse = APIResponse.returnFail(ApiResponseEnum.TOKEN_IS_EXPIRED);
        httpServletResponse.getWriter().print(JSONObject.toJSONString(apiResponse));
        return false;
    }

    /**
     * 用户存在，执行登录认证
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String token = JWTUtil.getRequestToken((HttpServletRequest)request);
        AuthTokenVo jwtToken = new AuthTokenVo(token);
        // 提交给AuthRealm进行登录认证
        getSubject(request, response).login(jwtToken);
        return true;
    }
}
