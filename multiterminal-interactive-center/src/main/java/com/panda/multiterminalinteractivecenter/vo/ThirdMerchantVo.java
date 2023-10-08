package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ThirdMerchantVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    private Long id;

    /**
     * 商户账号
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 入驻时间
     */
    private Long creatTime;

    private Date createdAt;

}
