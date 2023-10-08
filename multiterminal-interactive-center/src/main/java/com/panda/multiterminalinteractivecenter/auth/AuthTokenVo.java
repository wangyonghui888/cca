package com.panda.multiterminalinteractivecenter.auth;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.auth
 * @Description :  TODO
 * @Date: 2022-03-13 17:04:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public class AuthTokenVo implements AuthenticationToken {
    private String token;
    public AuthTokenVo(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal(){
        return token;
    }

    @Override
    public Object getCredentials(){
        return token;
    }
}
