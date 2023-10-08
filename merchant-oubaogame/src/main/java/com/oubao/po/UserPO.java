package com.oubao.po;

import com.oubao.vo.BaseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户表(t_user)
 *
 * @author Gardening
 * @since 2019-08-31 19:14
 */
@Data
public class UserPO extends BaseVO {

    /**
     * 版本号
     */
    private static final long serialVersionUID = -5591067906256831859L;

    /**
     * 用户已冻结
     */
    public static final int DISABLED_USER = 1;

    /**
     * 表ID，自增
     */
    private Long id;
    private Integer duration;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户状态 0启用 1禁用
     */
    private Integer disabled;
    private String terminal;

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

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 币种(注册时必填)
     */
    private String currencyCode;

    /**
     * 邮箱
     */
    private String email;

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
    private BigDecimal amount;

    /**
     * 用户等级
     */
    private Integer userLevel;
    private Integer transferMode;
    private Integer userBetPrefer;


    /**
     * 用户等级
     */
    private String ipAddress;
    /**
     * 前端唯一标识UUID
     */
    private String uuid;
    /**
     * 页码
     */
    Integer page;

    /**
     * 页面大小
     */
    Integer size;

    /**
     * 开始
     */
    private Integer start;

    /**
     * 结束
     */
    private Integer end;
    private String merchantCode;

    private Long lastLogin;

    private String lastBet;



    private Double balance;

    private String callBackUrl;

    private String sportId;

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

    private String fakeName;


    private Integer vipLevel;


    private String userMarketPrefer;

    private Integer settleInAdvance;

    private Integer specialBettingLimitType;
    private Long specialBettingLimitTime;
    private Integer specialBettingLimitDelayTime;
    private String specialBettingLimitRemark;

    private String languageName;


    private String agentId;

    private Integer marketLevel;

    private Long maxBet;

    private String filterSport;

    private String filterLeague;

    private String domainGroupCode;

    /**
     * VR体育开关 0关闭 1打开
     */
    private Integer openVrSport;

    private Integer openEsport;

    private Integer openVideo;

    /**
     * 提前结算开关，默认为0，0为关，1为开   篮球
     */
    private Integer settleSwitchBasket;



}