package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_merchant")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户账号
     */
    @TableField("account")
    private String account;

    /**
     * 顶层商户id
     */
    @TableField("top_id")
    private Long topId;

    /**
     * 顶层商户账号
     */
    @TableField("top_account")
    private String topAccount;

    /**
     * 父商户id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 父商户账号
     */
    @TableField("parent_account")
    private String parentAccount;

    /**
     * 商户排序层级
     */
    @TableField("sort_level")
    private String sortLevel;

    /**
     * 商户层深
     */
    @TableField("deph")
    private Long deph;

    /**
     * 转账方式 （1：免转，0：非免转）
     */
    @TableField("transfer_type")
    private Integer transferType;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Integer createTime;

    /**
     * 商户状态(1：开启，0：关闭)
     */
    @TableField("status")
    private Integer status;

    /**
     * 结算固定费率
     */
    @TableField("bet_rate")
    private String betRate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 结算备注
     */
    @TableField("rate_remark")
    private String rateRemark;

    /**
     * 结算周期(1:月度)
     */
    @TableField("rate_cycle")
    private Integer rateCycle;

    /**
     * 结算日（默认每月10号）
     */
    @TableField("rate_day")
    private Integer rateDay;

    /**
     * 结算模式(1:固定,2阶梯)
     */
    @TableField("rate_pattern")
    private Integer ratePattern;

    /**
     * 结算方式(1:盈亏占比,2:投注额占比)
     */
    @TableField("rate_way")
    private Integer rateWay;

    /**
     * 秘钥
     */
    @TableField("secret_key")
    private String secretKey;

    /**
     * 商户密码
     */
    @TableField("password")
    private String password;

    /**
     * 商户类型(0：普通，1：信用)
     */
    @TableField("m_type")
    private Integer mType;


}
