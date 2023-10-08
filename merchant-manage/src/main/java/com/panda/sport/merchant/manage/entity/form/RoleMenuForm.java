package com.panda.sport.merchant.manage.entity.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class RoleMenuForm {

    @NotNull(message = "商户等级不能为空")
    private String agentLevel;

    @NotNull(message = "商户的code不能为空")
    private String code;

    @NotBlank(message = "分配菜单不能为空")
    private String menuId;

    @NotBlank(message = "分配权限不能为空")
    private String permissionId;
}
