package com.panda.sport.bc.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName BcEditPwdForm
 * @auth YK
 * @Description 编辑密码
 * @Date 2020-09-03 14:24
 * @Version
 */
@Setter
@Getter
public class BcEditPwdForm {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String surePassword;

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
}
