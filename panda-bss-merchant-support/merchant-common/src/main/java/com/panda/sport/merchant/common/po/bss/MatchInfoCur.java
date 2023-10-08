package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 赛事基础信息表
 * </p>
 *
 * @author baomidou
 * @since 2022-06-05
 */
@TableName("s_match_info_cur")
public class MatchInfoCur implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id. id
     */
    private Long id;

    /**
     * 体育种类id. 运动种类id 对应s_sport_type.id
     */
    private Long sportId;

    /**
     * 标准联赛 id. 对应联赛 id  对应  s_tournament.id
     */
    private Long tournamentId;

    /**
     * 赛事操盘状态 0:active 开, 1:suspended 封, 2:deactivated 关, 11:锁',
     */
    private Integer matchHandicapStatus;

    /**
     * 第三方比赛id.
     */
    private Long thirdMatchId;

    /**
     * 是否为中立场。取值为 0  和1  。  1:是中立场，0:非中立场。操盘人员可手动处理
     */
    private Integer neutralGround;

    /**
     * 比赛开盘标识. 0: 未开盘; 1: 开盘; 2: 关盘; 3: 封盘; 开盘后用户可下注；11：锁盘
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
     * 赛事场地国际化编码
     */
    private Long nameCode;

    /**
     * 是否开放赛前盘. 取值为 1  或  0.  1=开放; 0=不开放
     */
    private Integer preMatchBusiness;

    /**
     * 赛事是否开放滚球. 取值为 1  或  0.  1=开放; 0=不开放
     */
    private Integer liveOddBusiness;

    /**
     * 比赛是否结束。0: 未结束； 1：结束。（比赛彻底结束，双方不再有加时赛，点球大战，且裁判宣布结束）
     */
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
     * 赛事状态：0未开赛，1 进行中  4 结束
     */
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
     * 是否补时 0未开,1开
     */
    private Integer stoppageStatus;

    /**
     * 补时
     */
    private Long stoppageTime;

    /**
     * 开始补时时间
     */
    private Long stoppageEventTime;

    /**
     * 业务更新时间
     */
    private Long updateTime;

    /**
     * 中国竞彩的id
     */
    private String chinaBetting;

    /**
     * 赛制
     */
    private Integer matchFormat;

    /**
     * 赛前开售时间
     */
    private Long preMatchTime;

    /**
     * 滚球开售时间
     */
    private Long liveOddTime;

    /**
     * 比赛先发球的球队:home away
     */
    private String fristServise;

    /**
     * 滚球操盘平台 如： SR、MTS
     */
    private String liveRiskManagerCode;

    /**
     * 赛前操盘平台 如： SR',
     */
    private String preRiskManagerCode;

    /**
     * 在网球里该字段显示局，乒乓，斯若克，羽毛球都是0展示
     */
    private Integer secondNum;

    /**
     * 动画状态  -1前端不用展示,0:不可用 1:可用，暂未播放 2：可用，播放中
     */
    private Integer animationStatus;

    /**
     * 视频状态 -1前端不用展示, 0:不可用 1:可用，暂未播放 2：可用，播放中'
     */
    private Integer videoStatus;

    /**
     * 在网球里该字段显示盘，乒乓，斯若克，羽毛球都是局表示
     */
    private Integer firstNum;

    /**
     * 是否展示角球玩法 Y:展示，N：不展示
     */
    private String displayCorner;

    /**
     * 赛前盘赛事列表显示数量
     */
    private Long displayMarketCount;

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
     * 联赛是否热门 1:热门，0： 非热门
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

    /**
     * 赛前是否开售 未售 Unsold, 取消预售 Cancel_Sold, 开售Sold, 停售 Stop_Sold, 数据源停售 Expected_End_Sold
     */
    private String preMatchSellStatus;

    /**
     * 滚球是否开售 未售 Unsold, 取消预售 Cancel_Sold, 开售Sold, 停售 Stop_Sold, 数据源停售 Expected_End_Sold
     */
    private String liveMatchSellStatus;

    /**
     * 赛节
     */
    private Integer matchLength;

    /**
     * 赛事临时比分
     */
    private String matchTestScore;

    /**
     * 是否暂停 1: start  0:stop
     */
    private Integer isSuspend;

    /**
     * 赛事最新事件编码
     */
    private String eventCode;

    /**
     * 事件发生时间
     */
    private Long eventTime;

    /**
     * 赛季ID
     */
    private String seasonId;

    /**
     * 滚球盘赛事列表显示数量
     */
    private Long liveMarketCount;

    /**
     * 附件字段1
     */
    private String addition1;

    /**
     * 附件字段2
     */
    private String addition2;

    /**
     * 附件字段3
     */
    private String addition3;

    /**
     * 附件字段4
     */
    private String addition4;

    /**
     * 附件字段5
     */
    private String addition5;

    /**
     * 附件字段6
     */
    private String addition6;

    /**
     * 附加字段7
     */
    private String addition7;

    /**
     * 附件字段8
     */
    private String addition8;

    /**
     * 附件字段9
     */
    private String addition9;

    /**
     * 附件字段10
     */
    private String addition10;

    /**
     * 是否热门赛事
     */
    private Integer hotStatus;

    /**
     * 运营管理是否置顶热推 1 置顶热门  0 非置顶热推
     */
    private Integer operationHotSortTop;

    /**
     * 运营管理赛事排序
     */
    private String operationHotSort;

    /**
     * 运营管理赛事文案
     */
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
     * 主队阵容
     */
    private String homeFormation;

    /**
     * 客队阵容
     */
    private String awayFormation;

    /**
     * 联赛别名NameCode
     */
    private Long leagueAsNameCode;

    /**
     * 事件审核员Id
     */
    private String auditorId;

    /**
     * 事件审核员名字
     */
    private String auditor;

    /**
     * 运营管理，按联赛排序赛事,联赛顺序
     */
    private Integer operationNotopSort;

    /**
     * 货量
     */
    private BigDecimal betAmount;

    /**
     * 权重
     */
    private Integer manualWeight;

    /**
     * 完赛创建时间
     */
    private Long closeTime;

    /**
     * 是否展示罚牌 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    private Integer displayDisableBooking;

    /**
     * 是否展示晋级 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    private Integer displayPromotion;

    /**
     * 是否展示加时赛 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    private Integer displayOverTime;

    /**
     * 是否展示点球大战 0:无盘口，1:早盘，2:滚球(包含早盘)
     */
    private Integer displayPenaltyShootout;

    /**
     * 赛事类型：1、普通，2、电竞，3、篮球3x3
     */
    private Long matchType;

    /**
     * 提前结算(1 开,0 关)
     */
    private Integer matchPreStatus;

    /**
     * 提前结算状态最后更新时间
     */
    private Long matchPreUpdate;

    /**
     * 是否展示15分钟玩法 Y:展示，N：不展示
     */
    private String displayMinute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }
    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }
    public Integer getMatchHandicapStatus() {
        return matchHandicapStatus;
    }

    public void setMatchHandicapStatus(Integer matchHandicapStatus) {
        this.matchHandicapStatus = matchHandicapStatus;
    }
    public Long getThirdMatchId() {
        return thirdMatchId;
    }

    public void setThirdMatchId(Long thirdMatchId) {
        this.thirdMatchId = thirdMatchId;
    }
    public Integer getNeutralGround() {
        return neutralGround;
    }

    public void setNeutralGround(Integer neutralGround) {
        this.neutralGround = neutralGround;
    }
    public Integer getOperateMatchStatus() {
        return operateMatchStatus;
    }

    public void setOperateMatchStatus(Integer operateMatchStatus) {
        this.operateMatchStatus = operateMatchStatus;
    }
    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }
    public Long getMatchManageId() {
        return matchManageId;
    }

    public void setMatchManageId(Long matchManageId) {
        this.matchManageId = matchManageId;
    }
    public String getRiskManagerCode() {
        return riskManagerCode;
    }

    public void setRiskManagerCode(String riskManagerCode) {
        this.riskManagerCode = riskManagerCode;
    }
    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }
    public Long getNameCode() {
        return nameCode;
    }

    public void setNameCode(Long nameCode) {
        this.nameCode = nameCode;
    }
    public Integer getPreMatchBusiness() {
        return preMatchBusiness;
    }

    public void setPreMatchBusiness(Integer preMatchBusiness) {
        this.preMatchBusiness = preMatchBusiness;
    }
    public Integer getLiveOddBusiness() {
        return liveOddBusiness;
    }

    public void setLiveOddBusiness(Integer liveOddBusiness) {
        this.liveOddBusiness = liveOddBusiness;
    }
    public Integer getMatchOver() {
        return matchOver;
    }

    public void setMatchOver(Integer matchOver) {
        this.matchOver = matchOver;
    }
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
    public Long getMatchPeriodId() {
        return matchPeriodId;
    }

    public void setMatchPeriodId(Long matchPeriodId) {
        this.matchPeriodId = matchPeriodId;
    }
    public Integer getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Integer matchStatus) {
        this.matchStatus = matchStatus;
    }
    public Long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }
    public Long getSecondsMatchStart() {
        return secondsMatchStart;
    }

    public void setSecondsMatchStart(Long secondsMatchStart) {
        this.secondsMatchStart = secondsMatchStart;
    }
    public Integer getStoppageStatus() {
        return stoppageStatus;
    }

    public void setStoppageStatus(Integer stoppageStatus) {
        this.stoppageStatus = stoppageStatus;
    }
    public Long getStoppageTime() {
        return stoppageTime;
    }

    public void setStoppageTime(Long stoppageTime) {
        this.stoppageTime = stoppageTime;
    }
    public Long getStoppageEventTime() {
        return stoppageEventTime;
    }

    public void setStoppageEventTime(Long stoppageEventTime) {
        this.stoppageEventTime = stoppageEventTime;
    }
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
    public String getChinaBetting() {
        return chinaBetting;
    }

    public void setChinaBetting(String chinaBetting) {
        this.chinaBetting = chinaBetting;
    }
    public Integer getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(Integer matchFormat) {
        this.matchFormat = matchFormat;
    }
    public Long getPreMatchTime() {
        return preMatchTime;
    }

    public void setPreMatchTime(Long preMatchTime) {
        this.preMatchTime = preMatchTime;
    }
    public Long getLiveOddTime() {
        return liveOddTime;
    }

    public void setLiveOddTime(Long liveOddTime) {
        this.liveOddTime = liveOddTime;
    }
    public String getFristServise() {
        return fristServise;
    }

    public void setFristServise(String fristServise) {
        this.fristServise = fristServise;
    }
    public String getLiveRiskManagerCode() {
        return liveRiskManagerCode;
    }

    public void setLiveRiskManagerCode(String liveRiskManagerCode) {
        this.liveRiskManagerCode = liveRiskManagerCode;
    }
    public String getPreRiskManagerCode() {
        return preRiskManagerCode;
    }

    public void setPreRiskManagerCode(String preRiskManagerCode) {
        this.preRiskManagerCode = preRiskManagerCode;
    }
    public Integer getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(Integer secondNum) {
        this.secondNum = secondNum;
    }
    public Integer getAnimationStatus() {
        return animationStatus;
    }

    public void setAnimationStatus(Integer animationStatus) {
        this.animationStatus = animationStatus;
    }
    public Integer getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(Integer videoStatus) {
        this.videoStatus = videoStatus;
    }
    public Integer getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(Integer firstNum) {
        this.firstNum = firstNum;
    }
    public String getDisplayCorner() {
        return displayCorner;
    }

    public void setDisplayCorner(String displayCorner) {
        this.displayCorner = displayCorner;
    }
    public Long getDisplayMarketCount() {
        return displayMarketCount;
    }

    public void setDisplayMarketCount(Long displayMarketCount) {
        this.displayMarketCount = displayMarketCount;
    }
    public String getDisplayPenalty() {
        return displayPenalty;
    }

    public void setDisplayPenalty(String displayPenalty) {
        this.displayPenalty = displayPenalty;
    }
    public Integer getTournamentLevel() {
        return tournamentLevel;
    }

    public void setTournamentLevel(Integer tournamentLevel) {
        this.tournamentLevel = tournamentLevel;
    }
    public Long getTournamentNameCode() {
        return tournamentNameCode;
    }

    public void setTournamentNameCode(Long tournamentNameCode) {
        this.tournamentNameCode = tournamentNameCode;
    }
    public String getTournamentLogoUrl() {
        return tournamentLogoUrl;
    }

    public void setTournamentLogoUrl(String tournamentLogoUrl) {
        this.tournamentLogoUrl = tournamentLogoUrl;
    }
    public Integer getTournamentIsHot() {
        return tournamentIsHot;
    }

    public void setTournamentIsHot(Integer tournamentIsHot) {
        this.tournamentIsHot = tournamentIsHot;
    }
    public Long getSprotNameCode() {
        return sprotNameCode;
    }

    public void setSprotNameCode(Long sprotNameCode) {
        this.sprotNameCode = sprotNameCode;
    }
    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }
    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }
    public String getAwayLogoUrl() {
        return awayLogoUrl;
    }

    public void setAwayLogoUrl(String awayLogoUrl) {
        this.awayLogoUrl = awayLogoUrl;
    }
    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }
    public Long getHomeNameCode() {
        return homeNameCode;
    }

    public void setHomeNameCode(Long homeNameCode) {
        this.homeNameCode = homeNameCode;
    }
    public Long getAwayNameCode() {
        return awayNameCode;
    }

    public void setAwayNameCode(Long awayNameCode) {
        this.awayNameCode = awayNameCode;
    }
    public String getPreMatchSellStatus() {
        return preMatchSellStatus;
    }

    public void setPreMatchSellStatus(String preMatchSellStatus) {
        this.preMatchSellStatus = preMatchSellStatus;
    }
    public String getLiveMatchSellStatus() {
        return liveMatchSellStatus;
    }

    public void setLiveMatchSellStatus(String liveMatchSellStatus) {
        this.liveMatchSellStatus = liveMatchSellStatus;
    }
    public Integer getMatchLength() {
        return matchLength;
    }

    public void setMatchLength(Integer matchLength) {
        this.matchLength = matchLength;
    }
    public String getMatchTestScore() {
        return matchTestScore;
    }

    public void setMatchTestScore(String matchTestScore) {
        this.matchTestScore = matchTestScore;
    }
    public Integer getIsSuspend() {
        return isSuspend;
    }

    public void setIsSuspend(Integer isSuspend) {
        this.isSuspend = isSuspend;
    }
    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }
    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }
    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }
    public Long getLiveMarketCount() {
        return liveMarketCount;
    }

    public void setLiveMarketCount(Long liveMarketCount) {
        this.liveMarketCount = liveMarketCount;
    }
    public String getAddition1() {
        return addition1;
    }

    public void setAddition1(String addition1) {
        this.addition1 = addition1;
    }
    public String getAddition2() {
        return addition2;
    }

    public void setAddition2(String addition2) {
        this.addition2 = addition2;
    }
    public String getAddition3() {
        return addition3;
    }

    public void setAddition3(String addition3) {
        this.addition3 = addition3;
    }
    public String getAddition4() {
        return addition4;
    }

    public void setAddition4(String addition4) {
        this.addition4 = addition4;
    }
    public String getAddition5() {
        return addition5;
    }

    public void setAddition5(String addition5) {
        this.addition5 = addition5;
    }
    public String getAddition6() {
        return addition6;
    }

    public void setAddition6(String addition6) {
        this.addition6 = addition6;
    }
    public String getAddition7() {
        return addition7;
    }

    public void setAddition7(String addition7) {
        this.addition7 = addition7;
    }
    public String getAddition8() {
        return addition8;
    }

    public void setAddition8(String addition8) {
        this.addition8 = addition8;
    }
    public String getAddition9() {
        return addition9;
    }

    public void setAddition9(String addition9) {
        this.addition9 = addition9;
    }
    public String getAddition10() {
        return addition10;
    }

    public void setAddition10(String addition10) {
        this.addition10 = addition10;
    }
    public Integer getHotStatus() {
        return hotStatus;
    }

    public void setHotStatus(Integer hotStatus) {
        this.hotStatus = hotStatus;
    }
    public Integer getOperationHotSortTop() {
        return operationHotSortTop;
    }

    public void setOperationHotSortTop(Integer operationHotSortTop) {
        this.operationHotSortTop = operationHotSortTop;
    }
    public String getOperationHotSort() {
        return operationHotSort;
    }

    public void setOperationHotSort(String operationHotSort) {
        this.operationHotSort = operationHotSort;
    }
    public String getOperationHotDesc() {
        return operationHotDesc;
    }

    public void setOperationHotDesc(String operationHotDesc) {
        this.operationHotDesc = operationHotDesc;
    }
    public String getPreTraderId() {
        return preTraderId;
    }

    public void setPreTraderId(String preTraderId) {
        this.preTraderId = preTraderId;
    }
    public String getPreTrader() {
        return preTrader;
    }

    public void setPreTrader(String preTrader) {
        this.preTrader = preTrader;
    }
    public String getLiveTraderId() {
        return liveTraderId;
    }

    public void setLiveTraderId(String liveTraderId) {
        this.liveTraderId = liveTraderId;
    }
    public String getLiveTrader() {
        return liveTrader;
    }

    public void setLiveTrader(String liveTrader) {
        this.liveTrader = liveTrader;
    }
    public String getHomeFormation() {
        return homeFormation;
    }

    public void setHomeFormation(String homeFormation) {
        this.homeFormation = homeFormation;
    }
    public String getAwayFormation() {
        return awayFormation;
    }

    public void setAwayFormation(String awayFormation) {
        this.awayFormation = awayFormation;
    }
    public Long getLeagueAsNameCode() {
        return leagueAsNameCode;
    }

    public void setLeagueAsNameCode(Long leagueAsNameCode) {
        this.leagueAsNameCode = leagueAsNameCode;
    }
    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }
    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }
    public Integer getOperationNotopSort() {
        return operationNotopSort;
    }

    public void setOperationNotopSort(Integer operationNotopSort) {
        this.operationNotopSort = operationNotopSort;
    }
    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }
    public Integer getManualWeight() {
        return manualWeight;
    }

    public void setManualWeight(Integer manualWeight) {
        this.manualWeight = manualWeight;
    }
    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }
    public Integer getDisplayDisableBooking() {
        return displayDisableBooking;
    }

    public void setDisplayDisableBooking(Integer displayDisableBooking) {
        this.displayDisableBooking = displayDisableBooking;
    }
    public Integer getDisplayPromotion() {
        return displayPromotion;
    }

    public void setDisplayPromotion(Integer displayPromotion) {
        this.displayPromotion = displayPromotion;
    }
    public Integer getDisplayOverTime() {
        return displayOverTime;
    }

    public void setDisplayOverTime(Integer displayOverTime) {
        this.displayOverTime = displayOverTime;
    }
    public Integer getDisplayPenaltyShootout() {
        return displayPenaltyShootout;
    }

    public void setDisplayPenaltyShootout(Integer displayPenaltyShootout) {
        this.displayPenaltyShootout = displayPenaltyShootout;
    }
    public Long getMatchType() {
        return matchType;
    }

    public void setMatchType(Long matchType) {
        this.matchType = matchType;
    }
    public Integer getMatchPreStatus() {
        return matchPreStatus;
    }

    public void setMatchPreStatus(Integer matchPreStatus) {
        this.matchPreStatus = matchPreStatus;
    }
    public Long getMatchPreUpdate() {
        return matchPreUpdate;
    }

    public void setMatchPreUpdate(Long matchPreUpdate) {
        this.matchPreUpdate = matchPreUpdate;
    }
    public String getDisplayMinute() {
        return displayMinute;
    }

    public void setDisplayMinute(String displayMinute) {
        this.displayMinute = displayMinute;
    }

    @Override
    public String toString() {
        return "MatchInfoCur{" +
            "id=" + id +
            ", sportId=" + sportId +
            ", tournamentId=" + tournamentId +
            ", matchHandicapStatus=" + matchHandicapStatus +
            ", thirdMatchId=" + thirdMatchId +
            ", neutralGround=" + neutralGround +
            ", operateMatchStatus=" + operateMatchStatus +
            ", beginTime=" + beginTime +
            ", matchManageId=" + matchManageId +
            ", riskManagerCode=" + riskManagerCode +
            ", dataSourceCode=" + dataSourceCode +
            ", remark=" + remark +
            ", createTime=" + createTime +
            ", modifyTime=" + modifyTime +
            ", nameCode=" + nameCode +
            ", preMatchBusiness=" + preMatchBusiness +
            ", liveOddBusiness=" + liveOddBusiness +
            ", matchOver=" + matchOver +
            ", score=" + score +
            ", matchPeriodId=" + matchPeriodId +
            ", matchStatus=" + matchStatus +
            ", remainingTime=" + remainingTime +
            ", secondsMatchStart=" + secondsMatchStart +
            ", stoppageStatus=" + stoppageStatus +
            ", stoppageTime=" + stoppageTime +
            ", stoppageEventTime=" + stoppageEventTime +
            ", updateTime=" + updateTime +
            ", chinaBetting=" + chinaBetting +
            ", matchFormat=" + matchFormat +
            ", preMatchTime=" + preMatchTime +
            ", liveOddTime=" + liveOddTime +
            ", fristServise=" + fristServise +
            ", liveRiskManagerCode=" + liveRiskManagerCode +
            ", preRiskManagerCode=" + preRiskManagerCode +
            ", secondNum=" + secondNum +
            ", animationStatus=" + animationStatus +
            ", videoStatus=" + videoStatus +
            ", firstNum=" + firstNum +
            ", displayCorner=" + displayCorner +
            ", displayMarketCount=" + displayMarketCount +
            ", displayPenalty=" + displayPenalty +
            ", tournamentLevel=" + tournamentLevel +
            ", tournamentNameCode=" + tournamentNameCode +
            ", tournamentLogoUrl=" + tournamentLogoUrl +
            ", tournamentIsHot=" + tournamentIsHot +
            ", sprotNameCode=" + sprotNameCode +
            ", homeLogoUrl=" + homeLogoUrl +
            ", homeTeamId=" + homeTeamId +
            ", awayLogoUrl=" + awayLogoUrl +
            ", awayTeamId=" + awayTeamId +
            ", homeNameCode=" + homeNameCode +
            ", awayNameCode=" + awayNameCode +
            ", preMatchSellStatus=" + preMatchSellStatus +
            ", liveMatchSellStatus=" + liveMatchSellStatus +
            ", matchLength=" + matchLength +
            ", matchTestScore=" + matchTestScore +
            ", isSuspend=" + isSuspend +
            ", eventCode=" + eventCode +
            ", eventTime=" + eventTime +
            ", seasonId=" + seasonId +
            ", liveMarketCount=" + liveMarketCount +
            ", addition1=" + addition1 +
            ", addition2=" + addition2 +
            ", addition3=" + addition3 +
            ", addition4=" + addition4 +
            ", addition5=" + addition5 +
            ", addition6=" + addition6 +
            ", addition7=" + addition7 +
            ", addition8=" + addition8 +
            ", addition9=" + addition9 +
            ", addition10=" + addition10 +
            ", hotStatus=" + hotStatus +
            ", operationHotSortTop=" + operationHotSortTop +
            ", operationHotSort=" + operationHotSort +
            ", operationHotDesc=" + operationHotDesc +
            ", preTraderId=" + preTraderId +
            ", preTrader=" + preTrader +
            ", liveTraderId=" + liveTraderId +
            ", liveTrader=" + liveTrader +
            ", homeFormation=" + homeFormation +
            ", awayFormation=" + awayFormation +
            ", leagueAsNameCode=" + leagueAsNameCode +
            ", auditorId=" + auditorId +
            ", auditor=" + auditor +
            ", operationNotopSort=" + operationNotopSort +
            ", betAmount=" + betAmount +
            ", manualWeight=" + manualWeight +
            ", closeTime=" + closeTime +
            ", displayDisableBooking=" + displayDisableBooking +
            ", displayPromotion=" + displayPromotion +
            ", displayOverTime=" + displayOverTime +
            ", displayPenaltyShootout=" + displayPenaltyShootout +
            ", matchType=" + matchType +
            ", matchPreStatus=" + matchPreStatus +
            ", matchPreUpdate=" + matchPreUpdate +
            ", displayMinute=" + displayMinute +
        "}";
    }
}
