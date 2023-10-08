package com.panda.sport.merchant.common.vo.finance;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 财务-对账单日报表-汇总-列表
 * </p>
 *
 * @author Toney
 * @since 2020-06-13
 */
@Data
public class MerchantFinanceDayListVo implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 商户Id
   */
  private String merchantId;


  /**
   * 商户名称
   */
  private String merchantName;

  /**
   * 商户类型/代理级别(0,直营;1:渠道;2:二级代理)
   */
  private Integer agentLevel;


  /**
   * 注单币种，1:人民币 2:美元 3:欧元 4:新元
   */
  private String currency;

  private String currencyStr;


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
   * 多币种列表
   */
  private List<MerchantFinanceDayListVo> dayVoList;


  /**
   * 子节点
   */
  private List<MerchantFinanceDayListVo> subNodeList;


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
