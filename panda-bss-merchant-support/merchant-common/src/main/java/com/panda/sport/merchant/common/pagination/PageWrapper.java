package com.panda.sport.merchant.common.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageWrapper<E> implements Serializable {

    protected static final int DEFAULT_MAX_PAGE_SIZE = 5000;
    protected static final int DEFAULT_PAGE_SIZE = 20;
    protected static final transient String DEFAULT_PARAM_ENCODING = "utf-8";
    private static final long serialVersionUID = -7503826106553472127L;
    /**
     * 当前页序号
     */

    protected int page = 1;
    /**
     * 当前页码
     */

    private int pageIndex;
    /**
     * 当前页码（页面传入字符时转换用）
     */
    private String pageIndexStr;
    private String orderK;
    private String orderV;
    private String combineOrderStr;
    /**
     * 页大小
     */
    private int pagesize = 0;
    /**
     * 页大小是否可以重设
     */
    private boolean pageSizeResetAble = true;
    /**
     * 最大页大小
     */
    private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;
    /**
     * 页数量
     */
    private int pageCount;
    /**
     * 行数量
     */
    private long rowCount;
    /**
     * 最大行数量
     */
    private long maxRowCount;
    /**
     * 数据列表
     */
    private List<E> list;
    private String paramEncoding = DEFAULT_PARAM_ENCODING;
    private Map<String, Object> paramMap = new HashMap<String, Object>();
    private String pageIndexKey = "PageWrapper.pageIndex";
    private String pageIndexKeyStr = "PageWrapper.pageIndexStr";
    public PageWrapper() {
    }
    /**
     * @param pagesize 页大小
     */
    public PageWrapper(int pagesize) {
        this(pagesize, false);
    }

    /**
     * @param pagesize 页大小
     */
    public PageWrapper(int pagesize, boolean pageSizeResetAble) {
        this.pagesize = pagesize;
        this.pageSizeResetAble = pageSizeResetAble;
    }

    /**
     * @param pageIndex 当前页码
     * @param pagesize  页大小
     */
    public PageWrapper(int pageIndex, int pagesize) {
        this.pageIndex = pageIndex < 1 ? 1 : pageIndex;
        this.pagesize = pagesize;
    }

    public int getPage() {
        return page;
    }

    public PageWrapper setPage(int page) {
        this.page = page;
        this.pageIndex = page;
        return this;
    }

    public String getOrderV() {
        return orderV;
    }

    public PageWrapper setOrderV(String orderV) {
        this.orderV = orderV;
        return this;
    }

    public String getOrderK() {
        return orderK;
    }

    public PageWrapper setOrderK(String orderK) {
        this.orderK = orderK;
        return this;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public PageWrapper setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
        return this;
    }

    /**
     * @return rowCount
     */
    public long getRowCount() {
        return rowCount;
    }

    /**
     * @param rowCount 要设置的 rowCount
     * @throws Exception
     */
    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
        if (maxRowCount > 0 && this.rowCount > maxRowCount) {
            this.rowCount = maxRowCount;
        }
        int pageSize = getPagesize();
        try {
            pageCount = (int) ((this.rowCount - 1 + pageSize) / pageSize);
        } catch (ArithmeticException e) {
            throw new RuntimeException("pagesize has not initial............");
        }
        if (pageCount > 0 && pageIndex > pageCount) {
            pageIndex = pageCount;
        }
    }

    /**
     * @return pagesize
     */
    public int getPagesize() {
        if (pagesize > maxPageSize) {
            return maxPageSize;
        } else if (pagesize < 1) {
            return 1;
        } else {
            return pagesize;
        }
    }

    public void setPagesize(int pagesize) {
        if (pageSizeResetAble) {
            this.pagesize = pagesize;
        }
    }

    public String getPageIndexStr() {
        return pageIndexStr;
    }

    public void setPageIndexStr(String pageIndexStr) {
        try {
            int pageIndex_stemp = Integer.parseInt(pageIndexStr);
            this.pageIndex = pageIndex_stemp;
        } catch (Exception e) {

        }
    }

    public String getPageIndexKeyStr() {
        return pageIndexKeyStr;
    }

    public void setPageIndexKeyStr(String pageIndexKeyStr) {
        this.pageIndexKeyStr = pageIndexKeyStr;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    /**
     * @return list
     */
    public List<E> getList() {
        if (list == null) {
            return new ArrayList();
        }
        return list;
    }

    /**
     * @param list 要设置的 list
     */
    public void setList(List<E> list) {
        this.list = list;
    }

    /**
     * @return paramEncoding
     */
    public String getParamEncoding() {
        return paramEncoding;
    }

    /**
     * @param paramEncoding 要设置的 paramEncoding
     */
    public void setParamEncoding(String paramEncoding) {
        this.paramEncoding = paramEncoding;
    }

    /**
     * @param key
     */
    public void removeParam(String key) {
        if (paramMap.containsKey(key)) {
            paramMap.remove(key);
        }
    }

    /**
     * @param key
     * @param value
     */
    public void removeParam(String key, Object value) {
        if (paramMap.containsKey(key) && (value == null ? "" : value.toString()).equals(paramMap.get(key))) {
            paramMap.remove(key);
        }
    }

    /**
     * @param request
     */
    public void addAllParam(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (name.indexOf(pageIndexKey) >= 0) {
                continue;
            }
            String[] values = request.getParameterValues(name);
            for (String value : values) {
                addParam(name, value);
            }
        }
    }

    /**
     * @param key
     * @param value
     */
    public void addParam(String key, Object value) {
        paramMap.put(key, value == null ? "" : value.toString());
    }

    /**
     * @return
     */
    public String getParams() {
        StringBuilder sb = new StringBuilder();
        try {
            for (String p : paramMap.keySet()) {
                sb.append("&");
                sb.append(URLEncoder.encode(p, paramEncoding));
                sb.append('=');
                sb.append(URLEncoder.encode(paramMap.get(p).toString(), paramEncoding));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 取当前页的第一行数据在数据库中的行号，在操作数据库的时候有用
     *
     * @return
     */
    public int getRowOffset() {
        return (getPageIndex() - 1) * getPagesize();
    }

    /**
     * @return pageIndex
     */
    public int getPageIndex() {
        return pageIndex < 1 ? 1 : pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 是否第一页
     *
     * @return
     */
    public boolean isFirstPage() {
        return getPageIndex() <= 1 ? true : false;
    }

    /**
     * 是否最后一页
     *
     * @return
     */
    public boolean isLastPage() {
        return getPageIndex() >= getPageCount() ? true : false;
    }

    /**
     * @return pageCount
     */
    public int getPageCount() {
        return pageCount;
    }

    public PageWrapper setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public String getPageIndexKey() {
        return pageIndexKey;
    }

    public void setPageIndexKey(String pageIndexKey) {
        this.pageIndexKey = pageIndexKey;
    }

    public long getMaxRowCount() {
        return maxRowCount;
    }

    public void setMaxRowCount(long maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    public int getPrePage() {
        return pageIndex < 1 ? 0 : pageCount - 1;
    }

    public int getNextpage() {
        return pageIndex >= pageCount ? pageCount : pageIndex + 1;
    }

}
