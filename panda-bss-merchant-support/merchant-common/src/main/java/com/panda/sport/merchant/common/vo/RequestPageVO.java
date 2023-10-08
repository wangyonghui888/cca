package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class RequestPageVO<T> {
    /**
     * 每页显示条数(默认十条)
     */
    private Integer pageSize = 20;
    /**
     * 当前页数
     */
    private Integer pageNum = 1;
    /**
     * 每页显示条数(默认十条)
     */
    private Integer size = 20;
    /**
     * 当前页数
     */
    private Integer currPage = 1;
    /**
     * 页面请求参数
     */
    private T param;
}