package com.panda.sport.merchant.api.aop;

import com.panda.sport.merchant.api.config.DataSourceType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    /**
     * 切换数据源名称.
     */
     DataSourceType value() default DataSourceType.common;
}

