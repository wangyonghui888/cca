package com.panda.sport.merchant.common.po.bss;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户奖券变更历史记录
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SSlotsTokenChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;

    /**
     * 商户ID
     */
    private String merchantCode;

    /**
     * 用户uid
     */
    @JsonSerialize(using=ToStringSerializer.class)
    private Long uid;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 账变类型 1:任务奖励，2:盲盒消耗，3:合成奖励，4:游戏消耗，5:合成返还，6:合成消耗，7:提升消耗，8:道具奖励，9:系统补发
     */
    private int changeType;

    /**
     * 奖券类型
     */
    @TableField("slot_ticket_id")
    private long tokenType;
    /**
     * 奖券类型
     */
    @TableField("slot_ticket_name")
    private String tokenName;

    /**
     * 账变奖券数
     */
    private long changeToken;

    public String getChangeTokenStr() {
    	return changeToken < 0 ? changeToken+"" : "+"+changeToken;
    }
    
    /**
     * 转帐前金额
     */
    private long beforeToken;

    /**
     * 转帐后金额
     */
    private long afterToken;

    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 账变结果情况
     */
    private String changeResult;

    /**
     * 创建时间
     */
    private long createTime;


}
