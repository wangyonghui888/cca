package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @description t_domain
 * @author ifan
 * @date 2022-07-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private int status;

    /**
     * 启用时间
     */
    private Long enableTime;

    /**
     * 创建时间
     */
    private Long createTime;

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

    private String tab;

    private Integer groupType;

    private Long lineCarrierId;

    private Integer selfTestTag;

}