package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  日志字段详情
 * @Date: 2020-09-01 16:57
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
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

    /**
     * 初始化
     */
    public MerchantLogFiledVO(){
        this.fieldName = new ArrayList<>();
        this.beforeValues = new ArrayList<>();
        this.afterValues = new ArrayList<>();
    }
}
