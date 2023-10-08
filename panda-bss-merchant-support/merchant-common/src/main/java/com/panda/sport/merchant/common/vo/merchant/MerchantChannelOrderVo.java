package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Butr
 * @Date: 2020/10/07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantChannelOrderVo implements Serializable {

    /**
     * 唯一ID,merchantCode+time
     */
    private String id;

    /**
     * merchant_code
     */
    private String merchantCode;

    /**
     * merchant_name
     */
    private String merchantName;

    /**
     * 增长金额
     */
    private BigDecimal addBetAmount;

    /**
     * 投注额
     */
    private BigDecimal validBetAmount;

    /**
     * 增速
     */
    private BigDecimal growthRate;

    /**
     * 上月排名
     */
    private Integer lastBy;

    /**
     * 当前排名
     */
    private Integer orderBy;
}
