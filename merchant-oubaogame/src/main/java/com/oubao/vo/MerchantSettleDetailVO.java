package com.oubao.vo;


import lombok.Data;

import java.io.Serializable;
/**
 * @author : Jeffrey
 * @Date: 2020-01-07 10:44
 * @Description : 商户结算明细
 */
@Data
public class MerchantSettleDetailVO implements Serializable {
    /**
     * 注单编号
     */
    private String betNo;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 运动种类编号
     */
    private Integer sportId;
    /**
     * 运动种类名称
     */
    private String sportName;
    /**
     * 玩法编号
     */
    private Long playId;
    /**
     * 玩法名称
     */
    private String playName;
    /**
     * 赛事编号
     */
    private Long matchId;
    /**
     * 类型：1 ：早盘 ，2： 滚球盘， 3： 冠军盘
     */
    private Integer matchType;
    /**
     * "最终赔率 ，因为有一些是1/20")
     */
    private String oddFinally;
    /**"是否自动接收最高赔率")
     *
     */
    private Integer acceptBetOdds;
    /**"下注时间")
     *
     */
    private Long betTime;
    /**"盘口类型(OU:欧盘 HK:香港盘 US:美式盘 ID:印尼盘 MY:马来盘 GB:英式盘）")
     *
     */
    private String marketType;
    /**"盘口值")
     *
     */
    private String marketValue;
    /**"对阵信息")
     *
     */
    private String matchInfo;
    /**"注单金额")
     *
     */
    private Double betAmount;
    /**"赔率值")
     *
     */
    private Double oddsValue;
    /**"最高可赢金额")
     *
     */
    private Double maxWinAmount;
    /**"注单状态(0未结算 1已结算 2结算异常)")
     *
     */
    private Integer betStatus;
    /**"基准比分")
     *
     */
    private String scoreBenchmark;
    /**"投注类型ID 对应上游投注项ID")
     *
     */
    private Long playOptionsId;
    /**"投注类型(投注时下注的玩法选项)，规则引擎用")
     *
     */
    private String playOptions;
    /**"玩法选项范围")
     *
     */
    private String playOptionsRange;
    /**"联赛id")
     *
     */
    private Long tournamentId;
    /**"盘口ID")
     *
     */
    private Long marketId;
    /**"最终盘口类型")
     *
     */
    private String marketTypeFinally;
    /**"赛事名称")
     *
     */
    private String matchName;
    /**"注项结算结果0-无结果  2-走水  3-输 4-赢 4-赢一半 5-输一半")
     *
     */
    private Integer betResult;
    /**"负盘附加值")
     *
     */
    private Double addition;
    /**"拼装注单记录玩法")
     *
     */
    private String playOptionName;
    /**"注单所有可能结算结果json格式[单关可用]")
     *
     */
    private String betAllResult;

}


