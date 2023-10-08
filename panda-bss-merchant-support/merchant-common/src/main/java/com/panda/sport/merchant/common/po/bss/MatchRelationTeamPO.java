package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:07
 * @Version: 1.0
 */
@Data
public class MatchRelationTeamPO {

    private  Integer id;
    /**
     * 球队ID
     */
    private  Integer teamId;

    /**
     * 比赛ID
     */
    private  Integer matchId;
    /**
     * 比赛中的作用。足球：主客队或者其他.home:主场队;away:客场队
     */
    private  String  matchPosition;

    /**
     * 参加该比赛时，球队的国际化名称，存档数据
     */
    private  Long  nameCode;

    /**
     * nameCode中文玩法名称.
     */
    private String nameText;


}
