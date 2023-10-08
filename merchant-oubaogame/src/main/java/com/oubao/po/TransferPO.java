package com.oubao.po;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TransferPO {

    /**
     * 版本号
     */
    private static final long serialVersionUID = -5591067906256831859L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户登录密码
     */
    private Integer bizType;

    /**
     * 真实姓名
     */
    private String merchantCode;

    /**
     * 手机号
     */
    private String transferId;

    /**
     * 身份证号
     */
    private Double amount;

    /**
     * 币种(注册时必填)
     */
    private Integer transferType;

    /**
     * 邮箱
     */
    private String orderList;

    /**
     * 备注
     */
    private Long timestamp;

    /**
     * 创建用户
     */
    private String signature;

}