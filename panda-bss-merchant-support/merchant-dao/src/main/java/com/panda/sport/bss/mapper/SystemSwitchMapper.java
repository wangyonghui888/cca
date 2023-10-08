package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemSwitchMapper extends BaseMapper<SystemSwitchVO> {

    int updateSystemSwitch(SystemSwitchVO systemSwitchVO);

    List<SystemSwitchVO> querySystemSwitch();

    int updateConfigValue(SystemSwitchVO systemSwitchVO);
}
