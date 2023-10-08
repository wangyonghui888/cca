package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author :  duwan
 * @Project Name :  panda-bss-report
 * @Package Name :  com.panda.sports.report.common.dto
 * @Description :  TODO
 * @Date: 2021-06-20 16:56:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class MerchantMatchBetCountDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总计订单
     */
    private Integer orderAmount;

    /**
     * 平台盈利率
     */
    private BigDecimal profit;

    /**
     * 投注人数
     */
    private Integer userAmount;

    /**
     * 总计投注额
     */
    private BigDecimal validBetAmount;

    /**
     * 用户净输赢
     */
    private BigDecimal unSettleAmount;

    /**
     * 币种
     */
    private String currencyCode;

}
