package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.Permissions;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.mapper
 * @Description :  TODO
 * @Date: 2022-03-13 13:43:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface PermissionsMapper extends BaseMapper<Permissions> {

    public List<Permissions> findPermissionsByRoleId(Long roleId);

    public List<Long> findPermissionsIdsByRoleId(Long roleId);

    @Select("select * from m_permissions ")
    public List<Permissions> selectAll();

    @Select("select max(id) from m_permissions")
    Long maxId();
}
