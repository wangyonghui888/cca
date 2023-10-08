package com.panda.sport.merchant.common.vo.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @auth: YK
 * @Description:财务-清算月报表
 * @Date:2020/5/21 14:41
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantFinanceMonthVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 表ID 账期-merchantCode
     */
    private String id;
    /**
     * 日期类型 UTC8,EZ
     */
    private String timeZone;
    /**
     * 账期 年-月
     */
    private String financeDate;

    /**
     * 账单时间戳
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

    private List<String> merchantCodeList;

    /**
     * 上级商户id
     */
    private String parentId;

    /**
     * 二级商户数量
     */
    private Integer merchantNum;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户类型/代理级别(0,直营;1:渠道;2:二级代理)
     */
    private Integer agentLevel;
    /**
     * 商户类型/代理级别(0,直营;1:渠道;2:二级代理)
     */
    private List<Integer> agentLevelList;

    /**
     * 结算币种:默认1,人民币，2为积分制
     */
    private String currency;

    /**
     * 账单金额：a*费率()+vip费+技术服务费
     */
    private BigDecimal billAmount;

    /**
     * 应缴费用：a*费率
     */
    private BigDecimal orderPaymentAmount;

    /**
     * 投注金额
     */
    private BigDecimal orderAmountTotal;

    /**
     * 盈利金额
     */
    private BigDecimal profitAmount;

    /**
     * 分成方式(1,投注;2盈利)
     */
    private String computingStandard;

    /**
     * 标准费率%
     */
    private Double terraceRate;

    /**
     * 执行费率%
     */
    private Double executeRate;

    /**
     * 调整额,含正负
     */
    private BigDecimal adjustAmount;

    /**
     * 旧的调整额,含正负
     */
    private BigDecimal oldAdjustAmount;

    /**
     * VIP费用
     */
    private Long vipAmount;

    /**
     * 技术费用
     */
    private Long techniqueAmount;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 缴纳周期(1:月,2:季,3年)
     */
    private Integer paymentCycle;

    /**
     * 缴纳周期(1:月,2:季,3年)
     */
    private Integer vipPaymentCycle;

    /**
     * 缴纳周期(1:月,2:季,3年)
     */
    private Integer techniquePaymentCycle;

    /**
     * 投注笔数
     */
    private Integer orderNum;

    /**
     * 调整原因
     */
    private String adjustCause;

    private String oldAdjustCause;

//----------------------------------------------以下为业务字段---------------------------------------------------
    /**
     * 盈利金额区间
     */
    private Long profitAmountStart;
    private Long profitAmountEnd;

    /**
     * 账单详情列表
     */
    private List<MerchantFinanceBillMonthVo> billMonthVoList;

    /**
     * 分页相关
     */
    private Integer pageNum;
    private Integer pageSize;
    /**
     * 排序相关
     */
    private String sort;
    private String orderBy;

    /**
     * 账单金额 繁体转换
     */
    private String billAmountName;

    /**
     * 列表序号使用
     */
    private Integer number;

    /**
     * 返回给前端的数据
     */
    //商户类型/代理级别(0,直营;1:渠道;2:二级代理)
    private String agentLevelStr;
    //结算币种
    private String currencyStr;
    //分成方式(1,投注;2盈利)
    private String computingStandardStr;
    /**
     * 人民币相关转换展示
     */
    private BigDecimal billAmountCNY;
    private BigDecimal orderAmountTotalCNY;
    private BigDecimal profitAmountCNY;


}
