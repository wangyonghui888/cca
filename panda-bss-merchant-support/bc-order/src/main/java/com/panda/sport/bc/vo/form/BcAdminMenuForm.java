package com.panda.sport.bc.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName AdminMenuForm
 * @auth YK
 * @Description 添加后台的菜单
 * @Date 2020-09-03 12:15
 * @Version
 */
@Setter
@Getter
public class BcAdminMenuForm {

    private Long id;

    @NotBlank(message = "菜单名不能为空")
    private String name;

    @Digits(integer = 20,fraction = 0,message = "父级格式不正确")
    private Long pid;

    @Digits(integer = 20,fraction = 0,message = "排序格式不正确")
    private Long sort;

    @NotBlank(message = "菜单路径不能为空")
    private String path;
}
