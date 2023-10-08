package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author :  david
 * @Version: V1.0.0
 * @Project Name :panda-bss
 * @Package Name :com.panda.sports.bss.lottery.po
 * @Description :结算历史回滚
 * @Since: 2019-10-14 15:31
 */
@Data
@ToString
public class SettleHistoryRollbackPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 下注号码
     */
    private String betNo;
    /**
     * 结算金额
     */
    private Long setAmount;


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
}