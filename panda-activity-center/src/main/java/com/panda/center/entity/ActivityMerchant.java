package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 活动商户配置表
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_activity_merchant")
public class ActivityMerchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户开启的活动，活动ID逗号分割
     */
    @TableField("activity_id")
    private String activityId;

    /**
     * 商户ID
     */
    @TableField("merchant_code")
    private String merchantCode;

    /**
     * 商户名称
     */
    @TableField("merchant_account")
    private String merchantAccount;

    /**
     * 状态(0,关闭;1,开启)
     */
    @TableField("status")
    private String status;

    /**
     * 活动入口状态 0：关闭 1：开启
     */
    @TableField("entrance_status")
    private Long entranceStatus;


}
