package com.panda.multiterminalinteractivecenter.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantDomainVO {

    private Long merchantGroupId;

    /**
     *  域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
     */
    private Integer domainType;

    /**
     *  域名名称
     */
    private String domainName;

    private String groupCode;

    private String groupName;

    private String userValue;

    private Long domainGroupId;

    /**
     * 前端域名分组
     */
    private String domainGroup;

    private Long domainId;

    /**
     * 旧域名
     */
    private String oldDomain;

    private String programId;

    private Integer groupType;

    private String areaName;

    private Integer enable;

    private String tab;

    private String merchantAccount;

}
