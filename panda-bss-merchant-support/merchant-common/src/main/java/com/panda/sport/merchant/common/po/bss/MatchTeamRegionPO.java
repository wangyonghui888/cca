package com.panda.sport.merchant.common.po.bss;

/**
 *
 * @author :  sklee
 * @Description : s_match_team_region 赛事与球队关系表
 * @Date : 2019-10-01 11:18:12
*/
public class MatchTeamRegionPO extends MatchTeamRegionKeyPO {
    /**
    * 主客队标识
    */
    private String matchPosition;

    /**
    * 描述
    */
    private String remark;

    public String getMatchPosition() {
        return matchPosition;
    }

    public void setMatchPosition(String matchPosition) {
        this.matchPosition = matchPosition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}