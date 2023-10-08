package com.panda.multiterminalinteractivecenter.vo;

import com.panda.multiterminalinteractivecenter.entity.DataPermissions;
import com.panda.multiterminalinteractivecenter.entity.Permissions;
import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description :  TODO
 * @Date: 2022-03-13 15:16:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class RoleVo extends PageVo{

    private Long id;

    private String roleName;

    private List<Permissions> permissions;

    private List<PermissionsVo> menuTree;

    private List<DataPermissions> dataPermissions;

    private List<Long> permissionsIds;
}
