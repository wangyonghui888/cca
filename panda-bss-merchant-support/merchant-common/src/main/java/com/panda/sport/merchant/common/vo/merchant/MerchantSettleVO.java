package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hobart 2023-09-30
 */
@Data
//@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantSettleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商户id
     */
    private String id;


    /**
     * sportId 1 足球  2 篮球
     */
    private String sportId;

    /**
     * 提前结算开关，默认为0，0为关，1为开 足球
     */
    private Integer settleSwitchAdvance;


    /**
     * 提前结算开关，默认为0，0为关，1为开   篮球
     */
    private Integer settleSwitchBasket;



}
