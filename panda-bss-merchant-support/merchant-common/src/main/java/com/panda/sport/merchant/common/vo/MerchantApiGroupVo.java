package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.po.bss.TDomain;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 商户Api分组
 */
@Setter
@Getter
public class MerchantApiGroupVo implements Serializable {

    // 商户分组
    private String id;

    //1:运维组，2:业务组，3:公用组
    private Integer groupType;

    private String groupName;

    // 商户组code
    private String groupCode;

    // 是否开启 1为开启 2为关闭
    private Integer status;

    // 商户domain
    List<TDomain> tDomainList;
}
