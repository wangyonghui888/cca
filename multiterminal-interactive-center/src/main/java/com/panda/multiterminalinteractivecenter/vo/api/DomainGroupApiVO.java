package com.panda.multiterminalinteractivecenter.vo.api;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
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
     * 专属类型
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
