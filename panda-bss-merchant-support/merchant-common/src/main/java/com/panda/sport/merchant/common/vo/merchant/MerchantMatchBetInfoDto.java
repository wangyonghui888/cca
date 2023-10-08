package com.panda.sport.merchant.common.vo.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-bss-report
 * @Package Name :  com.panda.sports.report.common.dto
 * @Description :  TODO
 * @Date: 2021-06-20 18:39:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class MerchantMatchBetInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自动编号
     */
    private String id;

    /**
     * 赛事id
     */
    private String matchId;

    /**
     * 1人民币 2美元 3港币 4越南盾 5新加坡币 6英镑 7欧元 8比特币
     */
    private String orderCurrencyCode;

    /**
     * 1 常规赛事 2虚拟赛事
     */
    private Integer matchType;

    /**
     * 赛事对阵
     */
    private String matchInfo;

    /**
     * 运动种类编号
     */
    private Integer sportId;

    /**
     * 运动种类名称
     */
    private String sportName;

    /**
     * 开赛时间
     */
    private Long beginTime;

    /**
     * 比赛状态
     */
    private String matchStatus;

    /**
     * 注单总金额
     */
    private BigDecimal betAmount;

    /**
     * valid_bet_amount
     */
    private BigDecimal validBetAmount;

    /**
     * 派彩金额
     */
    private BigDecimal settleAmount;

    /**
     * 盈利金额
     */
    private BigDecimal profit;

    /**
     * tournament_id
     */
    private Long tournamentId;

    /**
     * 投注用户数
     */
    private Integer userAmount;

    /**
     * 投注笔数
     */
    private Long orderAmount;

    /**
     * profit_rate
     */
    private BigDecimal profitRate;

    /**
     * 玩法数量
     */
    private Long playAmount;

    /**
     * 根据此字段增量同步到elasticsearch
     */
    private Long updatedTime;

    /**
     * 联赛名称
     */
    private String tournamentName;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * tournament_level
     */
    private Integer tournamentLevel;

    /**
     * un_settle_order
     */
    private Long unSettleOrder;

    /**
     * un_settle_amount
     */
    private BigDecimal unSettleAmount;

    /**
     * agent_level
     */
    private int agentLevel;

    /**
     * 串关有效投注金额
     */
    private BigDecimal parlayVaildBetAmount;

    /**
     * 串关有效投注订单数
     */
    private Integer parlayValidTickets;

    /**
     * 串关盈利金额
     */
    private BigDecimal parlayProfit;

    /**
     * 串关盈利率
     */
    private BigDecimal parlayProfitRate;

    /**
     * 开始时间
     */
    private String beginTimeStr;

    /**
     * 详情
     */
    List<MerchantMatchBetInfoDto> betInfoDtos;

    @ApiModelProperty(value = "有效投注金额")
    private BigDecimal settleValidBetMoney;

    @ApiModelProperty(value = "有效投注笔数")
    private Integer settleValidOrderCount;

    public MerchantMatchBetInfoDto() {
    }

}
