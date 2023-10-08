package com.panda.sport.match.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.TArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 赛事文章表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
public interface TArticleMapper extends BaseMapper<TArticle> {

    List<TArticle> selectDelay();

    void showBatch(List<Long> idList);

    void offlineBatch(List<Long> idList);

    void clearMatchInfo(@Param("id") Long id);
}
