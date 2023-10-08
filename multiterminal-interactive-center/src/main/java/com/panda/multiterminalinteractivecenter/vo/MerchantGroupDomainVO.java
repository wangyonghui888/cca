package com.panda.multiterminalinteractivecenter.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantGroupDomainVO {

    /**
     * 商户分组名称
     */
    private String groupName;

    /**
     * 商户分组ID
     */
    private Integer merchantGroupId;

    /**
     * 商户分组类型
     */
    private Integer groupType;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户编码集合
     */
    private Set<String> merchantCodeSet;

    /**
     * 区域
     */
    private String areaName;

    /**
     * 域名类型
     */
    private Integer domainType;

    /**
     * 域名ID
     */
    private Long domainId;

    /**
     * 域名
     */
    private String domainName;

    /**
     * tab页签类型(ty、dj、cp)
     */
    private String tab;

    /**
     * 域名数据集合
     */
    private List<DomainInfoVO> domainInfo;
}
