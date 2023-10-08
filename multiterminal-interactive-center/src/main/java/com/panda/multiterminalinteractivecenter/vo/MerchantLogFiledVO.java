package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :  ifan
 * @Description :  日志字段详情
 * @Date: 2022-07-11
 */
@Data
public class MerchantLogFiledVO {

    /**
     * 字段名称
     */
    private List<String> fieldName;

    /**
     * 修改之前的值
     */
    private List<String> beforeValues;

    /**
     * 修改之后的值
     */
    private List<String> afterValues;

    private Integer domainType;

    private String merchantName;

    /**
     * 初始化
     */
    public MerchantLogFiledVO(){
        this.fieldName = new ArrayList<>();
        this.beforeValues = new ArrayList<>();
        this.afterValues = new ArrayList<>();
    }
}
