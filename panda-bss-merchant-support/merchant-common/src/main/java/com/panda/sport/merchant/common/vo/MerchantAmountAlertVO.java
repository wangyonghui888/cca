package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author javier
 * @date 2021/3/5
 */
@Data
public class MerchantAmountAlertVO {
    private String dateExpect;
    private Long timestamp;
    private Long merchantId;
    private String creditId;
    private BigDecimal usedAmount;
    private BigDecimal usedAmountPercent;
}
