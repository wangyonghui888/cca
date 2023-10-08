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
 * 老虎机奖券合成记录
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-15
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slots_synthetic_records")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotsSyntheticRecords implements Serializable {

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
     * 消耗奖券数
     */
    @TableField("source_token")
    private Integer sourceToken;

    /**
     * 消耗奖券类型(名称)
     */
    @TableField("source_token_type")
    private String sourceTokenType;

    /**
     * 消耗奖券类型id
     */
    @TableField("source_token_id")
    private Long sourceTokenId;

    /**
     * 合成奖券数
     */
    @TableField("target_token")
    private Integer targetToken;

    /**
     * 合成奖券类型(名称)
     */
    @TableField("target_token_type")
    private String targetTokenType;

    /**
     * 合成奖券类型id
     */
    @TableField("target_token_id")
    private Long targetTokenId;

    /**
     * 返还奖券数
     */
    @TableField("return_token")
    private Integer returnToken;

    /**
     * 返还奖券类型(名称)
     */
    @TableField("return_token_type")
    private String returnTokenType;

    /**
     * 返还奖券类型id
     */
    @TableField("return_token_id")
    private Long returnTokenId;

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
     * 合成时间
     */
    @TableField("create_time")
    private Long createTime;


}
