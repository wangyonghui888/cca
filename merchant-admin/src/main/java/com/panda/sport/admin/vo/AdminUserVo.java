package com.panda.sport.admin.vo;


import lombok.Data;


/**
 * @auth: YK
 * @Description:用户后台输出
 * @Date:2020/5/13 11:03
 */
@Data
public class AdminUserVo {

    private String id;

    private String username;

    /**
     * 密码
     */
   //private String password;

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
    private Long merchantId;

    /**
     * 商户code
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    //private String merchantName;

    /**
     * 父级商户code
     */
    private String parentMerchantCode;

    private Integer agentLevel;

    /**
     * 用户角色ID
     */
    private Long roleId;

    private String roleName;

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
}
