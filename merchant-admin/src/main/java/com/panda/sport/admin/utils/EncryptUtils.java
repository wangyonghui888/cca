package com.panda.sport.admin.utils;

import org.springframework.util.DigestUtils;

import java.util.Random;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/12 19:30
 */
public class EncryptUtils {

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String encryptPassword(String password){
        return  DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public  static String getStr(){
        StringBuilder numStr = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<10;i++){
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }
}
