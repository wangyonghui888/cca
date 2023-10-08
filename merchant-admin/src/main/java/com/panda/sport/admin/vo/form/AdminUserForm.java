package com.panda.sport.admin.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

/**
 * @auth: YK
 * @Description:添加后台用户
 * @Date:2020/5/13 19:48
 */
@Setter
@Getter
public class AdminUserForm {


    private String id;

    @NotBlank(message = "用户名不能为空")
    private String username;


    private String password;

    private String avatar;

    private String email;

    @Digits(integer = 3,fraction = 0,message = "状态码格式不正确")
    private Integer enabled;

    private String phone;

//    @Digits(integer = 20,fraction = 0,message = "商户ID格式不正确")
//    private Long merchantId;
//
//    @NotBlank(message = "商户CODE不能为空")
//    private String merchantCode;
//
//    private String merchantName;
//
//    private String parentMerchantCode;

//    @Max(value = 5,message = "代理级别参数超过范围")
//    private Integer agentLevel;

    @Digits(integer = 20,fraction = 0,message = "用户权限格式不正确")
    private Long roleId;

}
