package com.panda.sport.match.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.dto.LeagueDto;
import com.panda.sport.merchant.common.dto.MatchPalyDto;
import com.panda.sport.merchant.common.dto.SearchDto;
import com.panda.sport.merchant.common.dto.SearchMatchDto;
import com.panda.sport.merchant.common.po.bss.MatchInfoCur;
import com.panda.sport.merchant.common.po.bss.SLanguagePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 赛事基础信息表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-06-05
 */
@Repository
public interface MatchInfoCurMapper extends BaseMapper<MatchInfoCur> {

    List<LeagueDto> selectLeagues(@Param(value = "sportId") Integer sportId);

    List<SearchMatchDto> searchById(@Param("matchId") Long matchId,@Param("sportId") Long sportId,@Param("leagueId") Long leagueId);

    List<SearchMatchDto> searchByKeyword(@Param("keyword")String keyword, @Param("sportId")Long sportId, @Param("leagueId")Long leagueId);

    List<MatchPalyDto> selectPlaysByMatchId(@Param("id")Long id);

    List<SLanguagePO> selectNameByIds(@Param("nameCodes") Set<Long> nameCodes);


    /**
     * 更新
     * @param animation3Url
     * @return
     */
    Integer aniUpdate(@Param("animation3Url") String animation3Url);

    String getAnimationURL();
}
