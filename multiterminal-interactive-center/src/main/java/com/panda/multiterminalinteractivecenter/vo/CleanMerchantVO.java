package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CleanMerchantVO implements Serializable {

    private String merchantName;

    private Integer merchantTag;

    private String merchantCode;

    private String parentCode;

    private Integer containsType;

    private String containsStr;

    private String tab;

    private Long groupId;
}
