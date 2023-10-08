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
public class TicketOrderSettleDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 自动编号
     * */
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
    private String orderStatus;
    /**
     * 盘口id
     */
    private String marketId;
    /**
     * 投注项id
     */
    private String playOptionsId;
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
    private String playId;
    private String childPlayId;

    /**
     * 玩法名称
     */
    private String playName;

    private String originalPlay;

    private String oddsValue;

    private String oddFinally;
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

    private String matchType;

    private String matchInfo;

    private String batchNo;

    private String phase;

    private String legOrder;

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
    private String sportId;

    /**
     * 创建时间
     */
    private String createTime;
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
    private String betResult;

    private String betStatus;

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
    private String settleTime;
    /**
     * 插入时间
     */
    private String insertTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 开始时间
     */
    private String beginTime;

    private String beginTimeStr;

    private String remark;

    private String cancelType;

    private String tradeType;

    private String riskEvent;

    /**
     * 结算次数，已经被结算过的次数，持续累加
     */
    private String settleTimes;

    /**
     * 赛事状态
     */
    private String matchStatus;

    /**
     * 是否结束
     */
    private String matchOver;


    private String ip;

    private String deviceImei;

}
