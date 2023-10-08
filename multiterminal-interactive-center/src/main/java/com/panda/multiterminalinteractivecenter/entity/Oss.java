package com.panda.multiterminalinteractivecenter.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class Oss implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer domainType;

    private String merchantTypeName;

    private Integer groupType;

    private String domainName;

    private String encryptDomainName;

    private String encryptNewDomain;

}
