package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class TMerchantGroupInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 商户编码
     */
    private Long merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户入住时间
     */
    private Long creatTime;

    /**
     * 商户分组id
     */
    private Integer merchantGroupId;

    /**
     * 操作时间
     */
    private Long updateTime;

    /**
     * 操作人
     */
    private String operator;

    public TMerchantGroupInfo() {}
}
