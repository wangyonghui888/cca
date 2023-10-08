package com.panda.multiterminalinteractivecenter.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author butr 2020-01-21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class MerchantResultVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商户id
     */
    private String id;
    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 域名防护商户组
     */
    private Long merchantGroupId;

    /**
     * 商户分库组组code,关联t_merchant_domain_group.group_code
     */
    private String domainGroupCode;

}
