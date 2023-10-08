package com.panda.sport.merchant.common.po.bcorder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName AdminUser
 * @auth YK
 * @Description： bc后台管理用户
 * @Date 2020-09-01 11:53
 * @Version
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "admin_user")
public class BcAdminUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 用户角色ID
     */
    private Long roleId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后一次更新时间
     */
    private String lastPasswordResetTime;

}
