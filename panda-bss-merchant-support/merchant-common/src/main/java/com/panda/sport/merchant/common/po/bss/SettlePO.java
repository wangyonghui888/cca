package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author： david
 * @version： V1.0.0
 * @Project Name : panda-bss
 * @Package Name : com.panda.sports.bss.lottery.po
 * @Description: 结算表
 * @date: 2019-10-11 16:34
 */
@Data
@ToString
public class SettlePO implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 注单号
     */
    private String betNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 结算状态（1赢，2输，3赢半，4输半，5走盘，6赛事取消，7赛事延期，8赛果不一致，9程序出现异常导致结算失败）
     */
    private Integer outCome;

    /**
     * 结算金额(最终要除以10000，并按照四舍六入五成双取2位小数)
     */
    private Double settleAmount;

    /**赔率*/
    private Double oddsValue;

    /**
     * 派彩状态 0 未派彩，1已派彩
     */
    private Integer payoutStatus;

    /**
     * 结算类型(1:自动结算，0：手工结算)
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
     * 修改人*
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 注单本金，x100后的本金
     */
    private Double betAmount;

    /**
     * 结算时的最新比分-获取结算订单信息
     */
    private String settleScore;

    /**
     * 净盈利
     */
    private Double profitAmount;
}