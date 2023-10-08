package com.panda.sport.merchant.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Joken
 * @Description: 字段解释说明
 * @Date: 2019/10/10 11:04
 * @Param:
 * @Return:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldExplain {

    String value();
}
