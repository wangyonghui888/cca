package com.panda.sport.merchant.manage.aop;

import com.panda.sport.merchant.common.exception.BusinessException;
import com.panda.sport.merchant.common.exception.SeaMoonVerificationException;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sports.auth.response.ApiResponseEnum;
import com.panda.sports.auth.rpc.IAuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shaun
 * @version v1.0
 * @date 9/8/23 4:14 pm
 * @description Shaun
 */
@Slf4j
@Aspect
@Component
public class SeaMoonValidAspect {

    //口令为空
    final Integer SEA_MOON_BLANK = -1;
    //口令信息错误/已失效
    final Integer SEA_MOON_FAIL = -2;

    @Reference
    private IAuthRequiredPermission iAuthRequiredPermission;

    @Pointcut("@annotation(com.panda.sport.merchant.common.permission.AuthSeaMoonKey)")
    public void seaMoonMethodPointcut() {

    }

    @Around("seaMoonMethodPointcut()")
    public Object validateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = null;
        Object[] args = joinPoint.getArgs();
        Object result = null;
        Object returnType = Response.returnSuccess();
        returnType = Response.returnSuccess();
        // 查找第一个参数中的 HttpServletRequest 对象
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }
        //如果参数中没有HttpServletRequest对象，则从上下文种获取
        if (null == request) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != requestAttributes) {
                request = requestAttributes.getRequest();
            }
        }
        if (null != request) {
            Integer validResult = null;
            try {
                String url = request.getRequestURI();
                Integer userId = SsoUtil.getUserId(request);
                Integer appId = SsoUtil.getAppId(request);
                String seaMoonToken = args.length > 0 ? (null != args[0] ? args[0].toString() : Strings.EMPTY) : Strings.EMPTY;
                ((MethodSignature) joinPoint.getSignature()).getMethod().getReturnType().newInstance();
                //验证SeaMoon口令
                Map<String, Object> validSeaMoonMap = this.validSeaMoon(userId, appId, seaMoonToken, url);
                validResult = (Integer) validSeaMoonMap.get("validResult");
                String validLogMessage = validSeaMoonMap.get("validLogMessage").toString();
                if (SEA_MOON_BLANK.equals(validResult)) {
                    result = BusinessException.responseFail(returnType, ApiResponseEnum.TOKEN_CODE_VALID1.getCode(), ApiResponseEnum.TOKEN_CODE_VALID1.getMessage());
                } else if (SEA_MOON_FAIL.equals(validResult)) {
                    result = BusinessException.responseFail(returnType, ApiResponseEnum.TOKEN_CODE_VALID2.getCode(), ApiResponseEnum.TOKEN_CODE_VALID2.getMessage());
                } else {
                    //验证通过，放行
                    result = joinPoint.proceed();
                }
            } catch (SeaMoonVerificationException e) {
                result = BusinessException.responseFail(returnType, e.getCode(), e.getMessage());
            }
        } else {
            //始终无法HttpServletRequest对象，直接放行
            result = joinPoint.proceed();
        }
        return result;
    }

    private Map<String, Object> validSeaMoon(Integer userId, Integer appId, String token, String url) throws SeaMoonVerificationException {
        Map<String, Object> result = new HashMap<>();
        result.put("validResult", 1);
        result.put("validLogMessage", Strings.EMPTY);
        if (Strings.isBlank(token)) {
            result.put("validResult", SEA_MOON_BLANK);
            result.put("validLogMessage", String.format("用户:%s,接口:%s,动态口令为空", userId, url));
            return result;
        }
        try {
            String userSnInfo = iAuthRequiredPermission.getUserSnInfo(userId, appId);
            boolean validResult = iAuthRequiredPermission.validSeaMoonKey(userId, userSnInfo, token);
            if (!validResult) {
                result.put("validResult", SEA_MOON_FAIL);
                result.put("validLogMessage", String.format("用户:%s,接口:%s,,tokenCode:%s, snInfo:%s,动态口令验证失败", userId, url, token, userSnInfo));
                return result;
            }
        } catch (Exception e) {
            log.info("海月调用权限大后台RPC异常 = {}", e);
            throw new SeaMoonVerificationException(ApiResponseEnum.TOKEN_CODE_VALID3.getCode(), ApiResponseEnum.TOKEN_CODE_VALID3.getMessage());
        }
        return result;
    }
}