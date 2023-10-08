package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @Author :  david
 * @Version: V1.0.0
 * @Project Name :panda-bss
 * @Package Name :com.panda.sport.merchant.common.po
 * @Description :登录推出相关日志
 * @Since: 2019-11-05 21:37
 */
@Data
public class TLogHistory {
    //用户id
    private Long uid;
    //用户名
    private String username;
    private String merchantCode;
    //地址
    private String ipAddress;
    //登录时间
    private Long loginTime;
    //日志详情
    private String logDetail;
    //登录类型  1: 登录  2:跳转
    private Integer logType;
}