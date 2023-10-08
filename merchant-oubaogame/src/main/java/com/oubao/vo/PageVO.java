package com.oubao.vo;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageVO<T> {
    /**
     * 数据总量
     */
    private Integer total;

    /**
     * 每页显示条数
     */
    private Integer size;

    /**
     * 当前页。从0 开始。
     */
    private Integer page;

    /**
     * 查询到的数据
     */
    private List<T> records = Collections.emptyList();

    public PageVO(Integer total, Integer size, Integer page) {
        this.total = total;
        this.size = size;
        this.page = page;
    }
}