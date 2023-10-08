package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * t_merchant_config
 *
 * @author duwan 2021-02-03
 */
@Data
@TableName("t_merchant_config")
public class MerchantConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 0 白色 1黑色
     */
    private Integer profesTag;

    /**
     * 0 白色 1黑色
     */
    private Integer nociceTag;

    /**
     * 0 白色 1黑色
     */
    private Integer standardTag;

    /**
     * 标题
     */
    private String title;

    /**
     * 深色log地址
     */
    private String darkLogoUrl;

    /**
     * 白色log地址
     */
    private String whiteLogoUrl;

    /**
     * pc浏览器图标
     */
    private String pcLogoUrl;

    /**
     * 兼容性log
     */
    private String compatLogoUrl;

    /**
     * 视频log
     */
    private String videoLogoUrl;

    /**
     * log 启用禁用0禁用 1启用
     */
    private Integer logoEnable;

    /**
     * 内嵌版最大宽度 1200-1480px
     */
    private Long inlineWidth;

    /**
     * pc视频播放背景
     */
    private String pcVideoBackground;

    /**
     * h5默认板式 0白色 1深色
     */
    private Integer h5Default;

    /**
     * app 默认版式 0白色 1深色
     */
    private Integer appDefault;

    /**
     * 默认盘口赔率 1欧洲盘 2香港盘
     */
    private Integer marketDefault;

    private Integer userPrefix;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * pc列表页图片
     */
    private String loadLogoUrl;

    /**
     * 无联赛logo
     */
    private String leagueLogoUrl;

    /**
     * 新版h5默认banner图片
     */
    private String bannerUrl;

    /**
     * 提前结算开关  0为关，1为开，默认为关0
     */
    private Integer settleSwitchAdvance;

    /**
     * 商户默认语种
     **/
    private String defaultLanguage;

    /**
     * 商户行情开关
     **/
    private Integer tagMarketStatus;

    private Integer tagMarketLevel;

    private Integer marketLevelIdPc;

    private Integer merchantTag;

    /**
     * 过滤赛种
     */
    private String filterSport;

    /**
     * 过滤联赛
     */
    private String filterLeague;

    private String videoDomain;

    @ApiModelProperty(value = "最小串关数,默认值为2")
    private Integer minSeriesNum;

    @ApiModelProperty(value = "最大串关数,默认值为10")
    private Integer maxSeriesNum;

    @ApiModelProperty(value = "赔率分组动态风控开关 0关 1开")
    private Integer tagOddsGroupingDynamicRiskControlStatus;

    /**
     * 是否对接APP 0否1是
     */
    private Integer isApp;

    /**
     *  预约投注开关  0:默认 关  1: 开
     */
    private Integer bookBet;

    /**
     * 足球预约盘口开关 0关 1开
     */
    private String bookMarketSwitch;

    /**
     * 蓝球预约盘口开关 0关 1开
     */
    private Integer bookMarketSwitchBasketball;

    /**
     * 是否风险商户新用户 0否1是
     */
    private Integer isRisk;

    /**
     * 视频流量管控开关 0关 1开
     */
    private Integer videoSwitch;

    /**
     * 用户名前缀位数
     */
    private Integer userProfixDigits;

    /**
     * 前三天累计投注金额
     */
    private Double threeDayAmount;

    /**
     * 前七天累计投注金额
     */
    private Double sevenDayAmount;

    /**
     * 商户默认视频域名配置
     */
    private String defaultVideoDomain;

    /**
     * 商户编码集合
     */
    private List<String> merchantCodeList;

    /**
     * 盘口开关
     */
    private String coilSwitch;

    /**
     * 篮球提前结算开关  0为关，1为开，默认为关0
     */
    private Integer settleSwitchBasket;
}
