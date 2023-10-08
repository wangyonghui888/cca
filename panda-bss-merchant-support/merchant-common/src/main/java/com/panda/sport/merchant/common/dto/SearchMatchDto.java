package com.panda.sport.merchant.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMatchDto {
    private Long id;
    /**
     * 联赛id
     */
    private Long leagueId;
    /**
     * 联赛名称
     */
    private String leagueName;

    private Long leagueNameCode;
    /**
     * 比赛时间
     */
    private Long beginTime;
    /**
     * 主队名称
     */
    private String homeName;

    private Long homeNameCode;

    private Long awayNameCode;
    /**
     * 客队名称
     */
    private String awayName;

    @Getter
    @Setter
    public class Play {
        /**
         * 玩法id
         */
        private Long playId;
        /**
         * 玩法名称
         */
        private String playName;
    }
}
