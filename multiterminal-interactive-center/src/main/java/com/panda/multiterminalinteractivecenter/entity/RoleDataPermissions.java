package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.entity
 * @Description :  TODO
 * @Date: 2022-03-12 13:20:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@TableName("m_roles_data_permissions")
public class RoleDataPermissions {

    private Long id;

    private Long roleId;

    private Long dataPermissionId ;
}
