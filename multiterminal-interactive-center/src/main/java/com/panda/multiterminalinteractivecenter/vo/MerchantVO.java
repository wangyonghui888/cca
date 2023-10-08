package com.panda.multiterminalinteractivecenter.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.multiterminalinteractivecenter.po.MerchantDomainPO;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author butr 2020-01-21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class MerchantVO implements Serializable {

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
    /**
     * 商户等级
     */
    private Integer level;

    /**
     * 商户等级
     */
    private String levelName;
    /**
     * 上级商户
     */
    private String parentId;

    private String parentName;

    private MerchantPO parent;

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
     * 商务
     */
    private String commerce;


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

    /**
     * email
     */
    private String email;

    private String filePath;
    private String fileName;
    private String country;
    private String province;

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

    private String pcDomain;
    private List<MerchantDomainPO> pcDomainList;

    private String h5Domain;
    private List<MerchantDomainPO> h5DomainList;

    private String videoDomain;

    private String appDomain;
    private List<MerchantDomainPO> appDomainList;

    private List<MerchantDomainPO> picDomainList;

    private List<MerchantDomainPO> othDomainList;

    private Integer pageSize;
    private Integer pageIndex;
    private String sort;
    private String orderBy;

    private Integer pageMode;

    /**
     * VR体育开关
     */
    private Integer openVrSport;

    private Integer openEsport;

    private Integer openVideo;
    
    private Integer serialNumber;

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
     * 商户行情开关
     **/
    private Integer tagMarketStatus;

    private Integer userPrefix;

    private Integer merchantTag;

    /**
     * 商户编码别名
     */
    private String kanaCode;
    private String parentKanaCode;

    /**
     * 商户编码别名更新时间
     */
    private Date kanaCodeTime;


    private Integer tagMarketLevel;

    /**
     * 域名防护商户组
     */
    private Long merchantGroupId;

    /**
     * 商户分库组组code,关联t_merchant_domain_group.group_code
     */
    private String domainGroupCode;

    /**
     * 是否测试商户0否1是
     */
    private Integer isTest;

    /**
     * 是否外部商户0外部1内部
     */
    private Integer isExternal;

    /**
     * 是否对接APP 0否1是
     */
    private Integer isApp;

    private String language;

    /**
     * 预约投注开关  默认0：关  1：开
     */
    private Integer bookBet;

    /**
     * 入驻时间
     */
    private Long creatTime;

    private Date createdAt;

    private String tab;
}
