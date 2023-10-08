package com.panda.sport.merchant.common.dto;

import com.panda.sport.merchant.common.po.bss.MerchantPO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * api域名的
 */
@Setter
@Getter
public class ApiMerchantGroupVO implements Serializable {

    // 商户id
    private Long id;

    // 商户code
    private List<String> merchantCodes;

    // 商户的基础信息
    private List<MerchantPO> merchantList;

    // 商户组名称
    private String groupName;

    // 商户组编码
    private String  groupCode;

    //
    private List<String> domainName;

    // 是否开启 1为开启 2为关闭
    private Integer status;

    // 创建时间
    private Long createTime;

    // 更新时间
    private Long updateTime;

}
