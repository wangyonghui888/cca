package com.panda.sport.merchant.common.po.bss;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class FrontendMerchantRelation {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 商户分组ID
     */
    private Long frontendMerchantGroupId;

    /**
     * 商户编码
     */
    private String merchantCode;

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String updateUser;
}
