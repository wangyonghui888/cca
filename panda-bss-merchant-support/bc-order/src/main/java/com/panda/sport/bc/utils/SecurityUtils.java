package com.panda.sport.bc.utils;

import cn.hutool.json.JSONObject;
import com.panda.sport.bc.security.JwtUser;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.util.DigestUtils;

import java.util.Random;

/**
 * @ClassName SecurityUtils
 * @auth YK
 * @Description 获取当前用户的账号和密码
 * @Date 2020-09-01 11:38
 * @Version
 */
public class SecurityUtils {

    /**
    * @description: 获取用户
    * @Param: []
    * @return: com.panda.sport.bc.security.JwtUser
    * @author: YK
    * @date: 2020/9/1 19:53
    */
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
    * @description: 获取用户名
    * @Param: []
    * @return: java.lang.String
    * @author: YK
    * @date: 2020/9/1 19:53
    */
    public static String getUsername() {
        Object obj = getUser();
        JSONObject json = new JSONObject(obj);
        return json.get("username", String.class);
    }

    /**
    * @description: 获取用户的ID
    * @Param: []
    * @return: java.lang.Long
    * @author: YK
    * @date: 2020/9/1 19:53
    */
    public static Long getUserId() {
        Object obj = getUser();
        JSONObject json = new JSONObject(obj);
        return json.get("id", Long.class);
    }


   /**
   * @description: 密码加密
   * @Param: [password]
   * @return: java.lang.String
   * @author: YK
   * @date: 2020/9/11 12:08
   */
    public static String encryptPassword(String password){
        return  DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
    * @description: 随机的字符串
    * @Param: []
    * @return: java.lang.String
    * @author: YK
    * @date: 2020/9/3 14:11
    */
    public  static String getStr(){
        StringBuilder numStr = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<10;i++){
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }
}
