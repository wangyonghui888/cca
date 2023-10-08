package com.panda.sport.merchant.common.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 赛事基础信息表
 * </p>
 *
 * @author Auto Generator
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SMatchInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id. id
     */
    @NotNull(message = "SMatchInfoid cannot be null")
    private Long id;

    private String fristServise;

    /**
     * 体育种类id. 运动种类id 对应s_sport_type.id
     */
    @NotNull(message = "sportId cannot be null")
    private Long sportId;

    /**
     * 标准联赛 id. 对应联赛 id  对应  s_tournament.id
     */
    @NotNull(message = "tournamentId cannot be null")
    private Long tournamentId;

    /**
     * 第三方比赛id.
     */
    private Long thirdMatchId;

    /**
     * 是否为中立场。取值为 0  和1  。  1:是中立场，0:非中立场。操盘人员可手动处理
     */
    private Integer neutralGround;

    /**
     * 比赛开赛标识. 0: 未开盘; 1: 开盘; 2: 关盘; 3: 封盘; 开盘后用户可下注
     */
    @NotNull(message = "operateMatchStatus cannot be null")
    private Integer operateMatchStatus;

    /**
     * 赛事盘口状态 0:active 开, 1:suspended 封, 2:deactivated 关, 11:锁'',',
     */
    private Integer matchHandicapStatus;

    /**
     * 比赛开始时间. 比赛开始时间 UTC时间
     */
    @NotNull(message = "beginTime cannot be null")
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
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 修改时间.
     */
    @NotNull(message = "modifyTime cannot be null")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modifyTime;

    /**
     * 国际化
     */
    @NotNull(message = "nameCode cannot be null")
    private Long nameCode;

    /**
     * 是否开放赛前盘. 取值为 1  或  0.  1=开放; 0=不开放
     */
    @NotNull(message = "preMatchBusiness cannot be null")
    private Integer preMatchBusiness;

    /**
     * 赛事是否开放滚球. 取值为 1  或  0.  1=开放; 0=不开放
     */
    @NotNull(message = "liveOddBusiness cannot be null")
    private Integer liveOddBusiness;

    /**
     * 比赛是否结束。0: 未结束； 1：结束。（比赛彻底结束，双方不再有加时赛，点球大战，且裁判宣布结束）
     */
    @NotNull(message = "matchOver cannot be null")
    private Integer matchOver;

    /**
     * 全场比分
     */
    private String score;

    /**
     * 比赛阶段id
     */
    private Long matchPeriodId;

    /**
     * 赛事状态 sys_item_dict表parent_type_id字段值为5的数据
     */
    @NotNull(message = "matchStatus cannot be null")
    private Integer matchStatus;

    /**
     * 比赛剩余时间
     */
    private Long remainingTime;

    /**
     * 当前比赛进行时间
     */
    private Long secondsMatchStart;

    /**
     * 业务更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    //事件发生时间
    private Long eventTime;

    /**
     * 是否收藏
     */
    @TableField(exist = false)
    private Boolean collect = false;

    /**
     * 栏目类型 category   =1  滚球   category=2 即将开赛，  category =4   早盘
     */
    @TableField(exist = false)
    private Integer category;

    /**
     * 赛前开售时间
     */
    private Long preMatchTime;
    /**
     * 滚球开赛时间
     */
    private Long liveOddTime;
    /**
     * 赛制
     */
    private Integer matchFormat;
    /**
     * 中国竞彩的id
     */
    private String chinaBetting;
    /**
     * 滚球操盘平台 如： SR、MTS
     */
    private String liveRiskManagerCode;
    /**
     * 赛前操盘平台 如： Sr
     */
    private String preRiskManagerCode;
    /**
     * 盘数
     */
    private Integer secondNum;
    /**
     * 局数
     */
    private Integer firstNum;

    /**
     * '动画状态 -1 0:不可用 1:可用，暂未播放 2：可用，播放中',
     */
    private Integer animationStatus;

    /**
     * 视频状态 -1 0:不可用 1:可用，暂未播放 2：可用，播放中''',
     */
    private Integer videoStatus;

    /**
     * 是否展示角球玩法 Y:展示，N：不展示
     */
    private String displayCorner;
    /**
     * 赛前盘赛事列表显示数量
     */
    private Integer displayMarketCount;

    /**
     * 滚球盘赛事列表显示数量
     */
    private Integer liveMarketCount;

    /**
     * 是否显示罚球 默认展示 * Y：展示；N：不展示
     */
    private String displayPenalty;

    /**
     * 联赛等级
     */
    private Integer tournamentLevel;
    /**
     * 联赛语言编码
     */
    private Long tournamentNameCode;
    /**
     * 联赛logoUrl
     */
    private String tournamentLogoUrl;
    /**
     * 联赛是否热门
     */
    private Integer tournamentIsHot;
    /**
     * 赛种语言编码
     */
    private Long sprotNameCode;
    /**
     * 主队logoUrl
     */
    private String homeLogoUrl;
    /**
     * 主队球队id
     */
    private Long homeTeamId;
    /**
     * 客队logoUrl
     */
    private String awayLogoUrl;
    /**
     * 客队球队id
     */
    private Long awayTeamId;
    /**
     * 主队语言编码
     */
    private Long homeNameCode;
    /**
     * 客队语言编码
     */
    private Long awayNameCode;

    private String preMatchSellStatus;

    private String liveMatchSellStatus;

    private Integer matchLength;

    /**
     * 临时比分
     */
    private String matchTestScore;
    /**
     * 是否暂停 1: start  0:stop
     */
    private Integer isSuspend;

    /**
     * 最新事件编码
     */
    private String eventCode;
    /**
     * 赛季ID
     */
    private String seasonId;

    /**
     * 附加字段1
     */
    private String addition1;
    /**
     * 附加字段2
     */
    private String addition2;
    /**
     * 附加字段3
     */
    private String addition3;
    /**
     * 附加字段4
     */
    private String addition4;
    /**
     * 附加字段5
     */
    private String addition5;
    /**
     * 附加字段6
     */
    private String addition6;
    /**
     * 附加字段7
     */
    private String addition7;
    /**
     * 附加字段8
     */
    private String addition8;
    /**
     * 附加字段9
     */
    private String addition9;
    /**
     * 附加字段10
     */
    private String addition10;

    /**
     * 运营管理是否置顶热推 1 置顶热门  0 非置顶热推
     */
    private Integer hotStatus;

    /**
     * 热门状态  0-非热门  1-热门  2-推荐
     */
    private Integer operationHotSortTop;

    /**
     * 运营管理赛事排序 默认9999
     */
    private String operationHotSort;

    /**
     * 今日早盘赛事排序规则
     */
    @TableField(exist = false)
    private Integer operationSort;


    /**
     * 赛事类型 1  专业版本   2 标准版本，  3 新手版本
     */
    @TableField(exist = false)
    private String matchTypeInfo;

    /**
     * 运营管理联赛的排序
     */
    @TableField(exist = false)
    private Integer tournamentSort;

    /**
     * 运营管理联赛非置顶的排序
     */
    @TableField(exist = false)
    private Integer operationNotopSort;

    /**
     * 投注金额 货量
     */
    @TableField(exist = false)
    private String betAmount;

    /**
     * 权重
     */
    @TableField(exist = false)
    private Integer manualWeight;


    /**
     * 联赛语言编码排序用
     */
    @TableField(exist = false)
    private Long tournamentNameCodeTo;

    /**
     * 联赛表中的联赛等级
     */
    @TableField(exist = false)
    private Integer tournamentLevelTo;

    /**
     * 赛事文案信息
     */
    @TableField(exist = false)
    private String operationHotDesc;

    /**
     * 赛前操盘手Id
     */
    private String preTraderId;

    /**
     * 赛前操盘手名字
     */
    private String preTrader;

    /**
     * 滚球操盘手Id
     */
    private String liveTraderId;

    /**
     * 滚球操盘手名字
     */
    private String liveTrader;

    /**
     * 事件审核员Id
     */
    private String auditorId;

    /**
     * 事件审核员名字
     */
    private String auditor;

    /**
     * 主队阵容
     */
    private String homeFormation;

    /**
     * 客队阵容
     */
    private String awayFormation;


    /**
     * 联赛别名，国际化名称
     */
    private Long leagueAsNameCode;

    /**
     * 赛事最后一次关盘时间
     */
    @TableField(exist = false)
    private Long closeTime;

    /**
     * 是否展示罚牌 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    @TableField(exist = false)
    private Integer displayDisableBooking = 0;
    /**
     * 是否展示晋级 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    @TableField(exist = false)
    private Integer displayPromotion = 0;
    /**
     * 是否展示加时赛 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    @TableField(exist = false)
    private Integer displayOverTime = 0;
    /**
     * 是否展示点球大战 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    @TableField(exist = false)
    private Integer displayPenaltyShootout = 0;

    /**
     * 赛事类型：1、普通，2、电竞，3、篮球3x3
     */
    private Integer matchType;
}
