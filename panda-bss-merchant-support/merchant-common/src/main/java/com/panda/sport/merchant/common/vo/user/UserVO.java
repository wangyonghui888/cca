package com.panda.sport.merchant.common.vo.user;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserVO implements Serializable {




    /**
     * 用户id
     */
    private String userId;

    /**
     * 币种(注册时必填)
     */
    private String currencyCode;

    /**
     * 用户等级
     */
    private Integer userLevel;

    /**
     * 用户状态 0启用 1禁用
     */
    private Integer disabled;

    /**
     * 用户名
     */
    private String username;

    private String merchantCode;

    /**
     * 真实姓名
     */
    private String realName;

    private String fakeName;


    private Integer vipLevel;

    /**
     * 创建时间
     */
    private Long createTime;


}