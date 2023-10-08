package com.panda.multiterminalinteractivecenter.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainInfoVO {

    private Long id;
    /**
     *  域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
     */
    private Integer domainType;

    /**
     *  域名名称
     */
    private String domainName;

    /**
     * 区域
     */
    private String areaName;
}
