package com.panda.multiterminalinteractivecenter.dto;

import lombok.Data;

@Data
public class DomainRelationDto {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 域名组Id
     */
    private Long domainGroupId;


    /**
     * 域名Id
     */
    private Long domainId;

    /**
     * 阈值
     */
    private Integer alarmThreshold ;

    /**
     * tab
     */
    private String tab ;

    private String domainName;
    private Integer domainType;

    private Integer h5Threshold;
    private Integer pcThreshold;
    private Integer apiThreshold;
    private Integer imgThreshold;


}
