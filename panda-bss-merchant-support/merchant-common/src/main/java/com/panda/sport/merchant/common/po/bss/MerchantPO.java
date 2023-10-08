package com.panda.sport.merchant.common.po.bss;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
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
     * 备注
     */
    private String remark;

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
     * 第二密钥
     */
    private String secondMerchantKey;

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
     * 代理级别(0:直营站点,1:渠道站点,2:渠道站点,10:代理商)
     */
    private Integer agentLevel;
    private List<Integer> agentLevelList;

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
     * 商户多币种
     */
    private String currencyCode;

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
     * 商户管理员密码-明码
     */
    private String adminpswCode;

    /**
     * 密码-明码
     */
    private String pswCode;

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
    private List<MerchantDomainPO> pcDomainList;

    private String h5Domain;
    private List<MerchantDomainPO> h5DomainList;

    private String appDomain;
    private List<MerchantDomainPO> appDomainList;

    private List<MerchantDomainPO> picDomainList;

    private List<MerchantDomainPO> othDomainList;

    private String videoDomain;

    /**
     * VR体育开关
     */
    private Integer openVrSport;

    private Integer openEsport;

    /**
     * 视频开关(0关 1开)
     */
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

    private Integer dynamicMarketLevelStatus;

    /**
     * 行情等级最后更新时间
     */
    private Long marketLevelUpdate;
    /**
     * cashout最后更新时间
     */
    private Long cashOutUpdate;

    private Integer merchantTag;

    /**
     * 域名防护商户组
     */
    private Long merchantGroupId;

    /**
     * 商户分库组组code,关联t_merchant_domain_group.group_code
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
     * 是否对接APP 0否1是
     */
    private Integer isApp;

    /**
     * 预约投注开关  默认0：关  1：开
     */
    private Integer bookBet;

    /**
     * 足球预约盘口开关 0关 1开
     */
    private Integer bookMarketSwitch;

    /**
     * 蓝球预约盘口开关 0关 1开
     */
    private Integer bookMarketSwitchBasketball;

    /**
     * 盘口开关
     */
    private String coilSwitch;

    /**
     * 视频流量管控开关(0关 1开)
     */
    private Integer videoSwitch;

    /**
     * 聊天室开关(0关,1开)
     */
    private Integer chatRoomSwitch;

    /**
     * 发言最低累计投注额(0关,1开)
     */
    private Integer chatMinBetAmount;

    /**
     * 是否默认设置(0默认,1自定义)
     */
    private Integer isDefault;

    /**
     * 前三天累计投注金额
     */
    private BigDecimal threeDayAmount;

    /**
     * 前七天累计投注金额
     */
    private BigDecimal sevenDayAmount;

    /**
     * 是否风险商户新用户 0否1是
     */
    private Integer isRisk;

    /**
     * 商户后台开关1开0关
     */
    private Integer backendSwitch;

    /**
     * 子商户列表
     */
    private List<MerchantPO> childList;

    @ApiModelProperty(value = "最小串关数,默认值为2")
    private Integer minSeriesNum;

    @ApiModelProperty(value = "最大串关数,默认值为10")
    private Integer maxSeriesNum;

    /**
     * 是否开启对帐：默认0关闭
     */
    private Integer isOpenBill;

    /**
     * 商户默认视频域名配置
     */
    private String defaultVideoDomain;

    /**
     * 精彩回放配置
     */
    private String merchantEvent;

    /**
     * 精彩回放开关
     */
    private Integer eventSwitch;

    /**
     * 篮球提前结算开关，默认为0，0为关，1为开
     */
    private Integer settleSwitchBasket;

    /**
     * 商户证书数量
     */
    private Integer merchantKeyCount;

    /**
     * 原始密钥状态(0-禁用,1-启用)
     */
    private Integer keyStatus;

    /**
     * 第二密钥状态(0-禁用,1-启用)
     */
    private Integer secondStatus;

    public String getStatusDescription() {
        Date now = new Date();
        Date end = null;
        if (endTime == null) {
            return "未上传";
        }
        try {
            end = DateUtils.parseDate(endTime, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date before = DateUtils.addDays(end, -30);
        if (status != null) {
            if (status == 0) {
                return "已过期";
            } else if (status == 1 && before.before(now)) {
                return "即将过期";
            } else {
                return "有效";
            }
        } else {
            return "";
        }
    }
}