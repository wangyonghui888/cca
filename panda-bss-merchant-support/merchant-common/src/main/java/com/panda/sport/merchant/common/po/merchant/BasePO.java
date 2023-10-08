package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author : Jeffrey
 * @Date: 2020-01-27 16:30
 * @Description :
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BasePO implements Serializable {

    private Long createTime;

    private String createUser;

    private Long modifyTime;

    private String modifyUser;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Override
    public String toString() {
        return "BasePO{" +
                "createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyUser='" + modifyUser + '\'' +
                '}';
    }
}
