package com.panda.multiterminalinteractivecenter.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantDomainPO implements Serializable {

    private static final long serialVersionUID = -1L;

    private String merchantId;
    private String merchantCode;
    private String merchantName;
    private Long merchantGroupId;
    private String groupName;
    private Integer programId;
    private Integer domainType;
    private String domainName;
    private String areaName;

}
