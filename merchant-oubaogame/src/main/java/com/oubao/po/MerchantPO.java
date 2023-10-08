package com.oubao.po;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Data
@ToString
public class MerchantPO implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 商户id
     */
    private String id;

    /**
     * 商户code
     */
    private String merchantCode;
    private List<String> merchantCodes;

    private String parentCode;

    /**
     * 商户名称
     */
    private String merchantName;
    private String parentName;

    /**
     * 商户级别
     */
    private Integer level;

    private Integer serialNumber;

    /**
     * 商务
     */
    private String commerce;

    /**
     * 商户等级
     */
    private String levelName;

    /**
     * 上级商户
     */
    private String parentId;

    private Integer childAmount;

    private Integer directSale;

    private Integer childConnectMode;

    private Integer childMaxAmount;

    /**
     * 游戏列表
     */
    private String sportList;

    /**
     * 商户下最大投注额封锁值
     */
    private Long maxBet;

    /**
     * 加扣款接口
     */
    private String url;

    /**
     * 转账类型(1:免转2:转账)
     */
    private Integer transferMode;

    /**
     * 商户状态（0-无效，1-有效）
     */
    private Integer status;

    /**
     * 商户验签和加解密的key
     */
    private String merchantKey;
    private String historyKey;
    private String whiteIp;

    /**
     * 加扣款回调url
     */
    private String callbackUrl;
    private String balanceUrl;

    /**
     * 对应商户topic
     */
    private String topic;

    /**
     * 代理级别
     */
    private Integer agentLevel;


    private String filePath;
    private String fileName;
    private String country;
    private String province;


    /**
     * email
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 币种:默认1,人民币，2为积分制
     */
    private Integer currency;

    /**
     * 代理级别
     */
    private Double rate;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 手机
     */
    private String phone;

    /**
     * 商户管理员
     */
    private String merchantAdmin;

    /**
     * 商户管理员密码
     */
    private String adminPassword;

    /**
     * 商户logo
     */
    private String logo;

    private String startTime;

    private String endTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 计算模式
     */
    private String computingStandard;

    /**
     * 金额范围起始
     */
    private Long rangeAmountBegin;

    /**
     * 金额范围结束
     */
    private Long rangeAmountEnd;

    /**
     * 平台费率单位%
     */
    private Double terraceRate;

    private Integer userPrefix;

    /**
     * 缴纳周期
     */
    private Integer paymentCycle;

    /**
     * vip费用
     */
    private Long vipAmount;

    /**
     * vip缴纳周期
     */
    private Integer vipPaymentCycle;

    /**
     * 技术费用
     */
    private Long techniqueAmount;

    /**
     * 技术费用缴纳周期
     */
    private Integer techniquePaymentCycle;

    /**
     * 状态
     */
    private String statusDescription;


    private Integer transferType;


    private String password;


    private String userId;


    private String parentTopic;

    private String userName;

    private String pcDomain;

    private String h5Domain;

    private String appDomain;
    private String videoDomain;

    /**
     * VR体育开关
     */
    private Integer openVrSport;

    private Integer openEsport;

    private Integer openVideo;

    /**
     * 提前结算开关，默认为0，0为关，1为开
     */
    private Integer settleSwitchAdvance;


    /**
     * 商户可选语种设置，多个以逗号分隔
     */
    private String languageList;
    /**
     * 商户默认语种
     **/
    private String defaultLanguage;

    /**
     * 商户行情
     **/
    private Integer tagMarketLevel;
    private Integer tagMarketLevelIdPc;

    /**
     * 行情等级最后更新时间
     */
    private Long marketLevelUpdate;
    /**
     * cashout最后更新时间
     */
    private Long cashOutUpdate;

    private Integer merchantTag;

    private Long merchantGroupId;

    /**
     * 域名组code,关联t_merchant_domain_group.group_code
     */
    private String domainGroupCode;

    /**
     * 商户编码别名
     */
    private String kanaCode;

    /**
     * 商户编码别名更新时间
     */
    private Date kanaCodeTime;

    private String filterSport;

    private String filterLeague;
    private String parentKanaCode;

    /**
     * 是否测试商户0否1是
     */
    private Integer isTest;

    /**
     * 是否外部商户0外部1内部
     */
    private Integer isExternal;

    /**
     * 提前结算开关，默认为0，0为关，1为开   篮球
     */
    private Integer settleSwitchBasket;


}