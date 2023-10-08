package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户奖券变更历史记录
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-15
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slots_token_change_history")
public class SlotsTokenChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_code")
    private String merchantCode;

    /**
     * 用户uid
     */
    @TableField("uid")
    private String uid;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 账变类型 1:任务奖励，2:盲盒消耗，3:合成奖励，4:游戏消耗，5:合成返还，6:合成消耗，7:提升消耗，8:道具奖励，9:系统补发
     */
    @TableField("change_type")
    private String changeType;

    /**
     * 奖券类型
     */
    @TableField("token_type")
    private String tokenType;

    /**
     * 账变奖券数
     */
    @TableField("change_token")
    private String changeToken;

    /**
     * 转帐前金额
     */
    @TableField("before_token")
    private String beforeToken;

    /**
     * 转帐后金额
     */
    @TableField("after_token")
    private String afterToken;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
