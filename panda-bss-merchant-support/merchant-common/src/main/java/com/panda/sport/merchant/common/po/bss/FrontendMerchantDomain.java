package com.panda.sport.merchant.common.po.bss;

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

    /**
     * 商户分组ID
     */
    private Integer merchantGroupId;

    /**
     * 域名类型(1 前端PC域名,2 前端H5域名)
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
    private Integer pageIndex;
    private String sort;
    private String orderBy;
    private Date beginTime;
    private Date endTime;
}
