package com.panda.sport.admin.vo.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @auth: YK
 * @Description:修改用户账号
 * @Date:2020/6/14 14:07
 */
@Data
public class EditPwdForm {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String surePassword;

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

}
