package com.panda.sport.merchant.common.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BetApiVo implements Serializable {
    private static final long serialVersionUID = 5241526151768786394L;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 币种
     */
    private String currency;
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
     * 订单
     */
    private String orderNo;
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
     * 订单结果
     */
    private Integer outcome;
    /**
     * 派彩金额
     */
    private BigDecimal settleAmount;

    /**
     * 盈利金额
     */
    private BigDecimal profitAmount;
    /**
     * 提前结算注单金额
     */
    private BigDecimal preBetAmount;

    private BigDecimal maxWinAmount;
    /**
     * 结算时间
     */
    private Long settleTime;

    /**
     * 结算次数
     */
    private Integer settleTimes;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 订单明细列表
     */
    private List<BetDetailApiVo> detailList;

    /**
     * 设备类型
     */
    private String deviceType;

    private String ip;

    /**
     * 设备码
     */
    private String deviceImei;

    /**
     * VIP等级
     */
    private Integer vipLevel;


    /**投注前余额*/
    private BigDecimal beforeTransfer;

    /**投注后余额*/
    private BigDecimal afterTransfer;


    /**有效投注金额*/
    private BigDecimal validOrderAmount;

    /**
     * 预投注订单状态(0预约中 ;1预约成功;2.风控预约失败;3.)风控取消预约注单.4.用户手动取消预约投注
     */
    private Integer preOrderStatus;
}
