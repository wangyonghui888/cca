package com.panda.sport.merchant.manage.entity.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantTreeForm {

    /**
     * 0 现金用户
     * 1 商户名称
     */
    private Integer tag;

    /**
     * 商户名称
     */
    private String name;
}
