package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.vo.MatchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MatchInfoMapper  extends BaseMapper<MatchInfo> {

    @Update("UPDATE m_match_info SET `status` = #{status} WHERE tab = #{tab} ")
    void updateByTab(String tab,Integer status);

    @Select("SELECT id,`name`,`tab`,`status`  FROM m_match_info WHERE tab = #{tab}")
    List<MatchInfo> selectListByTab(String tab);

    @Select("SELECT id,`name`,`tab`,`status` FROM m_match_info order by id asc")
    List<MatchInfo> selectAllList();
}
