package com.panda.multiterminalinteractivecenter.exception;

import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.exception
 * @Description :  TODO
 * @Date: 2022-03-14 15:44:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@ControllerAdvice
public class NoPermissionException {

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public APIResponse<?> handleBusinessException(Exception ex) {
       return APIResponse.returnFail(ex.getMessage());
    }

    /**
     * 用户角色权限认证 当没有权限时会报UnauthorizedException异常 此处处理异常给前端返回提示语
     * @param response
     * @param ex
     * @throws IOException
     */
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public void handleShiroException(HttpServletResponse response, Exception ex) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        //ReturnMessage returnMessage = ReturnMessage.failWithMsg(AuthConstant.AUTHENTICATE_FAIL);
        httpServletResponse.getWriter().print(JSONObject.toJSONString(APIResponse.returnFail("访问了无权限目录")));
    }
    //执行认证逻辑认证不通过时会走  过滤器AuthFilter中的onAccessDenied方法 所以这个异常不捕获
   /* @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception ex) {
        return "权限认证失败";
    }*/
}