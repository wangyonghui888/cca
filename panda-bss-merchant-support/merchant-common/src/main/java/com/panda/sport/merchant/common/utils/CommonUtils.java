package com.panda.sport.merchant.common.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

public class CommonUtils {
    private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);

    public CommonUtils() {
    }

    public static <T> T copyProperties(Object source, Class<T> targetType) {
        if (Objects.isNull(source)) {
            return null;
        } else {
            try {
                T target = targetType.newInstance();
                BeanUtils.copyProperties(source, target);
                return target;
            } catch (IllegalAccessException | InstantiationException var3) {
                log.error("对象浅拷贝出错 -> {}", var3.getMessage(), var3);
                throw new RuntimeException("对象浅拷贝出错");
            }
        }
    }

    public static <S, T> List<T> copyList(Collection<? extends S> sourceList, List<T> targetList, Class<T> targetType) {
        return (List)copyCollection(sourceList, targetList, targetType);
    }

    public static <S, T> List<T> copyList(Collection<? extends S> sourceList, Class<T> targetType) {
        return (List)copyCollection(sourceList, new ArrayList(), targetType);
    }

    public static <S, T> Set<T> copySet(Collection<? extends S> sourceList, Set<T> targetList, Class<T> targetType) {
        return (Set)copyCollection(sourceList, targetList, targetType);
    }

    public static <S, T> Collection<T> copyCollection(Collection<? extends S> sourceList, Collection<T> targetList, Class<T> targetType) {
        if (!CollectionUtils.isEmpty(sourceList)) {
            sourceList.forEach((source) -> {
                try {
                    T target = targetType.newInstance();
                    BeanUtils.copyProperties(source, target);
                    targetList.add(target);
                } catch (IllegalAccessException | InstantiationException var4) {
                    log.error("集合浅拷贝出错 -> {}", var4.getMessage(), var4);
                    throw new RuntimeException("集合浅拷贝出错");
                }
            });
        }

        return targetList;
    }

    public static <S> String joinCollection(String delimiter, Collection<S> collection) {
        StringBuffer buffer = new StringBuffer();
        if (!CollectionUtils.isEmpty(collection)) {
            collection.forEach((item) -> {
                if (buffer.length() > 0) {
                    buffer.append(delimiter);
                }

                buffer.append(item);
            });
        }

        return buffer.toString();
    }
}
