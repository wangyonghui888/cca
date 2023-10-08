package com.oubao.po;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TAccountChangeHistoryPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户表ID
     */
    private Long accountId;
    /**
     * uid
     */
    private Long uid;
    private Long id;

    private String userName;
    /**
     * 当前账号余额
     */
    private BigDecimal currentBalance;
    /**
     * 变更金额
     */
    private BigDecimal changeAmount;
    /**
     * 变更类型 0 增加  1减少
     */
    private Integer changeType;
    /**
     * 账户变更业务类型 1：充值 2：提现 3：下注（减钱） 4：结算（加钱） 5：退款 6：冻结 7:(礼金)
     */
    private Long bizType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 修改用户
     */
    private String modifyUser;
    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 关联的订单号
     */
    private String orderNo;
    private String merchantCode;
}