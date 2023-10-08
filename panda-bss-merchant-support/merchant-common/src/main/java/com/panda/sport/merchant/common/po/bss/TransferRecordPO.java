package com.panda.sport.merchant.common.po.bss;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRecordPO implements Serializable {

    private String transferId;

    private String merchantCode;

    private String merchantName;

    private Long userId;

    private String userName;

    /**
     * 1:加款,2扣款
     */
    private Integer transferType;

    private Double amount;

    private Double beforeTransfer;

    private Double afterTransfer;

    private Integer status;

    private String mag;
    /**
     * 1:免转,2:转账
     */
    private Integer transferMode;
    /**
     * 1:转入,2:转出,3:投注,4:结算,5:撤单
     */
    private Integer bizType;

    private Long createTime;

    private String orderStr;

    private Integer retryCount;
}
