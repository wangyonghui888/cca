package com.panda.sport.merchant.common.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.po.bss.BaseVO;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.order.vo
 * @Description: 账变历史
 * @Date: 2019/10/10 11:06
 * @Version: 1.0
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountChangeHistoryVO {

    private String id;

    private Long accountId;

    private String uid;

    private BigDecimal currentBalance;

    private BigDecimal beforeTransfer;

    private BigDecimal afterTransfer;

    private BigDecimal changeAmount;

    private Integer changeType;

    private Integer bizType;

    private String remark;

    private String orderNo;
    private String createUser;
    private Long createTime;
    private String modifyUser;
    private Long modifyTime;
    private String currencyCode;
    private String ipAddress;


}
