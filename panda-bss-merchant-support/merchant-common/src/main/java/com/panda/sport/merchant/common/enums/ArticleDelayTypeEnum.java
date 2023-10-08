package com.panda.sport.merchant.common.enums;

/**
 * 赛事文章延迟类型
 */
public enum ArticleDelayTypeEnum {
    AUTO_SHOW(1,"自动上线"),
    AUTO_TOP(2,"自动置顶"),
    AUTO_OFFLINE(3,"置顶自动下线");
    ;

    private Integer type;
    private String name;

    ArticleDelayTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
