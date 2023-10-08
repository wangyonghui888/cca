package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.MatchInfoPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 赛事基础信息表 Mapper 接口
 * </p>
 *
 * @author christion
 * @since 2020-01-08
 */
@Repository
public interface SMatchInfoMapper extends BaseMapper<MatchInfoPO> {

    MatchInfoPO getMatchInfoByMid(@Param("mid") Long matchManageId);
}