package com.panda.sport.merchant.common.po.bss;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 商户域名分组
 */
@Setter
@Getter
public class TMerchantDomainGroupPo {

    // 主键
    private Long id;

    // 商户组名称
    private String groupName;

    // 商户组编码
    private String groupCode;

    // 域名名称
    private String domainName;

    // 是否开启 1为开启 2为关闭
    private Integer status;
    // 创建时间
    private Long createTime;

    // 更新时间
    private Long updateTime;
}
