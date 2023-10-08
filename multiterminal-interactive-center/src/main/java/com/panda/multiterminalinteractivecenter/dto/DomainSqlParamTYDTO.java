package com.panda.multiterminalinteractivecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainSqlParamTYDTO {

    /**
     * 是否未相同线路商
     */
    private Boolean isSameLineCarrier;

    /**
     * 线路商ID
     */
    private Long lineCarrierId;

    /**
     * tab : TY/DJ/CP
     */
    private String tab;

    /**
     * 分组类型
     */
    private Integer groupType;

    /**
     * 域名组ID
     */
    private Long domainGroupId;


    /**
     * 域名类型
     * @see com.panda.multiterminalinteractivecenter.enums.DomainTypeEnum
     */
    private Integer domainType;

}
