package com.panda.sport.backup.mapper;


import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.vo.HotPlayNameVO;
import com.panda.sport.merchant.common.vo.TournamentVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SportMapper {

    List<SportPO> selectAll();

    @Select("select t.*,t.play_id as playId,l.zs playName from s_betting_play t LEFT JOIN s_language l on l.name_code=t.play_name_code")
    List<PlayPO> queryPlayList();

    List<SportRegionPO> queryTournamentList(@Param("tournamentLevel") Integer tournamentLevel, @Param("sportId") Integer sportId);

    List<Map<String, Object>> getAllSportList(@Param("language") String language);

    List<SportFilterVo> getAllSportListByFilter(@Param("language") String language);

    @Select("SELECT s.id, l.${language} name from s_sport s left join s_language l on s.name_code=l.name_code order by s.id asc")
    List<SportPO> getSportList(@Param("language") String language);

    @Select("SELECT s.id, l.${language} name from s_sport s left join s_language l on s.name_code=l.name_code order by s.id asc")
    List<Map<String, Object>> getNormalSportList(@Param("language") String language);

    List<TournamentVo> queryTournament(@Param("level") Integer level, @Param("sportName") String sportName,
                                       @Param("tournamentName") String tournamentName, @Param("language") String language);

    List<TournamentVo> queryTournamentBySportId(@Param("level") Integer level, @Param("sportId") Long sportId,
                                                @Param("tournamentName") String tournamentName, @Param("language") String language);

    List<TournamentVo> queryFilterTournamentBySportId(@Param("level") Integer level,
                                                      @Param("sportId") Long sportId,
                                                      @Param("tournamentName") String tournamentName,
                                                      @Param("language") String language,
                                                      @Param("dataSourceCode") String dataSourceCode);

    TournamentVo queryFilterTournamentById(@Param("id") Long id, @Param("language") String language);


    List<HotPlayNameVO> queryHotPlayName(@Param(value = "sportId") Integer sportId, @Param("language") String language);


    List<PullDownResultPO> listTournament(PullDownParamPO param);
}
