package com.panda.sport.merchant.common.vo.merchant;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CurrencyRateVO implements Serializable {

    /**
     * 中文
     */
    private String countryZh;

    /**
     * 英文
     */
    private String countryCn;

    /**
     * 编号
     */
    private Integer currencyCode;
}
