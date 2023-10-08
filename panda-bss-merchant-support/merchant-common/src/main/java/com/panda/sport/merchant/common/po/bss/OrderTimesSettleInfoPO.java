package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

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
@TableName("t_order_times_settle_info")
public class OrderTimesSettleInfoPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表ID,自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户表ID
     */
    private Long uid;

    /**
     * 多次账变金额差
     */
    private Long negativeAmount;

    /**
     * 用户当前账户金额
     */
    private Long amount;

    /**
     * 赛事ID
     */
    private Long matchId;

    /**
     * 比赛开始时间
     */
    private Long beginTime;

    /**
     * 最后一次账变金额
     */
    private Long lastChangeAmount;

    /**
     * 最后一次账变时间
     */
    private Long lastChangeTime;

    /**
     * 最后一次账变后账户金额
     */
    private Long lastAfterChangeAmount;

    /**
     * 第一次账变金额
     */
    private Long firstChangeAmount;

    /**
     * 第一次账变时间
     */
    private Long firstChangeTime;

    /**
     * 第一次账变前账户金额
     */
    private Long firstChangeBeforeAmount;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long modifyTime;

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
     * 投注项名称
     */
    private String playOptionName;

    /**
     * 变动原因
     */
    private String remark;

    /**
     * 是否赔偿 0: 不赔偿 1:待处理
     */
    private Integer isDamage ;


}
