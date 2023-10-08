package com.panda.sport.merchant.common.po.bss;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPO implements Serializable {

    /**
     * 版本号
     */
    private static final long serialVersionUID = -5591067906256831859L;

    /**
     * 用户已冻结
     */
    public static final int DISABLED_USER = 1;


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

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    private String fakeName;

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

    private Long lastLogin;

    private String lastBet;

    private Integer vipLevel;

    /**
     * 用户等级
     */
    private Integer userLevel;

    private Integer userBetPrefer;

    private String userMarketPrefer;

    private Integer settleInAdvance;

    /**
     * 用户等级
     */
    private String ipAddress;

    private String merchantCode;


    private Double balance;

    private String callBackUrl;


    private String sportId;

    private Integer specialBettingLimitType;
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

    private String languageName;
    /**
     * 盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)
     */
    private BigDecimal profit;

    private String agentId;

    private Integer marketLevel;

    private Long maxBet;

    /**是否vip用户 1否2是*/
    private Integer isTest;

    /**
     * 提前结算开关，默认为0，0为关，1为开   篮球
     */
    private Integer settleSwitchBasket;
}