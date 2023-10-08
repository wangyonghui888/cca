package com.panda.sport.merchant.common.vo;


import com.panda.sport.merchant.common.po.bss.BaseVO;
import lombok.Data;
import lombok.ToString;

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
public class AccountChangeHistoryFindVO extends BaseVO {

    private String id;

    private Long accountId;

    private String uid;

    private String username;

    private String fakeName;

    private String merchantCode;

    private String merchantName;

    private Long currentBalance;

    private Double beforeTransfer;

    private Double afterTransfer;

    private Double changeAmount;

    private Integer changeType;

    private Integer bizType;

    private String remark;

    private String orderNo;

    private String createUser;

    private Long createTime;

    private String modifyUser;

    private Long modifyTime;

    private String transferId;

    private String ipAddress;

    private String currencyCode;

    private String type;

}
