package com.panda.sport.merchant.api.aop;

import com.panda.sport.merchant.api.config.DataSourceType;
import com.panda.sport.merchant.api.config.DynamicDataSourceContextHolder;
import com.panda.sport.merchant.api.config.MerchantGroupConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.panda.sport.merchant.api.config.DataSourceConstant.dataSourceList;

@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect {

    @Pointcut("execution(* com.panda.sport.merchant.api.controller..*(..))||@annotation(com.panda.sport.merchant.api.aop.DataSource)")
    //@Pointcut("@annotation(com.panda.sport.merchant.api.aop.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();
        String dataSourceStr;
        DataSource dataSource = method.getAnnotation(DataSource.class);
        String methodName = method.getName();
        if (dataSource != null) {
            dataSourceStr = dataSource.value().name();
            log.info("1注解:切换数据源为{}", dataSourceStr);
            DynamicDataSourceContextHolder.setDateSoureType(dataSourceStr);
        } else {
            Object[] paramValues = joinPoint.getArgs();
            String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            String pv = null;
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                if (paramName.equalsIgnoreCase("merchantCode")) {
                    pv = (String) paramValues[i];
                    break;
                }
            }
            String group = MerchantGroupConfig.groupMap.get(pv);
            if (pv == null || StringUtils.isEmpty(group)) {
                dataSourceStr = DataSourceType.common.name();
                DynamicDataSourceContextHolder.setDateSoureType(DataSourceType.common.name());
            } else {
                if (!dataSourceList.contains(group)) {
                    log.error(pv + "此商户配置数据库错误," + group);
                    throw new Exception("此商户配置错误!");
                }
                dataSourceStr = group;
                //log.info("2参数:切换数据源为{}", dataSourceStr);
                DynamicDataSourceContextHolder.setDateSoureType(group);
            }
        }
        try {
            return joinPoint.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDateSoureType();
        }
    }

    /**
     * 根据类或方法获取数据源注解
     */
    private DataSource getDSAnnotation(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        DataSource dsAnnotation = targetClass.getAnnotation(DataSource.class);
        // 先判断类的注解，再判断方法注解
        if (Objects.nonNull(dsAnnotation)) {
            return dsAnnotation;
        } else {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            return methodSignature.getMethod().getAnnotation(DataSource.class);
        }
    }
}
