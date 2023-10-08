package com.panda.multiterminalinteractivecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class DomainImportReqDTO {

    /**
     *  域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
     */
    private Integer domainType;

    /**
     *  分组类型(1Common2Y3S4B)
     */
    private Integer groupType;

    /**
     * 线路商ID
     */
    private Long lineCarrierId;

    /**
     *  域名
     */
    private List<String> domainName;

    /**
     *  操作人
     */
    private String tab;

    /**
     *  操作人
     */
    private String operator;


}
