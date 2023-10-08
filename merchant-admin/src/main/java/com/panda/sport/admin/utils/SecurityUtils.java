package com.panda.sport.admin.utils;

import cn.hutool.json.JSONObject;
import com.panda.sport.admin.security.JwtUser;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @auth: YK
 * @Description:获取当前用户的账号和密码
 * @Date:2020/5/12 19:33
 */
public class SecurityUtils {


    public static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new AccountExpiredException("登录状态过期");
        }
        return userDetails;
    }

    public static JwtUser getUser() {
        JwtUser userDetails = null;
        try {
            userDetails = (JwtUser) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new AccountExpiredException("登录状态过期");
        }
        return userDetails;
    }


    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("username", String.class);
    }

    /**
     * 获取系统用户id
     *
     * @return 系统用户id
     */
    public static Long getUserId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("id", Long.class);
    }
}
