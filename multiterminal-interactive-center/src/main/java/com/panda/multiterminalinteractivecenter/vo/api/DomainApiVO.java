package com.panda.multiterminalinteractivecenter.vo.api;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
public class DomainApiVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
     */
    private Integer domainType;

    /**
     * 域名名称
     */
    private String domainName;

    /**
     * 域名组id
     */
    private Long domainGroupId;

    /**
     * 商户组id
     */
    private String merchantGroupId;

    /**
     * 状态
     */
    private Long status;

    private Integer enable;
}
