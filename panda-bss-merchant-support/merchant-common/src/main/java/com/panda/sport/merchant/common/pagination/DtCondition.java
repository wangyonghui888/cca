package com.panda.sport.merchant.common.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtCondition<T> implements Serializable {


    private static final long serialVersionUID = 6411715294367991847L;

    protected PageWrapper<T> pageWrapper = new PageWrapper<T>();

    /**
     * 当前页序号
     */
    protected int page = 1;
    /**
     * 单页数据量
     */
    protected int pagesize = 10;
    private String startTime;
    private String endTime;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getPage() {

        return page;
    }

    public DtCondition setPage(int page) {

        this.page = page;
        this.pageWrapper.setPageIndex(page);
        return this;
    }

    public int getPagesize() {

        return this.pageWrapper.getPagesize();
    }

    public DtCondition setPagesize(int pagesize) {

        this.pagesize = pagesize;
        this.pageWrapper.setPagesize(pagesize);
        return this;
    }

    public PageWrapper<T> getPageWrapper() {
        return pageWrapper;
    }

    public DtCondition setPageWrapper(PageWrapper<T> pageWrapper) {
        this.pageWrapper = pageWrapper;
        return this;

    }

    public String getFullStartTime() {
        if (StringUtils.isNotEmpty(this.startTime)) {
            return this.startTime + " 00:00:00";
        }
        return null;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFullEndTime() {
        if (StringUtils.isNotEmpty(this.endTime)) {
            return this.endTime + " 23:59:59";
        }
        return null;
    }

}
