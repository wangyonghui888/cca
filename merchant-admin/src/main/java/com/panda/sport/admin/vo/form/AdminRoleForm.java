package com.panda.sport.admin.vo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @auth: YK
 * @Description:用户角色的的添加
 * @Date:2020/5/14 16:13
 */
@Setter
@Getter
public class AdminRoleForm {

    private Long id;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    private String remark;
}
