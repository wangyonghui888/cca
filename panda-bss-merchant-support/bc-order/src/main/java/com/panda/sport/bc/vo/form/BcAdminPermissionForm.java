package com.panda.sport.bc.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName BcAdminPermissionForm
 * @auth YK
 * @Description 添加权限
 * @Date 2020-09-03 13:43
 * @Version
 */
@Getter
@Setter
public class BcAdminPermissionForm {

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
