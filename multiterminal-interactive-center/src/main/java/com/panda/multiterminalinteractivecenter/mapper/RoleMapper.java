package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.mapper
 * @Description :  TODO
 * @Date: 2022-03-12 16:30:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> queryRoleByUserId(Long userId);

    @Select("select * from m_role")
    List<Role> selectAll();

    @Select("select max(id) from m_role")
    Long maxId();
}
