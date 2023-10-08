package com.panda.sport.merchant.api.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源的变量
     */
    public static void setDateSoureType(String dsType) {
        CONTEXT_HOLDER.set(dsType);
    }

    /**
     * 获得数据源的变量
     */
    public static String getDateSoureType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空数据源变量
     */
    public static void clearDateSoureType() {
        CONTEXT_HOLDER.remove();
    }
}
