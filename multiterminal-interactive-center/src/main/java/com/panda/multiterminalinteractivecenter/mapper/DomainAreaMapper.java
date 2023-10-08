package com.panda.multiterminalinteractivecenter.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.DomainArea;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DomainAreaMapper extends BaseMapper<DomainArea> {
    void logicDeleteById(@Param("id") Long id);

    List<DomainArea> pageList(@Param("page")Integer page,@Param("size") Integer size,@Param("name") String name);

    List<JSONObject> simpleList(@Param("name") String name);

    int countByName(@Param("name")String name,@Param("id")Long id);

    DomainArea getById(@Param("id") Long id);
}
