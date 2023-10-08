package com.panda.sport.merchant.common.vo;

import lombok.Data;

/**
 * @auth: YK
 * @Description:用户角色
 * @Date:2020/5/12 14:48
 */
@Data
public class AdminRoleVO {

    private Long id;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 用户角色名称
     */
    private String name;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 用户校色所属商户
     */
    private String merchantCode;

    /**
     * 创建人
     */
    private String createUsername;

    /**
     * 授权时间
     */
    private String authorizedTime;

    /**
     * 超级管理员 0否 1是
     */
    private Integer isAdmin;
}
