package com.panda.sport.admin.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 汇率输出
 */
@Setter
@Getter
public class RateVo {

    /**
     * 页数
     */
    private Integer size;

    /**
     * 第几页
     */
    private Integer pg;

    /**
     * 关键词
     */
    private String keyWord;

    /**
     * 汇率编码
     * */
    private Integer  code;

    /**
     * 当前日期
     */
    private String  dt;

}
