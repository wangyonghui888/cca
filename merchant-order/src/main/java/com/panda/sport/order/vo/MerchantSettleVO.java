package com.panda.sport.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @author : Jeffrey
 * @Date: 2020-01-07 10:44
 * @Description : 商户结算
 */
@Data
public class MerchantSettleVO implements Serializable {

    /**
     * 数据源给的赛果
     */
    private String sourceResult;
    /**
     * 用户id
     */
    private Long userId;

    private String userName;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 结算金额，是x10000过后的金额数
     */
    private Double settleAmount;

    /**
     * 派彩状态 0 未派彩，1已派彩
     */
    private Integer payoutStatus = 0;

    /**
     * 结算类型(1:自动结算，0：手工结算)
     */
    private Integer settleType = 1;
    /**
     * 结算时间
     */
    private Long settleTime;
    /**
     * 最终赔率
     */
    private String oddFinally;
    /**赔率*/
    private Double oddsValue;

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
     * 注单本金 说明该值是乘以100之后的金额
     */
    private Double betAmount;
    /**
     * 结算时的最新比分-获取结算订单信息
     */
    private String settleScore;
    /**
     * 结算（2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期）
     */
    private Integer outCome;
    /**
     * 订单结算状态 0：未结算  1：已结算  2：结算异常
     */
    private Integer orderStatus;

    /**
     * 商户编码
     */
    private String merchantCode;
/*
    *//**
     * 订单明细
     *//*
    List<MerchantSettleDetailVO> settleDetailList;*/

}
