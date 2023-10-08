package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description t_domain
 * @author duwan
 * @date 2021-08-19
 */
@Data
public class TDomain implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 删除状态 0 未删除 1已删除
     */
    private int deleteTag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 商户组id
     */
    private Long merchantGroupId;

    public TDomain() {}
}