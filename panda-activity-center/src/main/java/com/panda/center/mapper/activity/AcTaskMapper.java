package com.panda.center.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.center.entity.AcTask;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 活动任务表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-24
 */
public interface AcTaskMapper extends BaseMapper<AcTask> {

    Integer getCurMaxNo(@Param("actId") Integer actId);
}
