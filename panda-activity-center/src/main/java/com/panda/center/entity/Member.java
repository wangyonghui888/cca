package com.panda.center.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_member")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("uid")
    private Long uid;

    /**
     * 父商户ID
     */
    @TableField("parent_merchant_id")
    private Long parentMerchantId;

    /**
     * 父商户名称
     */
    @TableField("parent_merchant_account")
    private String parentMerchantAccount;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商户名称
     */
    @TableField("merchant_account")
    private String merchantAccount;

    /**
     * 用户账号
     */
    @TableField("account")
    private String account;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 顶层商户id
     */
    @TableField("top_merchant_id")
    private Long topMerchantId;

    /**
     * 顶层商户账号
     */
    @TableField("top_merchant_account")
    private String topMerchantAccount;

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
     * 注册时间
     */
    @TableField("register_time")
    private Long registerTime;

    /**
     * 最后一次登录时间
     */
    @TableField("last_login_time")
    private Long lastLoginTime;

    /**
     * 上次登录ip
     */
    @TableField("last_login_ip")
    private Long lastLoginIp;

    /**
     * 会员状态(1:正常,2资金冻结,3:限制登录,4:只可资金转出)
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否测试账号(0:正常账号,1:测试账号,2:信用账户)
     */
    @TableField("tester")
    private Integer tester;

    /**
     * 会员限红开关 1 开启 0未开启/关闭
     */
    @TableField("limit_status")
    private Integer limitStatus;

    /**
     * 在线状态
     */
    @TableField("online_status")
    private Integer onlineStatus;

    /**
     * 1 自动接受最新赔率 2自动接受更高赔率 3永不接受最新赔率
     */
    @TableField("odd_update_type")
    private Integer oddUpdateType;

    /**
     * 直属代理ID
     */
    @TableField("agent_id")
    private Long agentId;

    /**
     * 直属代理账号
     */
    @TableField("agent_account")
    private String agentAccount;

    /**
     * 最后登录token
     */
	/*@TableField("token")
	private String token;*/


}
