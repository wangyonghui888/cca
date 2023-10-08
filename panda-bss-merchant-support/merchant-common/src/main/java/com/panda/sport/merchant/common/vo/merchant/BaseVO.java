package com.panda.sport.merchant.common.vo.merchant;

import java.io.Serializable;

/**
 * @author : Jeffrey
 * @Date: 2020-01-27 16:19
 * @Description :
 */
public class BaseVO implements Serializable {
    /**
     * 操作时间
     */
    private Long modifyTime;
    /**
     * 操作用户
     */
    private String modifyUser;

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
        return "BaseVO{" +
                "modifyTime=" + modifyTime +
                ", modifyUser='" + modifyUser + '\'' +
                '}';
    }
}
