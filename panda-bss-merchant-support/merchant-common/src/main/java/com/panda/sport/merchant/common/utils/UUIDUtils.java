package com.panda.sport.merchant.common.utils;

import java.util.UUID;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : uuid生成工具
 * @Date: 2019-08-29 下午2:29:46
 */
public class UUIDUtils {

    public static String getUUID(){
        // 创建 GUID 对象
        UUID uuid = UUID.randomUUID();
        // 得到对象产生的ID
        String uuidString = uuid.toString();
        // 转换为大写
        //uuidString = uuidString.toUpperCase();
        // 替换 -
        uuidString = uuidString.replaceAll("-", "");
        return uuidString;

    }

    public static String createId() {
        UUID randomUUID = UUID.randomUUID();
        String uuid = randomUUID.toString().replaceAll("-", "");
        StringBuffer bf = new StringBuffer();
        bf.append(uuid);
        return bf.toString().toLowerCase();
    }

    public static void main(String[] args)
    {
        // 产生GUID
        System.out.println(getUUID());

    }
}
