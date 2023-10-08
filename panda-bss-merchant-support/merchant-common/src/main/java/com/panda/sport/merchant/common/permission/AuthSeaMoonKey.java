package com.panda.sport.merchant.common.permission;

import java.lang.annotation.*;

/**
 * @author Shaun
 * @version v1.0
 * @date 2023/8/30 14:32
 * @description 验证海月动态密令
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthSeaMoonKey {
    String value();
}