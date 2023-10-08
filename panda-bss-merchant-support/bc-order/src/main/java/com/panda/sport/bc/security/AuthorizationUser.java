package com.panda.sport.bc.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName AuthorizationUser
 * @auth YK
 * @Description 登录用户的账号和密码
 * @Date 2020-09-01 11:39
 * @Version
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
