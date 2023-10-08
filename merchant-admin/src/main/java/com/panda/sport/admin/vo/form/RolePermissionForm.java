package com.panda.sport.admin.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @auth: YK
 * @Description:角色和权限
 * @Date:2020/5/14 17:44
 */
@Getter
@Setter
public class RolePermissionForm {

    @NotNull(message = "角色必传")
    private Long roleId;

    @NotBlank(message = "分配权限不能为空")
    private String permissionId;
}
