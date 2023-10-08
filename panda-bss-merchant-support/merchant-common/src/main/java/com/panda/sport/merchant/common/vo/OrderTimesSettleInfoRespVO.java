package com.panda.sport.merchant.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 订单多次结算账变
 * </p>
 *
 * @author amos
 * @since 2022-05-22
 */
@Getter
@Setter
@Accessors(chain = true)
public class OrderTimesSettleInfoRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表ID,自增
     */
    private Long id;

    /**
     * 用户表ID
     */
    private String uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 多次账变金额差
     */
    private BigDecimal negativeAmount;

    /**
     * 用户当前账户金额
     */
    private BigDecimal amount;

    /**
     * 赛事ID
     */
    private Long matchId;

    /**
     * 对阵信息
     */
    private String matchInfo;

    /**
     * 联赛名称
     */
    private String matchName;

    /**
     * 玩法名称
     */
    private String playName;

    /**
     *  变动原因
     */
    private String remark;

    /**
     * 投注项名称
     */
    private String playOptionName;

    /**
     * 二次结算原因
     *
     */
    private String changeReason;

    /**
     * 比赛开始时间
     */
    private String beginTime;

    /**
     * 最后一次账变金额
     */
    private BigDecimal lastChangeAmount;

    /**
     * 最后一次账变时间
     */
    private String lastChangeTime;

    /**
     * 最后一次账变后账户金额
     */
    private BigDecimal lastAfterChangeAmount;

    /**
     * 第一次账变金额
     */
    private BigDecimal firstChangeAmount;

    /**
     * 第一次账变时间
     */
    private String firstChangeTime;

    /**
     * 第一次账变前账户金额
     */
    private BigDecimal firstChangeBeforeAmount;


    /**
     * 是否赔偿 0: 不赔偿 1:待处理
     */
    private Integer isDamage ;

    /**
     * 是否赔偿 字符串  0: 不赔偿 1:待处理
     */
    private String isDamageStr;



}
