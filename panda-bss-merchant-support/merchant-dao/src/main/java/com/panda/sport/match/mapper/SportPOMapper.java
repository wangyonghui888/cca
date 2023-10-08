package com.panda.sport.match.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SportPO;
import com.panda.sport.merchant.common.vo.SportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标准球类表. 【数据来自融合表：standard_sport_type】 Mapper 接口
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
public interface SportPOMapper extends BaseMapper<SportPO> {

    List<SportVO> getListByIds(@Param("ids") List<Integer> acIds);

    Map<String, Object> getFromAndTo(@Param("marketId") Long marketId);

}
