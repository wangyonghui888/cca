package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * t_merchant_config
 *
 * @author duwan 2021-02-03
 */
@Data
@TableName("t_merchant_code_log")
public class MerchantCodeConfigLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 商户等级
     */
    private String code;

    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 月份  年-月
     */
    private String  month;


    /**
     * 创建时间
     */
    private Long createTime;


}
