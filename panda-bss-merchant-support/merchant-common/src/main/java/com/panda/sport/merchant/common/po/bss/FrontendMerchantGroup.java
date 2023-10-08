package com.panda.sport.merchant.common.po.bss;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class FrontendMerchantGroup {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long groupId;

    /**
     * 分组名称
     */
    private String groupName;
    private String groupType;

    /**
     * 是否启用(1为启用,2为禁用)
     */
    private Integer status;

    /**
     * 时间类型(1为分钟,2为小时,3为日,4为月)
     */
    private Integer timeType;

    /**
     * 时间值
     */
    private Integer times;

    /**
     * 报警阈值
     */
    private Integer alarmNum;

    /**
     * 商户编码集合
     */
    private List<String> merchantCodeList;

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String updateUser;

    /**1 商户组下的商户信息  2 未挂靠商户组的商户*/
    private Integer type;

    private String merchantName;
    /**0直营 2二级*/
    private Integer agentLevel;

    /**所属渠道商户code*/
    private String parentCode;
}
