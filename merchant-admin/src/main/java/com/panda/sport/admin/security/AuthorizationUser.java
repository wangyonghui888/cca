package com.panda.sport.admin.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @auth: YK
 * @Description:登录用户的账号和密码
 * @Date:2020/5/10 17:12
 */
@Getter
@Setter
public class AuthorizationUser {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "用户密码不能为空")
    private String password;

    @Override
    public String toString() {
        return "{username=" + username  + ", password= ******}";
    }
}
