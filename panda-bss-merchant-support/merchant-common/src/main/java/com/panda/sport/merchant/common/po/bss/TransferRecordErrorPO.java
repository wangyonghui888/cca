package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("t_transfer_record_error")
public class TransferRecordErrorPO {

    @TableId("transfer_id")
    private String transferId;

    @TableField("merchant_code")
    private String merchantCode;

    @TableField("user_id")
    private Long userId;
    /**
     * 1:加款,2扣款
     */
    @TableField("transfer_type")
    private Integer transferType;

    @TableField("amount")
    private Long amount;

    @TableField("before_transfer")
    private Long beforeTransfer;

    @TableField("after_transfer")
    private Long afterTransfer;

    @TableField("status")
    private Integer status;

    @TableField("mag")
    private String mag;
    /**
     * 1:免转,2:转账
     */
    @TableField("transfer_mode")
    private Integer transferMode;
    /**
     * 1:转入,2:转出,3:投注,4:结算,5:撤单
     */
    @TableField("biz_type")
    private Integer bizType;

    @TableField("create_time")
    private Long createTime;

    @TableField("orderStr")
    private String orderStr;
    
    @TableField("retry_count")
    private int retryCount;
}
