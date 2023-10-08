package com.panda.sport.merchant.common.po.bss;

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
 * 用户老虎机奖券表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-15
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slots_user_token")
public class SlotsUserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId("user_id")
    private Long userId;

    /**
     * 拥有奖券数量
     */
    @TableField("token_num")
    private Long tokenNum;

    /**
     * 奖券类型id
     */
    @TableField("token_id")
    private Long tokenId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 最后更新时间(yyyyMMddHH)
     */
    @TableField("last_update_time")
    private Long lastUpdateTime;


}
