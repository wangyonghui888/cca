package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.dto.AricleStatisticsDto;
import com.panda.sport.merchant.common.po.bss.TArticleStatistics;
import com.panda.sport.merchant.common.vo.AricleStatisticsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
public interface ITArticleStatisticsService extends IService<TArticleStatistics> {

    List<AricleStatisticsVO> statisticsList(AricleStatisticsDto req);
}
