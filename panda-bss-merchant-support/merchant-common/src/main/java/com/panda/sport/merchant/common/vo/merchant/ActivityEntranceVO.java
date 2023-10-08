package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;


/**
 * @Author: dorf
 * @Date: 2021/8/20
 */
@Data
public class ActivityEntranceVO{

    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 商户名称
     */
    private Long merchantId;
    /**
     * 活动状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

}
