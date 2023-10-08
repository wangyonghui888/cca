package com.panda.sport.bc.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName AuthenticationInfo
 * @auth YK
 * @Description
 * @Date 2020-09-01 11:30
 * @Version
 */
@Getter
@AllArgsConstructor
public class AuthenticationInfo {

    private final String token;

    private final JwtUser user;
}
