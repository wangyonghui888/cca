package com.panda.multiterminalinteractivecenter.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class FrontendMerchantDomain {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 域名ID集合
     */
    private List<Integer> ids;

    private List<String> domainNameList;

    /**
     * 商户分组ID
     */
    private Long groupId;

    private Long merchantGroupId;

    private String tab;

    /**
     * 域名类型(1 前端h5域名,2 前端pc域名 6，全量域名  7 精彩回放)
     */
    private Integer domainType;

    /**
     * 域名名称
     */
    private String domainName;

    /**
     * 使用状态(0 未使用 1已使用)
     */
    private Integer enable;

    /**
     * 启用时间
     */
    private Date enableTime;

    /**
     * 删除状态(0 未删除 1已删除)
     */
    private Integer deleteTag;

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String updateUser;

    private Integer pageSize;
    private Integer pageNum;
    private String sort;
    private String orderBy;
    private Date beginTime;
    private Date endTime;
}
