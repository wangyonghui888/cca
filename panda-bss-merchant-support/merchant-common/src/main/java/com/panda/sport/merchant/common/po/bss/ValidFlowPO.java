package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

import java.io.Serializable;

/**
 * @author david
 * @version V1.0.0
 * @Project Name : panda-bss
 * @Package Name : com.panda.sports.bss.lottery.po
 * @Description: 流水表
 * @Date: 2019-10-10 17:39
 */
@Data
public class ValidFlowPO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 金额
     */
    private Double amount;
    /**
     * 提现标记 0未体现 1以提现
     */
    private int backState;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 修改人*
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 最终赔率（欧赔）
     */
    private Double oddsValue;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 最终赔率
     */
    private String oddFinally;
}