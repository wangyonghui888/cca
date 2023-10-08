package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_merchant_config
 *
 * @author duwan 2021-02-03
 */
@Data
@TableName("t_merchant_code_config")
public class MerchantCodeConfig implements Serializable {

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
     * 月货量（万） 最少值
     */
    private Integer startValue;

    /**
     * 月货量（万） 最大值
     */
    private Integer endValue;

    /**
     * 备注
     */
    private String remark;


}
