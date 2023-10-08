package com.oubao.po;


import java.io.Serializable;

public class ScheduleSearchPO implements Serializable {

    public ScheduleSearchPO() {
        super();
    }

    public ScheduleSearchPO(String keyword) {
        super();
        this.keyword = keyword;
    }

    private Long id;

    private Long searchValue;

    private Long userId;

    private Integer searchType;

    private String keyword;

    private Long count;

    private String condition;

    private String uuid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(Long searchValue) {
        this.searchValue = searchValue;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


}