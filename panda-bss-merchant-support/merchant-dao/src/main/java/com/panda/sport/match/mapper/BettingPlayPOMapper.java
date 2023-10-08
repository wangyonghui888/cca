package com.panda.sport.match.mapper;

import com.panda.sport.merchant.common.po.bss.BettingPlayPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.vo.BettingPlayVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
public interface BettingPlayPOMapper extends BaseMapper<BettingPlayPO> {

    List<BettingPlayVO> getListByIds(@Param("acIds") List<Integer> acIds);
}
