package com.panda.sport.merchant.common.po.bss;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.vo.MerchantChatRoomSwitchVO;
import com.panda.sport.merchant.common.vo.MerchantEventSwitchVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户表(t_user)
 *
 * @author Gardening
 * @since 2019-08-31 19:14
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WindUserPO implements Serializable {
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

    /**
     * 用户状态 0启用 1禁用
     */
    private Integer disabled;

    /**
     * 用户名
     */
    private String username;
    /*
     * 币种(注册时必填)
     */
    private String currencyCode;

    private String jumpsupport;

    private String jumpfrom;

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

    private String terminal;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 用户等级
     */
    private Integer userLevel;

    private Integer financeTag;

    /**
     * 用户标签
     */
    private List<UserLevelRelationPO> userlevelList;

    private String merchantCode;

    private String callBackUrl;

    private String languageName;

    private Integer transferMode;
    /**
     * VR体育开关 0关闭 1打开
     */
    private Integer openVrSport;
    /**
     * DJ开关 0关闭 1打开
     */
    private Integer openEsport;

    private Integer openVideo;
    private String videoDomain;
    /**
     * 提前结算开关，整合用户和商户一起 0：关；1：开
     */
    private Integer settleInAdvance;

    /**
     * 用户投注爱好
     */
    private Integer userBetPrefer;

    /**
     * 用户最大投注金额
     */
    private Double maxBet;
    /**
     * 风控额度百分比(乘以100以后的值)
     */
    private Integer riskBetPercent;
    /**
     * VIP等级： 0 非VIP，1 VIP用户
     */
    private Integer vipLevel;
    /**
     * 投注盘口爱好（eu:欧盘,hk:香港盘,us:美式盘,id:印尼盘,my:马来盘,gb:英式盘）
     */
    private String userMarketPrefer;

    /**
     * 1无  2特殊百分比限额 3特殊单注单场限额 4特殊vip限额.
     */
    private Integer specialBettingLimitType;

    private String agentId;

    private Integer marketLevel;


    private Integer userMarketLevel;
    /**

    /**
     * 真实姓名
     */
    private String realName;

    private String fakeName;

    private String ipAddress;

    private Double balance;

    private Long specialBettingLimitTime;
    private Integer specialBettingLimitDelayTime;
    private String specialBettingLimitRemark;

    private String filterSport;

    private String filterLeague;

    /**
     * 最小串关数,默认值为2
     */
    private Integer minSeriesNum;

    /**
     * 最大串关数,默认值为10
     */
    private Integer maxSeriesNum;

    private String domainGroupCode;

    private Integer dynamicMargin;

    private Integer bookBet;

    private String loginUrl;

    private List<String> loginUrlArr;

    private String imgDomain;

    private String livePcUrl;

    private String apiDomain;

    private String liveH5Url;

    private String chatroomUrl;

    private String chatroomHttpUrl;

    private MerchantChatRoomSwitchVO chatRoomSwitchVO;

    private MerchantEventSwitchVO merchantEventSwitchVO;

    /**是否vip用户 1否2是*/
    private Integer isTest;
}