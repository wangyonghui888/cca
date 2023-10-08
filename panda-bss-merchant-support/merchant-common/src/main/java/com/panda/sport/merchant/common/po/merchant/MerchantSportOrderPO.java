package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MerchantSportOrderPO implements Serializable {


    private Integer id;
    /**
     * 体育种类
     */
    private String sportId;
    /**
     * 商户代码
     */
    private String merchantCode;

    private Integer time;
    /**
     * 投注用户数/注册用户数
     */
    private Integer betUserRate;
    /**
     * 投注量-派彩金额
     */
    private BigDecimal profit;
    /**
     * 返奖率,派彩金额/总投注量，
     */
    private Integer returnRate;
    /**
     * 派彩金额
     */
    private BigDecimal returnAmount;
    /**
     * 商户等级
     */
    private Byte merchantLevel;
    /**
     * 注单金额
     */
    private BigDecimal betAmount;
    /**
     * 订单数量
     */
    private Integer orderNum;
    /**
     * 投注用户数
     */
    private Integer betUserAmount;
    /**
     * 成功结算用户数
     */
    private Integer settileUserRate;
    /**
     * 当天结算派彩金额
     */
    private BigDecimal settleProfit;
    /**
     * 当天结算派彩金额
     */
    private BigDecimal settleReturn;
    /**
     * 返奖率,结算注单派彩金额/结算注单总投注量
     */
    private Integer settleReturnRate;
    /**
     * 结算注单总额
     */
    private BigDecimal settleBetAmount;
    /**
     * 结算订单数量
     */
    private Integer settleOrderNum;
    /**
     *当天比赛注单用户数
     */
    private Integer liveUserRate;


    /**
     *当天比赛注单派彩金额
     */
    private BigDecimal liveProfit;
    /**
     * 当天比赛注单派彩金额
     */
    private BigDecimal liveReturn;
    /**
     * 当天比赛返奖率
     */
    private Integer liveReturnRate;
    /**
     * 当天比赛注单总额
     */
    private BigDecimal liveBetAmount;
    /**
     * 比赛当日注单数
     */
    private Integer liveOrderNum;


    /**
     * 新增用户数
     */
    private Integer addUser;

    /**
     * 注册用户数
     */
    private Integer registerAmount;
    /**
     * 商户名称
     */
    private String  merchantName;

    /**
     * 动态表参数
     */
    private String tableName;


    private static final long serialVersionUID = 1L;



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sportId=").append(sportId);
        sb.append(", merchantCode=").append(merchantCode);
        sb.append(", time=").append(time);
        sb.append(", betUserRate=").append(betUserRate);
        sb.append(", profit=").append(profit);
        sb.append(", returnRate=").append(returnRate);
        sb.append(", returnAmount=").append(returnAmount);
        sb.append(", merchantLevel=").append(merchantLevel);
        sb.append(", betAmount=").append(betAmount);
        sb.append(", orderNum=").append(orderNum);
        sb.append(", betUserAmount=").append(betUserAmount);
        sb.append(", settileUserRate=").append(settileUserRate);
        sb.append(", settleProfit=").append(settleProfit);
        sb.append(", settleReturn=").append(settleReturn);
        sb.append(", settleReturnRate=").append(settleReturnRate);
        sb.append(", settleBetAmount=").append(settleBetAmount);
        sb.append(", settleOrderNum=").append(settleOrderNum);
        sb.append(", liveUserRate=").append(liveUserRate);
        sb.append(", liveProfit=").append(liveProfit);
        sb.append(", liveReturn=").append(liveReturn);
        sb.append(", liveReturnRate=").append(liveReturnRate);
        sb.append(", liveBetAmount=").append(liveBetAmount);
        sb.append(", liveOrderNum=").append(liveOrderNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}