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
import java.math.BigDecimal;

/**
 * <p>
 * 老虎机彩金领取记录
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-15
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@TableName("s_slots_lottery_records")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotsLotteryRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 老虎机名称
     */
    @TableField("slots_name")
    private String slotsName;

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
     * 老虎机id
     */
    @TableField("slots_id")
    private Long slotsId;

    /**
     * 商户ID
     */
    @TableField("merchant_code")
    private String merchantCode;

    /**
     * 消耗奖券数
     */
    @TableField("use_token")
    private Integer useToken;

    /**
     * 滚轴奖金(单位 分)
     */
    @TableField("award")
    private BigDecimal award;

    /**
     * 总计奖金(单位 分)
     */
    @TableField("total_award")
    private BigDecimal totalAward;

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
     * 领取时间
     */
    @TableField("create_time")
    private Long createTime;


}
