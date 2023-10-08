package com.panda.multiterminalinteractivecenter.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 */
@Data
@Accessors(chain = true)
public class DomainSqlParamDto {

    /**
     * 是否未相同域名组
     */
    private Boolean isSameDomainGroup;


    /**
     * 是否未相同线路商
     */
    private Boolean isSameLineCarrier;

    /**
     * 是否VIP域名组的域名
     */
    private Boolean isVIP;

    /**
     * tab : DJ/CP
     */
    private String tab;

}
