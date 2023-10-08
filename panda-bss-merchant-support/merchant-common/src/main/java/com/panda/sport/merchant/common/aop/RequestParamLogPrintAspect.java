package com.panda.sport.merchant.common.aop;


import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.exception.BusinessException;
import com.panda.sport.merchant.common.exception.SeaMoonVerificationException;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
@Component
@Slf4j
public class RequestParamLogPrintAspect {

    private static final String LOG_ADD = "add";
    private static final String LOG_SAVE = "save";
    private static final String LOG_UPDATE = "update";
    private static final String LOG_DELETE = "delete";

    /**
     * controller dml操作打印入参
     *
     * @param point
     */
    @Around("execution(* com.panda..*.controller..*(..))")
    public Object Interceptor(ProceedingJoinPoint point) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Object result;
        Object[] params = point.getArgs();
        StringBuffer requestUrl = request.getRequestURL();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Object responseObj = Response.returnSuccess();
        try {
            if (method.getName().contains(LOG_SAVE) || method.getName().contains(LOG_ADD) || method.getName().contains(LOG_UPDATE) || method.getName().contains(LOG_DELETE)) {
                log.info("请求路径RequestURL: {} \n params：{}", requestUrl, params);
            }
            result = point.proceed();
            try {
                responseObj = ((MethodSignature) point.getSignature()).getMethod().getReturnType().newInstance();
            } catch (Exception e) {
            }
        } catch (SeaMoonVerificationException e) {
            result = BusinessException.responseFail(responseObj, e.getCode(), e.getMessage());
        } catch (Throwable e) {
            log.error(requestUrl + "exception: ", e);
            result = BusinessException.responseFail(responseObj, ApiResponseEnum.INTERNAL_ERROR.getId(), ApiResponseEnum.INTERNAL_ERROR.getLabel());
        }
        return result;
    }

}
