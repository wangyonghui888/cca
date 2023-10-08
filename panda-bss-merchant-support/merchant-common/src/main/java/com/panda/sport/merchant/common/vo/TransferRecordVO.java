package com.panda.sport.merchant.common.vo;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class TransferRecordVO implements Serializable {

    private String transferId;

    private String merchantCode;

    private String merchantName;

    private String userId;

    private String userName;

    private String fakeName;

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
    private String currency;
}
