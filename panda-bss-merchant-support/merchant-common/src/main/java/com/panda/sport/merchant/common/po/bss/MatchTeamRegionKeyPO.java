package com.panda.sport.merchant.common.po.bss;

/**
 *
 * @author :  sklee
 * @Description : s_match_team_region 赛事与球队关系表
 * @Date : 2019-10-01 11:18:12
*/
public class MatchTeamRegionKeyPO {
    /**
    * 赛事ID
    */
    private Long matchId;

    /**
    * 球队ID
    */
    private Long teamId;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}