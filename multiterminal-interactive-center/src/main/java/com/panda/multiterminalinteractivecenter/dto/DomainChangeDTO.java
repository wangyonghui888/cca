package com.panda.multiterminalinteractivecenter.dto;

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
    private String groupName;

    /**
     * 分组ID
     */
    private Long groupId;

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
    private Integer domainType;

    private String updateUser;
    private Long updateTime;
    private String ip;
}
