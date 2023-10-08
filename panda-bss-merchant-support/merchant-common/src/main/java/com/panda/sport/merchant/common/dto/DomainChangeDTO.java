package com.panda.sport.merchant.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainChangeDTO {

    /**
     * 分组名称
     */
    private String domainGroup;

    /**
     * 分组ID
     */
    private Integer groupId;

    /**
     * 新域名
     */
    private String newDomain;

    /**
     * 旧域名
     */
    private String oldDomain;

    /**
     * 类型(PC、H5)
     */
    private String domainType;

    private String updateUser;
    private Long updateTime;
}
