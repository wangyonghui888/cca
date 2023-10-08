package com.panda.sport.merchant.common.vo.finance;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 财务-对账单日报表-汇总-列表
 * </p>
 *
 * @author Toney
 * @since 2020-06-13
 */
@Data
public class MerchantFinanceDayDetailListVo implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 账期-merchantCode
   */
  private String financeDayId;

  /**
   * 商户Id
   */
  private String merchantId;


  /**
   * 商户名称
   */
  private String merchantName;




  /**
   * 投注用户数
   */
  private Integer orderUserNum;

  /**
   * 投注笔数
   */
  private Long orderValidNum;


  /**
   * 投注金额
   */
  private BigDecimal orderAmountTotal;


  /**
   * 平台盈利
   */
  private BigDecimal platformProfit;




  /**
   * 盈利率
   *
   * @return
   */
  public BigDecimal getPlatformProfitRate() {

    BigDecimal result =platformProfit.multiply(new BigDecimal(100)).divide(orderAmountTotal,2, BigDecimal.ROUND_HALF_UP);
    return result;
  }
}
