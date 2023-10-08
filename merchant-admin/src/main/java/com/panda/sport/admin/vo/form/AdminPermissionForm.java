package com.panda.sport.admin.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/14 17:57
 */
@Getter
@Setter
public class AdminPermissionForm {

    private Long id;

    @NotBlank(message = "别名不能为空")
    private String alias;

    @NotBlank(message = "名称不能为空")
    private String name;

    @Digits(integer = 10,fraction = 0,message = "父级格式不正确")
    private Integer pid;

    @Digits(integer = 10,fraction = 0,message = "菜单ID必须为整数")
    private Integer menuId;

}
