package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrderSettleDetail implements Serializable {
    private static final long serialVersionUID = 2819257632275334581L;
    /**
     * 自动编号
     */
    private Long id;

    /**
     * 订单单号
     */
    private String orderNo;

    /**
     * 注单单号
     */
    private String betNo;

    /**
     * 订单状态(0:待处理，1:已处理，2:取消交易)
     */
    private Integer orderStatus;
    /**
     * 盘口id
     */
    private Long marketId;
    /**
     * 投注项id
     */
    private Long playOptionsId;
    /**
     * 投注项类型
     */
    private String playOptions;

    private BigDecimal betAmount;
    private BigDecimal localBetAmount;

    private String playOptionName;

    private String optionValue;

    private String marketValue;
    /**
     * 玩法id
     */
    private Integer playId;
    private Long childPlayId;

    /**
     * 玩法名称
     */
    private String playName;

    private String originalPlay;

    private Double oddsValue;

    private Double oddFinally;
    /**
     * 赛事id
     */
    private String matchId;

    private String matchManageId;
    /**
     * 赛事名称
     */
    private String matchName;

    private String tournamentName;

    private Integer matchType;

    private String matchInfo;

    private String batchNo;

    private String phase;

    private Integer legOrder;

    private String matchDay;
    /**
     * 盘口类型(盘口类型( 1:欧盘 2:香港盘 3:美盘 4:印尼盘 5:马来盘))
     */
    private String marketType;
    /**
     * 体育名
     */
    private String sportName;

    /**
     * sport_id
     */
    private Integer sportId;

    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 盘口比赛结果
     */
    private String marketMatchResult;
    /**
     * tournamentid
     */
    private String tournamentId;
    /**
     * 注单结算结果
     */
    private Integer betResult;

    private Integer betStatus;

    private String settleScore;

    private String matchScore;


    private String homeName;

    private String awayName;

    private String scoreBenchmark;

    private String description;

    //private BigDecimal betAmount;
    /**
     * 结算时间
     */
    private Long settleTime;
    /**
     * 插入时间
     */
    private Long insertTime;
    /**
     * 修改时间
     */
    private Long updateTime;
    /**
     * 开始时间
     */
    private Long beginTime;

    private String beginTimeStr;

    private String remark;

    private Integer cancelType;

    private Integer tradeType;

    private String riskEvent;

    /**
     * 结算次数，已经被结算过的次数，持续累加
     */
    private Integer settleTimes;

    /**
     * 赛事状态
     */
    private Integer matchStatus;

    /**
     * 是否结束
     */
    private Integer matchOver;

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
        if (beginTime != null) {
            this.beginTimeStr = DateFormatUtils.format(new Date(beginTime), "yyyy-MM-dd HH:mm:ss");
        }
    }
}
