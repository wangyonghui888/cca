package com.panda.sport.merchant.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.po.bss.UserLevelRelationVO;
import com.panda.sport.merchant.common.vo.MerchantChatRoomSwitchVO;
import com.panda.sport.merchant.common.vo.MerchantEventSwitchVO;
import com.panda.sport.merchant.common.vo.MerchantVideoManageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 禁止在此类添加对象!!!!!
 *  -20230602
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserInfo implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = -5591067906256831859L;
    /**
     * 用户已冻结
     */
    public static final int DISABLED_USER = 1;

    private Integer duration;
    /**
     * 表ID，自增
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;


    private Integer financeTag;


    /**
     * 商户名
     */
    private String merchantCode;
    /**
     * 商户名
     */
    private Integer transferMode;
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    private String fakeName;

    private String jumpsupport;

    private String jumpfrom;
    /**
     * 币种(注册时必填)
     */
    private String currencyCode;
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 修改用户
     */
    private String modifyUser;
    /**
     * 修改时间
     */
    private Long modifyTime;

    private String terminal;

    /**
     * 用户等级
     */
    private Integer userLevel;

    private Integer userBetPrefer;

    private String userMarketPrefer;
    /**
     * 用户等级
     */
    private String ipAddress;

    private Double balance;

    private String callBackUrl;

    private String languageName;

    private String sportId;

    private Integer specialBettingLimitType;

    private String agentId;

    private Integer marketLevel;

    private Integer userMarketLevel;

    private Integer vipLevel;

    private List<UserLevelRelationVO> userlevelList;

    /**
     * VR体育开关 0关闭 1打开
     */
    private Integer openVrSport;

    private Integer openEsport;

    /**
     * 对内视频开关
     */
    private Integer openVideo;

    private String videoDomain;

    private Integer settleInAdvance;

    private Long maxBet;

    private Long lastLogin;

    private String lastBet;

    private Long specialBettingLimitTime;
    private Integer specialBettingLimitDelayTime;
    private String specialBettingLimitRemark;

    private String ip;
    /**
     * 投注笔数
     */
    private Integer betNum;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    private BigDecimal validBetAmount;
    /**
     * 盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal profit;

    private String filterSport;

    private String filterLeague;

    private String domainGroupCode;

    @ApiModelProperty(value = "最小串关数,默认值为2")
    private Integer minSeriesNum;

    @ApiModelProperty(value = "最大串关数,默认值为10")
    private Integer maxSeriesNum;

    private Integer dynamicMargin;

    private Integer bookBet;

    /**
     * 足球预约盘口开关 0关 1开
     */
    private Integer bookMarketSwitch;

    /**
     * 蓝球预约盘口开关 0关 1开
     */
    private Integer bookMarketSwitchBasketball;

    private String loginUrl;

    private List<String> loginUrlArr;

    private String imgDomain;

    private String livePcUrl;

    private String liveH5Url;

    private String apiDomain;

    private String chatroomUrl;

    private String chatroomHttpUrl;

    /**是否vip用户 1否2是*/
    private Integer isTest;

    /**
     * 是否测试商户(0否1是)
     */
    private Integer isTestMerchant;

//    private MerchantVideoManageVo videoManageVo;

    /**
     * 长时间未操作暂停视频(0关,1开)
     */
    private Integer closedWithoutOperation;

    /**
     * 视频设置(0默认,1自定义)
     */
    private Integer videoSettings;

    /**
     * 默认视频观看时长设置(15分钟)
     */
    private Integer viewingTime;
    /**
     * 是否测试商户(0否1是)
     */
    private MerchantVideoManageVo videoManageVo;

    /**
     * 自定义视频观看时长设置(5~120分钟)
     */
    private Integer customViewTime;

    /**
     * 不可背景播放(0关,1开)
     */
    private Integer noBackgroundPlay;

//    private MerchantChatRoomSwitchVO chatRoomSwitchVO;

    private MerchantEventSwitchVO merchantEventSwitchVO;

    /**
     * 提前结算开关，默认为0，0为关，1为开   篮球
     */
    private Integer settleSwitchBasket;
}