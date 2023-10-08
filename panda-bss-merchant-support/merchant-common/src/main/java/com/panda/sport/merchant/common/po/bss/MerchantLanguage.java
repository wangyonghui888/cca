package com.panda.sport.merchant.common.po.bss;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class MerchantLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
        * 语种代码
     */
    private String languageName;

    private Integer status;

    /**
     *语种中文
     */
    private String msg;

}