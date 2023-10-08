package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.entity
 * @Description :  TODO
 * @Date: 2022-03-11 14:55:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class LoginParamVo {

    @NotNull(message = "用户名不能为空")
    private String name;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "验证码不能为空")
    private Integer code;

}
