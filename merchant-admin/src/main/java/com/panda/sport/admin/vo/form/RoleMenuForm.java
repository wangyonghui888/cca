package com.panda.sport.admin.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @auth: YK
 * @Description:用户分配权限
 * @Date:2020/5/14 17:27
 */
@Setter
@Getter
public class RoleMenuForm {

    @NotNull(message = "角色必传")
    private Long roleId;

    @NotBlank(message = "分配菜单不能为空")
    private String menuId;

    @NotBlank(message = "分配权限不能为空")
    private String permissionId;
}
