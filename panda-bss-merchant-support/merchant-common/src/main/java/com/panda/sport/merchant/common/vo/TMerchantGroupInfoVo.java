package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description b端商户分组表
 * @author duwan
 * @date 2022-01-01
 */
@Data
public class TMerchantGroupInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户组名称
     */
    private String groupName;

    /**
     * 商户信息
     */
    private String account;

    /**
     * 域名类型 1 h5域名，2PC域名 ，3 App域名
     */
    private Integer type;

    /**
     * 域名信息
     */
    private String url;

}
