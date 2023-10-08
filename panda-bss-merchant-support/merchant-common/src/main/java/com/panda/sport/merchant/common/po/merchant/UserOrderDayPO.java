package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOrderDayPO extends OrderPO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**rder/user/queryUserBetListByTime
     * 格式20200110
     */
    private Integer time;
    /**
     * 活跃天数
     */
    private Integer activeDays = 1;

    private BigDecimal orderValidBetMoney;
    /**
     * 结算有效投注额
     */
    private BigDecimal settleValidBetMoney;

    private String vipUpdateTime;

    /**
     * 有效投注笔数
     */
    private Integer orderValidBetCount;
    /**
     * 子项
     */
    private List<UserOrderDayPO> itemList;
}
