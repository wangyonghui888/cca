package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("t_transfer_record_retry")
public class TransferRecordRetryPO {

    @TableId("id")
    private Long id;

    @TableField("transfer_id")
    private String transferId;

    @TableField("status")
    private Integer status;

    @TableField("mag")
    private String mag;

    @TableField("create_time")
    private Long createTime;

    @TableField("merchant_code")
    private String merchantCode;

    @TableField("user_name")
    private String userName;

    @TableField("biz_type")
    private Integer bizType;

    @TableField("amount")
    private Long amount;

    @TableField("create_user")
    private String createUser;
}
