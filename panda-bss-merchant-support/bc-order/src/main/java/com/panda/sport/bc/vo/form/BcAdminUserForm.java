package com.panda.sport.bc.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName BcAdminUserForm
 * @auth YK
 * @Description 添加用户角色
 * @Date 2020-09-03 14:06
 * @Version
 */
@Setter
@Getter
public class BcAdminUserForm {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String avatar;

    private String email;

    @Digits(integer = 3,fraction = 0,message = "状态码格式不正确")
    private Integer enabled;

    private String phone;

    @Digits(integer = 20,fraction = 0,message = "用户权限格式不正确")
    private Long roleId;
}
