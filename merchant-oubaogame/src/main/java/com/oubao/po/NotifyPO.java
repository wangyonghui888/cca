package com.oubao.po;

import lombok.Data;

@Data
public class NotifyPO {

    /**
     * 版本号
     */
    private static final long serialVersionUID = -5591067906256831859L;


    /**
     * 真实姓名
     */
    private String merchantCode;

    /**
     * 手机号
     */
    private String transferId;



    /**
     * 邮箱
     */
    private String status;

    /**
     * 邮箱
     */
    private String orderList;
    /**
     * 邮箱
     */
    private String msg;

    /**
     * 备注
     */
    private Long timestamp;

    /**
     * 创建用户
     */
    private String signature;

}