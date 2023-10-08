package com.panda.sport.merchant.common.vo.finance;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @auth: YK
 * @Description:财务-对账单日报表
 * @Date:2020/5/21 16:22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantFinanceDayVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表ID  账期-merchantCode-currency
     */
    private String id;

    private Integer managerCode;

    /**
     * 表ID  账期-merchantCode
     */
    private String financeDayId;

    /**
     * 账期 年-月-日
     */
    private String financeDate;

    /**
     * 账期时间戳
     */
    private Long financeTime;

    /**
     * 商户Id
     */
    private String merchantId;

    /**
     * 商户Code
     */
    private String merchantCode;
    /**
     * 商户Code
     */
    private List<String> merchantCodeList;

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
     * 投注金额
     */
    private BigDecimal orderAmountTotal;

    /**
     * 派彩金额
     */
    private BigDecimal settleAmount;

    /**
     * 投注笔数
     */
    private Long orderNum;

    /**
     * 有效注单数
     */
    private Integer orderValidNum;

    /**
     * 退回注单数
     */
    private Integer orderBackNum;

    /**
     * 帐变记录数
     */
    private Integer transferNum;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long modifyTime;

    private BigDecimal platformProfit;

    private Integer orderUserNum;

    private BigDecimal platformProfitRate;

    private BigDecimal settleOrderAmountTotal;

    private BigDecimal settleSettleAmount;

    private Long settleOrderNum;

    private Integer settleOrderValidNum;

    private Integer settleOrderBackNum;

    private Integer settleTransferNum;

    private BigDecimal settlePlatformProfit;

    private Integer settleOrderUserNum;

    private BigDecimal settlePlatformProfitRate;

//----------------------------------------------以下为业务字段---------------------------------------------------
    /**
     * 分页相关
     */
    private Integer pageNum;

    private Integer pageSize;

    /**
     * 列表序号使用
     */
    private Integer number;

    /**
     * 多币种列表
     */
    private List<MerchantFinanceDayVo> dayVoList;
    //搜索开始时间
    private String startTime;
    //搜索结束时间
    private String endTime;
    //1：投注时间    3：结算时间 （与其他页面一致）
    private String filter;
    //1：账务日（默认）    3：自然日
    private String timeZone;

    //1:列表调用 2：弹窗调用
    private Integer type;

    /**
     * 返回给前端的数据
     */
    //商户类型/代理级别(0,直营;1:渠道;2:二级代理)
    private String agentLevelStr;
    //结算币种
    private String currencyStr;

    private String language;

    /**
     * vip 级别 : 0,1
     */
    private Integer vipLevel;
    private Integer merchantNum;

    private String token;
}
