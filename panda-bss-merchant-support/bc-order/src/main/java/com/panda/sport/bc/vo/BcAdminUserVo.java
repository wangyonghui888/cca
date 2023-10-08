package com.panda.sport.bc.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName BcAdminUserVo
 * @auth YK
 * @Description  后台用户的输出
 * @Date 2020-09-03 14:01
 * @Version
 */
@Setter
@Getter
public class BcAdminUserVo {

    private Long id;

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
     * 用户角色ID
     */
    private Long roleId;

    private String roleName;

    private String roleNameEn;

    /**
     * 创建时间
     */

    private String createTime;

    /**
     * 最后一次更新时间
     */

    private String lastPasswordResetTime;

}
