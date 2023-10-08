package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 老虎机道具重置记录
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-15
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slots_prop_reset_records")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotsPropResetRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户表id
     */
    @TableField("uid")
    private String uid;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 商户ID
     */
    @TableField("merchant_code")
    private String merchantCode;

    /**
     * 道具类型(名称)
     */
    @TableField("prop_type")
    private String propType;

    /**
     * 道具赔率
     */
    @TableField("prop_times")
    private String propTimes;

    /**
     * 消耗奖券数
     */
    @TableField("use_token")
    private Integer useToken;

    /**
     * 消耗奖券类型(名称)
     */
    @TableField("use_token_type")
    private String useTokenType;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 重置时间
     */
    @TableField("create_time")
    private Long createTime;


}
