package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description t_domain
 * @author duwan
 * @date 2021-08-19
 */
@Data
public class TDomainVo implements Serializable {

    private static final long serialVersionUID = 1L;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 域名类型 1 前端域名，2 app域名，3 静态资源域名
     */
    private int domainType;

    /**
     * 域名
     */
    private String domainName;

    /**
     * 0 未使用 1使用
     */
    private int enable;

    /**
     * 启用时间
     */
    private Date enableTime;

    private String enableTimeStr;

    /**
     * 删除状态 0 未删除 1已删除
     */
    private int deleteTag;

    /**
     * 创建时间
     */
    private Date createTime;

    private String createTimeStr;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    private String updateTimeStr;

    /**
     * 商户组id
     */
    private Long merchantGroupId;

    /**
     * 更新人
     */
    private String updateUser;

    private Integer groupCode;

    public void setEnableTime(Date enableTime) {
        this.enableTime = enableTime;
        this.enableTimeStr = sdf.format(enableTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        this.createTimeStr = sdf.format(createTime);
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        this.updateTimeStr = sdf.format(updateTime);
    }

    public TDomainVo() {}
}