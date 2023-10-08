package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.dto.LeagueDto;
import com.panda.sport.merchant.common.dto.MatchPalyDto;
import com.panda.sport.merchant.common.dto.SearchDto;
import com.panda.sport.merchant.common.dto.SearchMatchDto;
import com.panda.sport.merchant.common.po.bss.MatchInfoCur;
import com.panda.sport.merchant.common.po.bss.SLanguagePO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 赛事基础信息表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-06-05
 */
public interface IMatchInfoCurService extends IService<MatchInfoCur> {

    List<LeagueDto> selectLeagues(Integer sportId);

    List<SearchMatchDto> searchById(SearchDto req);

    List<SearchMatchDto> searchByKeyword(SearchDto req);

    List<MatchPalyDto> selectPlaysByMatchId(Long id);

    List<SLanguagePO> selectNameByIds(Set<Long> nameCodes);
}
