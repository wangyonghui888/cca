package com.oubao.po;

import lombok.Data;


@Data
public class TLogHistory {
    //用户id
    private Long uid;
    //用户名
    private String username;
    //地址
    private String ipAddress;
    //登录时间
    private Long loginTime;
    //日志详情
    private String logDetail;
    //登录类型
    private Integer logType;
}