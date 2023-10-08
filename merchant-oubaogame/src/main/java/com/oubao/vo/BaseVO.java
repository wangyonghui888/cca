package com.oubao.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class BaseVO implements Serializable {

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 页码
     */
    private Integer page;
    /**
     * 页面大小
     */
    private Integer size;
    /**
     * 开始
     */
    private Integer start;
}
