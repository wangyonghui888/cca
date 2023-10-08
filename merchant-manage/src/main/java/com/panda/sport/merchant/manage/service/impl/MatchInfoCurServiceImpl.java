package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.match.mapper.MatchInfoCurMapper;
import com.panda.sport.merchant.common.dto.LeagueDto;
import com.panda.sport.merchant.common.dto.MatchPalyDto;
import com.panda.sport.merchant.common.dto.SearchDto;
import com.panda.sport.merchant.common.dto.SearchMatchDto;
import com.panda.sport.merchant.common.po.bss.MatchInfoCur;
import com.panda.sport.merchant.common.po.bss.SLanguagePO;
import com.panda.sport.merchant.manage.service.IMatchInfoCurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 赛事基础信息表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-06-05
 */
@Service
public class MatchInfoCurServiceImpl extends ServiceImpl<MatchInfoCurMapper, MatchInfoCur> implements IMatchInfoCurService {

    @Override
    public List<LeagueDto> selectLeagues(Integer sportId) {
        return baseMapper.selectLeagues(sportId);
    }

    @Override
    public List<SearchMatchDto> searchById(SearchDto req) {
        return baseMapper.searchById(Long.valueOf(req.getKeyword()),req.getSportId(),req.getLeagueId());
    }

    @Override
    public List<SearchMatchDto> searchByKeyword(SearchDto req) {
        return baseMapper.searchByKeyword(req.getKeyword(),req.getSportId(),req.getLeagueId());
    }

    @Override
    public List<MatchPalyDto> selectPlaysByMatchId(Long id) {
        return baseMapper.selectPlaysByMatchId(id);
    }

    @Override
    public List<SLanguagePO> selectNameByIds(Set<Long> nameCodes) {
        return baseMapper.selectNameByIds(nameCodes);
    }
}
