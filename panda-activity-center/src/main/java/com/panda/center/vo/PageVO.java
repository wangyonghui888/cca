package com.panda.center.vo;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageVO<T> {
    /**
     * 数据总量
     */
    private Integer total=0;

    /**
     * 每页显示条数
     */
    private Integer pageSize=20;

    /**
     * 当前页。从0 开始。
     */
    private Integer pageNum=1;

    private Integer start=0;

    private Integer end=0;

    /**
     * 查询到的数据
     */
    private List<T> records = Collections.emptyList();

    public PageVO(Integer total, Integer size, Integer page) {
        this.total = total;
        this.pageSize = size;
        this.pageNum = page;
        if (size == null || size == 0) {
            this.pageSize = 100;
        }
        if (size > 100) {
            this.pageSize = 100;
        }
        if (page == null || page == 0) {
            this.pageNum = 1;
        }
        this.start = (this.pageNum - 1) * this.pageSize;
        this.end = this.pageNum * this.pageSize;
    }

    public PageVO(Page<T> pgae, List<T> list) {
        if(pgae == null || list == null){
            return;
        }
        //设置集合时设置总数
        this.total = (int)pgae.getTotal();
        this.pageSize = pgae.getPageSize();
        this.pageNum = pgae.getPageNum();
        this.start = (this.pageNum - 1) * this.pageSize;
        this.end = this.pageNum * this.pageSize;
        this.records = list;
    }
}
