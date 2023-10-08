package com.panda.sport.merchant.common.vo.api;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PreBetOrderVo implements Serializable {

    private static final long serialVersionUID = 5241526151768786398L;

    /**
     * 注单号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 注单数量
     */
    private Integer betCount;

    /**
     * 串关类型
     */
    private Integer seriesType;

    /**
     * 串关类型值
     */
    private String seriesValue;

    /**
     * 订单创建时间
     */
    private Long createTime;

    /**
     * 订单更新时间
     */
    private Long modifyTime;

    /**
     * 投注总额
     */
    private String orderAmount;

    /**
     * 最高可赢金额
     */
    private BigDecimal maxWinAmount;

    /**
     * 订单明细列表
     */
    private List<PreBetOrderDetailVo> detailList;

    /**
     * 预投注订单状态(0预约中 ;1预约成功;2.风控预约失败;3.)风控取消预约注单.4.用户手动取消预约投注
     */
    private Integer preOrderStatus;

    /**
     * 用户名
     */
    private String userName;
}
