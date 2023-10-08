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
 * 奥运拆盒历史记录
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_olympic_luckybox_records")
public class OlympicLuckyboxRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 盲盒类型，1：白银盲盒  2：黄金盲盒  3：钻石盲盒
     */
    @TableField("box_type")
    private Integer boxType;

    /**
     * 盲合id
     */
    @TableField("box_id")
    private Long boxId;

    /**
     * 拆盒次数
     */
    @TableField("open_number")
    private Integer openNumber;

    /**
     * 消耗奖券数
     */
    @TableField("use_token")
    private Integer useToken;

    /**
     * 单次奖金(单位 分)
     */
    @TableField("award")
    private Long award;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private String merchantId;

    /**
     * 商户名称
     */
    @TableField("merchant_account")
    private String merchantAccount;

    /**
     * 用户表id
     */
    @TableField("uid")
    private Long uid;

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
     * 活动类型记录:0 普通盲盒，1 奥运盲盒, 2 世界杯....
     */
    @TableField("activity_type")
    private Integer activityType;

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

    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Long modifyTime;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updatedBy;


}
