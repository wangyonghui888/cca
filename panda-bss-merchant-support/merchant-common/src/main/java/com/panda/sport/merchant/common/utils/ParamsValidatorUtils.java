package com.panda.sport.merchant.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ParamsValidatorUtils implements SmartValidator {

    private javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        // 这里面为验证实现，可以根据自己的需要进行完善与修改
        if (target == null) {
            throw new IllegalArgumentException("参数不能为空！");
        } else {
            if (target instanceof String) {
                if (StringUtils.isEmpty(target.toString())) {
                    throw new IllegalArgumentException("参数不能为空！");
                }
            } else if (target instanceof Collection) {
                for (Object o : (Collection) target) {
                    validate(o, validationHints);
                }
            } else {
                validate(target, validationHints);
            }
        }


    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {
        validate(o,errors,null);
    }

    private void validate(Object target,Object ... objs) {
        Set<ConstraintViolation<Object>> violations;
        // 没有groups的验证
        if(objs==null || objs.length==0) {
            violations = validator.validate(target);
        } else {
            // 基于groups的验证
            Set<Class<?>> groups = new LinkedHashSet<Class<?>>();
            for (Object hint : objs) {
                if (hint instanceof Class) {
                    groups.add((Class<?>) hint);
                }
            }
            violations = validator.validate(target, ClassUtils.toClassArray(groups));
        }
        // 若为空，则验证通过
        if(violations==null||violations.isEmpty()) {
            return;
        }
        // 验证不通过则抛出ParamsException异常。
        for(ConstraintViolation item:violations) {
            throw new IllegalArgumentException(item.getMessage());
        }
    }
}
