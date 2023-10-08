package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @auth: YK
 * @Description:后台管理用户
 * @Date:2020/5/12 11:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminUser {

    @TableId(value = "id")
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    private String avatar;

    /**
     * 密码
     */
    private String email;

    /**
     * 是否禁用
     */
    private Integer enabled;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 所属商户ID
     */
    private String merchantId;

    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 父级商户code
     */
    private String parentMerchantCode;

    /**
     * 代理级别，0:直营，1渠道 ，2二级
     */
    private Integer agentLevel;

    /**
     * 用户角色ID
     */
    private Long roleId;

    /**
     * 超级管理员 0否 1是
     */
    private Integer isAdmin;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后一次更新时间
     */
    private String lastPasswordResetTime;

    /**
     * 密码明文
     */
    private String pswCode;

}
