package com.panda.sport.merchant.manage.entity.form;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class MpForm {

    /**
     * 商户的code
     */
    private String code;
}
