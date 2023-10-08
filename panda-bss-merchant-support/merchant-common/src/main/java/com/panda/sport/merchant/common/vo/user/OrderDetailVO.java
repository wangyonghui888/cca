package com.panda.sport.merchant.common.vo.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;

/**
 * @author :  arlo
 * @Project Name : panda-bss-order
 * @Package Name : com.panda.sports.bss.order.vo
 * @Description : 注单详情
 * @Date: 2019-09-20 22:30
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrderDetailVO {
    //    /** 自动编号 */
//    private Long id;
    private static final long serialVersionUID = 2819257632275334581L;

    /**
     * 注单编号
     */
    private String betNo;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 用户编号
     */
    private Long uid;
    /**
     * 运动种类编号
     */
    private Integer sportId;

    private Long tournamentId;
    /**
     * 运动种类名称
     */
    private String sportName;
    /**
     * 玩法编号
     */
    private Integer playId;

    private Long childPlayId;

    /**
     * 玩法名称
     */
    private String playName;
    /**
     * 赛事编号
     */
    private Long matchId;
    /**
     * 赛事名称
     */
    private String matchName;
    /**
     * 类型：1 ：早盘 ，2： 滚球盘， 3： 冠军盘
     */
    private Integer matchType;
    /**
     * 最终赔率
     */
    private String oddFinally;

    /**
     * 是否自动接收最高赔率
     */
    private Integer acceptBetOdds;
    /**
     * 下注时间
     */
    private Long betTime;

    private Long closingTime;

    private String outrightYear;

    /**
     * 盘口类型( 1:欧盘 2:香港盘 3:美盘 4:印尼盘 5:马来盘)
     */
    private String marketType;
    /**
     * 盘口值
     */
    private String marketValue;
    /**
     * 对阵信息
     */
    private String matchInfo;
    /**
     * 注单金额
     */
    private Long betAmount;
    /**
     * 赔率值
     */
    private Double oddsValue;
    /**
     * 注单状态(0:无效 1:有效)
     */
    private Integer isValid;
    /**
     * 基准比分
     */
    private String scoreBenchmark;
    /**
     * 投注类型ID 对应上游投注项ID
     */
    private Long playOptionsId;
    /**
     * 投注类型(投注时下注的玩法选项)，规则引擎用
     */
    private String playOptions;
    /**
     * 玩法选项范围
     */
    private String playOptionsRange;
    /**
     * 备注
     */
    private String remark;

    /**
     * 赛事状态id
     */
    private Long matchStatusId;

    /**
     * 创建用户
     */
    private Integer matchOver;

    /**
     * 创建时间 从1970开始的以毫秒数为单位时间戳
     */
    private Long createTime;

    /**
     * 投注金额
     */
    private String orderAmountTotal;
    /**
     * 注单状态
     */
    private Integer betResult;

    /**
     * 比赛开始时间
     */
    private String startTime;

    /**
     * 投注盘id
     */
    private Long marketId;

    /**
     * 投注玩法
     */
    private String marketName;
    /**
     * 结算比分
     */
    private String settleScore;

    private String homeName;

    private String awayName;

    /**
     * 返还结果
     */
    private String backAmount;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 比赛开始时间
     */
    private Long beginTime;

    private String optionValue;

    private String tournamentPic;
    /**
     * 全场比分
     */
    private String score;

    private String phase;

    private String playOptionName;

    private Integer firstNum;

    private Integer secondNum;

    private String batchNo;

    /**
     * 取消类型 0未取消，1比赛取消，2比赛延期， 3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，
     * 10联赛错误，11比分错误，12电视裁判， 13主客场错误，14赛制错误，15赛程错误，16事件错误，17赛事提前，
     * 18自定义原因，19数据源取消，20比赛延迟，40PA手动拒单，41PA自动拒单，42业务拒单，43MTS拒单，46赔率调整中
     */
    private Integer cancelType;

    /**
     * 坑位
     */
    private Integer placeNum ;

    @FieldExplain("注单状态(0未结算 1已结算 2结算异常 3注单取消)")
    private Integer betStatus;

    @FieldExplain("冗余字段，虚拟赛事，格式：MatchDay||BatchNo")
    private String riskEvent;
}
