package com.panda.sport.match.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.dto.AricleStatisticsDto;
import com.panda.sport.merchant.common.po.bss.TArticleStatistics;
import com.panda.sport.merchant.common.vo.AricleStatisticsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
public interface TArticleStatisticsMapper extends BaseMapper<TArticleStatistics> {

    List<AricleStatisticsVO> selectForPage(AricleStatisticsDto req);
}
