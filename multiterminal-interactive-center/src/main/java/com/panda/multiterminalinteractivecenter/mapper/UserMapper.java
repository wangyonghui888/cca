package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.mapper.xml
 * @Description :  TODO
 * @Date: 2022-03-11 14:58:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from m_user where name = #{name} and password = #{password}")
    User queryUserByNameAndPwd(String name,String password);


    @Select("select * from m_user where name = #{name}")
    User queryUserByName(String name);

    @Update("update m_user set secret = '' where id =  #{id} ")
    void restKey(Long id);

    @Select("select max(id) from m_user")
    Long maxId();
}
