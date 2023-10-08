package com.panda.sport.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : Jeffrey
 * @Date: 2020-01-06 20:50
 * @Description : 商户注单VO对象
 */
@Data
public class MerchantBetOrderDetailVO implements Serializable {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 注单单号
     */
    private String betNo;
    /**
     * 盘口ID
     */
    private Long marketId;
    /**
     * 投注项ID
     */
    private Long playOptionsId;
    /**
     * 投注项类型
     */
    private String playOptions;
    /**
     * 玩法ID
     */
    private Long playId;
    /**
     * 玩法名称
     */
    private String playName;
    /**
     * 赔率
     */
    private Double oddsValue;
    /**
     * 赛事ID
     */
    private Long matchId;
    /**
     * 联赛ID
     */
    private Long tournamentId;
    /**
     * 赛事名称
     */
    private String matchName;
    /**
     * 赛事名称
     */
    private String matchInfo;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 体育名
     */
    private String sportName;
    /**
     * 体育种类
     */
    private Integer sportId;


}


