package com.panda.sport.merchant.api.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisAPILimit {

    //限制数量
    int limit() default 5;

    //标识 时间段 5秒
    int sec() default 5;

    String apiKey() default "";

    //是否全局拦截(否,根据商户拦截)
    boolean comprehensive() default true;
}