package com.panda.sport.merchant.common.vo.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Z9-velpro
 */
@Data
public class MatchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赛事id
     */
    private Long matchId;
    /**
     * 对阵名zs
     */
    private String matchInfoZs;
    /**
     * 对阵名en
     */
    private String matchInfoEn;
    /**
     * 球种zs
     */
    private String sportNameZs;
    /**
     * 球种en
     */
    private String sportNameEn;
    /**
     * 联赛zs
     */
    private String tournamentNameZs;
    /**
     * 联赛en
     */
    private String tournamentNameEn;
    /**
     * 开赛时间zs
     */
    private String beginTimeZs;
    /**
     * 开赛时间en
     */
    private String beginTimeEn;


}
