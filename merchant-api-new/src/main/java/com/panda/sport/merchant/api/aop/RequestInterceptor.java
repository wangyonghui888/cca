package com.panda.sport.merchant.api.aop;


import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 拦截器：验签 会话管理
 *
 * @author valar so handsome
 */
@Aspect
@Component
@Slf4j
public class RequestInterceptor {

    @Autowired
    public RedisTemp redisTemp;

    @Resource
    public NacosConfigParameter nacosConfigParameter;

    @Pointcut("execution(* com.panda.sport.merchant.api.controller..*(..))")
    public void controllerMethodPointcut() {

    }

    /**
     * 拦截器具体实现
     *
     * @param proceedingJoinPoint
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("controllerMethodPointcut()") //指定拦截器规则
    public Object Interceptor(ProceedingJoinPoint proceedingJoinPoint) {
        long beginTime = System.currentTimeMillis();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Object result = null;
        StringBuffer requestUrl = request.getRequestURL();
        log.info("请求路径getRequestURL:" + request.getRequestURL());
        
        try {
        	Object[] args = proceedingJoinPoint.getArgs();
            String[] pNames = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
            String merchantCode = null;
            String userName = null;
            for (int i = 0; i < pNames.length; i++) {
                String paramName = pNames[i];
                if (paramName.equalsIgnoreCase("merchantCode")) {
                	merchantCode = (String) args[i];
                }
                if (paramName.equalsIgnoreCase("userName")) {
                	userName = (String) args[i];
                }
            }
            if(StringUtils.isNotBlank(merchantCode))
            	if(RequestLimitHand.requestLimit(request.getRequestURI(), merchantCode,userName))  return APIResponse.returnFail(ApiResponseEnum.RATE_LIMIT_ERROR);
        	
            // 一切正常的情况下，继续执行被拦截的方法
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error(requestUrl + "exception: ", e);
            result = APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        } finally {
            if (result != null) {
                long endTime = System.currentTimeMillis();
                long costMs = endTime - beginTime;
                boolean timeout = costMs > 2000;
                //API名称+请求时间+响应时间+总共耗时ms
                String costLog = String.format("%s %s %s %s cost:%sms timeout:%s", "业务API监控", requestUrl, beginTime, endTime, costMs, timeout);
                log.info(costLog);
            }
        }
        return result;
    }

}
