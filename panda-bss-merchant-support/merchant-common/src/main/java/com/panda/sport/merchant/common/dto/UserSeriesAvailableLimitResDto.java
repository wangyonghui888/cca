package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 用户串关可用额度 出参
 * @Author : Paca
 * @Date : 2021-12-26 21:53
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class UserSeriesAvailableLimitResDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    private String merchantId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账务日期
     */
    private String dateExpect;

    /**
     * 用户特殊限额类型，0-无，1-标签限额，2-特殊百分比限额，3-特殊单注单场限额，4-特殊VIP限额
     */
    private String userSpecialLimitType;

    /**
     * 单日串关赔付总限额（串关单日初始额度），值为（new BigDecimal(Integer.MAX_VALUE)）时，表示不限制，特殊限额的时候可能不限制
     */
    private BigDecimal dailySeriesPaymentTotalLimit;

    /**
     * 单日串关赔付总已用限额
     */
    private BigDecimal dailySeriesPaymentTotalUsedLimit;

    /**
     * 单日串关赔付总可用限额 = dailySeriesPaymentTotalLimit - dailySeriesPaymentTotalUsedLimit
     */
    private BigDecimal dailySeriesPaymentTotalAvailableLimit;

}
