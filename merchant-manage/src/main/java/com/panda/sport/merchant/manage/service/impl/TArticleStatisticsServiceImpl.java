package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.match.mapper.TArticleStatisticsMapper;
import com.panda.sport.merchant.common.dto.AricleStatisticsDto;
import com.panda.sport.merchant.common.po.bss.TArticleStatistics;
import com.panda.sport.merchant.common.vo.AricleStatisticsVO;
import com.panda.sport.merchant.manage.service.ITArticleStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Service
public class TArticleStatisticsServiceImpl extends ServiceImpl<TArticleStatisticsMapper, TArticleStatistics> implements ITArticleStatisticsService {

    @Override
    public List<AricleStatisticsVO> statisticsList(AricleStatisticsDto req) {
        return baseMapper.selectForPage(req);
    }
}
