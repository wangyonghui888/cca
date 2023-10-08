package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.DataPermissions;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.mapper
 * @Description :  TODO
 * @Date: 2022-03-12 16:31:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Repository
public interface DataRolePermissionsMapper extends BaseMapper<DataPermissions> {

    public List<DataPermissions> findDataPermissionsByRoleId(Long roleId);

    @Select("select * from m_data_permissions")
    public List<DataPermissions> selectAll();
}
