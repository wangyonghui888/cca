package com.panda.sport.merchant.common.vo.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainGroupApiVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 域名名称
     */
    private String domainGroupName;

    /**
     * 分组类型
     */
    private Integer groupType;

    /**
     * 分组类型
     */
    private Integer domainType;

    /**
     * 专属类型 1区域 2vip
     */
    private Integer exclusiveType;

    /**
     * 所属区域
     */
    private Long belongArea;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 状态
     */
    private Long status;

    /**
     * 域名列表
     */
    private List<DomainApiVO> domainls;

}
