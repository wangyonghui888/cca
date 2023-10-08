package com.panda.sport.merchant.common.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据容器,用于容纳查询列表数据和分页信息 ;<br>
 * <br>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataTableContainer<T> implements Serializable {


    private static final long serialVersionUID = 4101915001784148873L;
    private T aaData;
    private PageWrapper pageWrapper;
    private long iTotalRecords = 0; //总记录数
    private Map<String, Object> map = new HashMap<String, Object>();

    //public DataTableContainer(T datas, PageWrapper pageWrapper) {
    public DataTableContainer(PageWrapper pageWrapper) {
        this.aaData = (T) pageWrapper.getList();
        this.setiTotalRecords(pageWrapper.getRowCount());

    }

    public DataTableContainer(T aaData) {
        super();
        this.aaData = aaData;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public DataTableContainer setMap(Map<String, Object> map) {
        this.map = map;
        return this;
    }

    public PageWrapper getDtCondition() {
        return pageWrapper;
    }

    public void setPageWrapper(PageWrapper pageWrapper) {
        this.pageWrapper = pageWrapper;
    }

    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public T getAaData() {
        return aaData;
    }

    public void setAaData(T aaData) {
        this.aaData = aaData;
    }
}
