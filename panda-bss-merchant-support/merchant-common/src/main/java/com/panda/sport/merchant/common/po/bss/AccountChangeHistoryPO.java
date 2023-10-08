package com.panda.sport.merchant.common.po.bss;


import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountChangeHistoryPO extends BaseVO {

    private String id;

    private Long accountId;

    private String uid;

    private String username;

    private String merchantCode;

    private String merchantName;

    private Long currentBalance;

    private Long beforeTransfer;

    private Long afterTransfer;

    private Double changeAmount;

    private Integer changeType;

    private Integer bizType;

    private String remark;

    public String getRemark() {
    	
    	if(remark != null && remark.contains("结算状态:")) return remark.substring(0, remark.indexOf("结算状态:")); 
    		
    	return remark;
    }
    
    private String orderNo;

    private String ipAddress;

    private String createUser;

    private Long createTime;

    private String modifyUser;

    private String currencyCode;

    private Long modifyTime;

}
