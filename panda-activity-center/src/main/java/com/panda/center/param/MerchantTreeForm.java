package com.panda.center.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
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
