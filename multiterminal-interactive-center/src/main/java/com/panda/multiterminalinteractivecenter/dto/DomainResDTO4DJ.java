package com.panda.multiterminalinteractivecenter.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainResDTO4DJ {

    private Long merchantGroupId;
    private Long merchantId;
    private String merchantAccount;
    /**域名组类型0默认  1 区域，2 VIP*/
    private Integer domainGroupType;
    /**区域Id*/
    private String areaId;
    /**区域名称*/
    private String areaName;
    /**区域CODE*/
    private String areaCode;
    /**域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名*/
    private Integer domainType;
    /**域名列表，|分割*/
    private String domains;
}
