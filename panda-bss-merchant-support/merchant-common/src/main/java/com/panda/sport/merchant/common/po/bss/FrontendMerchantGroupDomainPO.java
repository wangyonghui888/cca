package com.panda.sport.merchant.common.po.bss;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.vo.merchant.MerchantSimpleVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantVO;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class FrontendMerchantGroupDomainPO {

    public static final String merchantKey = "front_merchant_group_";
    public static final String merchant_video_Key = "video_merchant_group_";

    private static final long serialVersionUID = 1L;

    /**
     * 域名名称
     */
    private String domainName;

    /**
     * 域名名称集合
     */
    private Map<String, String> domainMap;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 域名类型  1 前端PC域名,2 前端H5域名
     */
    private Integer domainType;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户名称集合
     */
    private Set<String> merchantNameSet;

    /**
     * 商户名称集合
     */
    private List<MerchantSimpleVO> merchantList;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户编码集合
     */
    private Set<String> merchantCodeSet;

    /**
     * 时间值
     */
    private Integer times;

    /**
     * 时间类型(1为分钟,2为小时,3为日,4为月)
     */
    private Integer timeType;

    /**
     * 报警阈值
     */
    private Integer alarmNum;

    /**
     * 是否启用(1为启用,0为禁用)
     */
    private Integer status;

    private String pc;
    private String h5;
}
