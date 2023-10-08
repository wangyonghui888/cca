package com.panda.sport.merchant.common.vo.finance;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 对账单临时数据对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceDayVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商户Id
     */
    private String merchantId;

    /**
     * 商户Code
     */
    private String merchantCode;

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

    /**
     * 账期 年-月-日
     */
    private String financeDate;

    //搜索开始时间
    private String startTime;
    //搜索结束时间
    private String endTime;

    //1：投注时间    3：结算时间 （与其他页面一致）
    private String filter;
    //1：账务日（默认）    3：自然日
    private String timeZone;

    List<String> merchantCodeList;

    private List<String> financeDates;

    private String financeDayIdListStr;

}
