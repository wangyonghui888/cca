package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
    List<SystemConfig> querySystemConfig(SystemConfig po);

    int createSystemConfig(SystemConfig po);

    int updateSystemConfig(SystemConfig po);

    Long queryMaxId();
}
