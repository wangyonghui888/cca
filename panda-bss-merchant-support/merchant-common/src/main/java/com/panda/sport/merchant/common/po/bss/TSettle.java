package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @auth: YK
 * @Description:结算单
 * @Date:2020/5/26 17:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TSettle implements Serializable {

    /**
     * 表ID，自增
     */
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 结算（2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期）
     */
    private Integer outCome;

    /**
     * 结算金额，是x100过后的金额数
     */
    private Long settleAmount;

    /**
     * 派彩状态 0 未派彩，1已派彩
     */
    private Integer payoutStatus;

    /**
     * 结算类型(2:结算回滚,1:自动结算，0：手工结算)
     */
    private Integer settleType;

    /**
     * 结算时间
     */
    private Long settleTime;

    /**
     * 最终赔率
     */
    private String oddFinally;

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
     * 备注（如赛事延期、比分有误等）
     */
    private String remark;

    /**
     * 注单本金
     */
    private Long betAmount;

    /**
     * 结算时的最新比分-获取结算订单信息
     */
    private String settleScore;

    /**
     * 注单赔率,固定2位小数 【欧洲赔率】
     */
    private Double oddsValue;

    /**
     * 净盈利
     */
    private Long profitAmount;

    /**
     * 结算ip地址
     */
    private String ip;

    /**
     * 是否为最终结算结果 1:是，0:否
     */
    private Integer lastSettle;
}
