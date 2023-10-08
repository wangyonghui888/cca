package com.panda.sport.admin.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/10 17:12
 */
@Getter
@AllArgsConstructor
public class AuthenticationInfo {

    private final String token;

    private final JwtUser user;

}
