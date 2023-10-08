package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

/**
 * @author :  arlo
 * @Project Name : panda-bss-order
 * @Package Name : com.panda.sports.bss.order.po
 * @Description : 注单详情PO
 * @Date: 2019-09-20 22:30
 */
@Data
@ToString
public class OrderDetailPO extends BaseVO<OrderDetailPO> {

    /** 自动编号 */
    private Long id;
    /** 注单编号 */
    private String  betNo;
    /** 订单编号 */
    private String  orderNo;
    /** 用户编号 */
    private Long  uid;
    /** 运动种类编号 */
    private Integer  sportId;
    /** 运动种类名称 */
    private String  sportName;
    /** 玩法编号 */
    private Long  playId;
    /** 玩法名称 */
    private String  playName;
    /** 赛事编号 */
    private Long  matchId;
    /** 类型：1 ：早盘 ，2： 滚球盘， 3： 冠军盘 */
    private Integer matchType;
    /** 最终赔率 ，因为有一些是1/20*/
    private String oddFinally;

    /** 是否自动接收最高赔率 */
    private Integer  acceptBetOdds;
    /** 下注时间 */
    private Long  betTime;

    /** 盘口类型(OU:欧盘 HK:香港盘 US:美式盘 ID:印尼盘 MY:马来盘 GB:英式盘） */
    private String  marketType;
    /** 盘口值 */
    private String  marketValue;
    /** 对阵信息 */
    private String  matchInfo;
    /** 注单金额 */
    private Double  betAmount;
    /** 赔率值 */
    private Double  oddsValue;
    /** 最高可赢金额 */
    private Double  maxWinAmount;
    /** 注单状态(0未结算 1已结算 2结算异常)*/
    private Integer  betStatus;
    /** 基准比分 */
    private String scoreBenchmark;
    /** 投注类型ID 对应上游投注项ID */
    private Long playOptionsId;
    /** 投注类型(投注时下注的玩法选项)，规则引擎用 */
    private String playOptions;
    /** 玩法选项范围 */
    private String playOptionsRange;
    /** 备注 */
    private String  remark;

    /** 赛事状态id */
    private Long matchProcessId;
    /** 联赛id */
    private Long tournamentId;
    /** 赛事id */
    private Long marketId;
    /** 最终盘口类型*/
    private String marketTypeFinally;
    /** 是否需要和风控赛果进行对比 1:是，0：否*/
    private Integer result;
    /** 赛事名称*/
    private String matchName;
    /** 注项结算结果0-无结果  2-走水  3-输 4-赢 4-赢一半 5-输一半*/
    private Integer betResult;
    /** 负盘附加值*/
    private Double addition;
    /** 拼装注单记录玩法 */
    private String playOptionName;
}
