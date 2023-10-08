package com.panda.sport.merchant.api.aop;

import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Aspect
public class APILimitAspect {

    @Autowired
    public RedisTemp redisTemp;

    @Around("@annotation(redisAPILimit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, RedisAPILimit redisAPILimit) throws Throwable {
        if (redisAPILimit == null) {
            return proceedingJoinPoint.proceed();
        }
        Integer sec = redisAPILimit.sec();
        String apiKey = redisAPILimit.apiKey();
        boolean comprehensive = redisAPILimit.comprehensive();
        String currentCount;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        log.info("请求路径getRequestURL:" + request.getRequestURL());
        int limit = redisAPILimit.limit();
        if (comprehensive) {
            currentCount = redisTemp.getObject(apiKey);
        } else {
            Object[] paramValues = proceedingJoinPoint.getArgs();
            String[] paramNames = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
            Object pv = null;
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                if (paramName.equalsIgnoreCase("merchantCode")) {
                    pv = paramValues[i];
                    break;
                }
            }
            if (pv == null) {
                log.error("根据商户限流时,无法取到商户编码,无法限流");
                return proceedingJoinPoint.proceed();
            }
            apiKey = apiKey + pv;
            currentCount = redisTemp.getObject(apiKey);
        }
        if (currentCount != null && (Integer.parseInt(currentCount) + 1) > limit) {
            Long expire = redisTemp.getExpire(apiKey, TimeUnit.SECONDS);
            log.info("全局限流{},{}到达限制,查询次数{},限制频次为：{},剩余时间{}秒", comprehensive, apiKey, currentCount, limit, expire);
            return APIResponse.returnFail("限制频次为:" + limit);
        }
        String script = "local count = redis.call('incr',KEYS[1]) if count == 1 then  redis.call('expire',KEYS[1] , " +
                "tonumber(ARGV[1])) end  local ttlTime = redis.call('ttl',KEYS[1]) if ttlTime == -1 then  redis.call('expire'," +
                "KEYS[1] , tonumber(ARGV[1])) end  return count ";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long incNum = redisTemp.execute(redisScript, apiKey, sec + "");
        return proceedingJoinPoint.proceed();
    }
}