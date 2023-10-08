package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 *
 * @author :  sklee
 * @Description : s_match_info 
 * @Date : 2019-09-30 07:49:20
*/
@Data
public class MatchInfoPO {
    /**
    * id. id
    */
    private Long id;

    /**
    * 体育种类id. 运动种类id 对应sport.id
    */
    private Long sportId;

    /**
    * 标准联赛 id. 对应联赛 id  对应  standard_sport_tournament.id
    */
    private Long standardTournamentId;
    /**
     * 是否中立场
     */
    private Integer neutralGround;
    /**
    * 第三方比赛id. 第三方比赛在 表 third_match_info 中的id
    */
    private String thirdMatchId;

    /**
    * 是否支持串关. 1 支持串关;0 不支持串关
    */
    private Integer canParlay;

    /**
    * 比赛开盘标识. 0: 未开盘; 1: 开盘; 2: 关盘; 3: 封盘; 开盘后用户可下注
    */
    private Integer operateMatchStatus;

    /**
    * 比赛开始时间. 比赛开始时间 UTC时间
    */
    private Long beginTime;



    /**
    * 标准赛事编码. 用于管理的赛事id
    */
    private Long matchManageId;

    /**
    * 风控服务商编码. sr bc pa 等. 详见 数据源表 data_source中的code字段
    */
    private String riskManagerCode;

    /**
    * 数据来源编码. 取值见: data_source.code
    */
    private String dataSourceCode;

    /**
    * 备注. 
    */
    private String remark;

    /**
    * 创建时间. 
    */
    private Long createTime;

    /**
    * 修改时间. 
    */
    private Long modifyTime;

    /**
    * 
    */
    private String createUser;

    /**
    * 
    */
    private String modifyUser;

    private Long nameCode;

    /**
     * 赛事是否开放赛前盘. 取值为 1  或  0.  1=开放; 0=不开放
     */
    private Integer preMatchBusiness;

    /**
     * 赛事是否开放滚球. 取值为 1  或  0.  1=开放; 0=不开放
     */
    private Integer liveOddBusiness;

    /**
     * 比分.  仅显示 90分钟内的比分.
     */
    private String score;

    /**
     * 比赛阶段id. 取自基础表 : match_status.id
     */
    private Long matchPeriodId;

    /**
     * 比赛是否结束. 0: 未结束;  1: 结束. （比赛彻底结束, 双方不再有加时赛, 点球大战, 且裁判宣布结束）
     */
    private Integer matchOver;

    /**
     * 赛事状态
     */
    private Integer matchStatus;

    /**
     * 联赛语言编码
     */
    private Long tournamentNameCode;

    /**
     * 主队名称
     */
    private Long homeNameCode;

    /**
     * 赛事状态
     */
    private Long awayNameCode;

}