package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:09
 * @Version: 1.0
 */
@Data
public class MatchOddsPO {

    /**
     * 盘口ID
     */
    private String id;
    /**
     * 联赛id
     */
    private Long tournamentId;
    /**
     * 比赛id
     */
    private Long matchInfoId;
    /**
     * 玩法id
     */
    private Long playId;
    /**
     * 盘口类型。属于赛前盘或者滚球盘。1：赛前盘；0：滚球盘
     */
    private Integer marketType;
    /**
     * 该盘口具体显示的值。例如：大小球中，大小界限是： 3.5
     */
    private String marketValue;
    /**
     * 盘口名称
     */
    private String marketName;
    /**
     * 排序类型
     */
    private String orderType;
    /**
     * 数据来源。
     */
    private String dataSourceCode;
    /**
     * 盘口状态
     */
    private Integer status;
    /**
     * 盘口阶段
     */
    private Long matchProcessId;

    /**
     * 玩法名称
     */
    private String playName;
    /**
     * 备注。
     */
    private String remark;

    /**
     * 盘口显示值
     */
    private String[] title;

    /**
     * 盘口显示样式
     */
    private Integer oddsTemple;


}
