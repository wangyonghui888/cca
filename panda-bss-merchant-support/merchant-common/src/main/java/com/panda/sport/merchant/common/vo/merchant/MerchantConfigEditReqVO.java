package com.panda.sport.merchant.common.vo.merchant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author :  ives
 * @Description :  获取查询条件设置响应VO
 * @Date: 2022-01-24 18:20
 */
@Data
@Api(value = "获取查询条件设置响应VO")
public class MerchantConfigEditReqVO implements Serializable {

    private static final long serialVersionUID = -8949487947500891323L;

    @ApiModelProperty(value = "逻辑ID")
    private Long id;

    @ApiModelProperty(value = "商户编码")
    @NotBlank(message = "商户编码不允许为空！")
    private String merchantCode;

    @ApiModelProperty(value = "专业版 1 白色 2黑色")
    private Integer profesTag;

    @ApiModelProperty(value = "新手版 1 白色 2黑色")
    private Integer nociceTag;

    @ApiModelProperty(value = "标准版 1 白色 2黑色")
    private Integer standardTag;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "深色log地址")
    private String darkLogoUrl;

    @ApiModelProperty(value = "白色log地址")
    private String whiteLogoUrl;

    @ApiModelProperty(value = "pc浏览器图标")
    private String pcLogoUrl;

    @ApiModelProperty(value = "兼容性log")
    private String compatLogoUrl;

    @ApiModelProperty(value = "视频log")  // PC视频异常
    private String videoLogoUrl;

    @ApiModelProperty(value = "logo控制 0 禁用 1启用")
    private Integer logoEnable;

    @ApiModelProperty(value = "内嵌版最大宽度 1200-1480px")
    @Range(min = 1200, max = 1480, message = "内嵌最大宽度范围1200-1480！")
    private Long inlineWidth;

    @ApiModelProperty(value = "pc视频播放背景")
    private String pcVideoBackground;

    @ApiModelProperty(value = "h5默认板式 1白色 2深色")
    private Integer h5Default;

    @ApiModelProperty(value = "app 默认版式 1白色 2深色")
    private Integer appDefault;

    @ApiModelProperty(value = "默认盘口赔率 1 欧洲盘 2香港盘")
    private Integer marketDefault;

    @ApiModelProperty(value = "用户名前缀")
    private Integer userPrefix;

    @ApiModelProperty(value = "pc列表页图片")
    private String loadLogoUrl;


    @ApiModelProperty(value = "无联赛logo")
    private String leagueLogoUrl;

    @ApiModelProperty(value = "新版h5默认banner图片")
    private String bannerUrl;

    @ApiModelProperty(value = "提前结算开关,0关闭，1为开")
    private Integer settleSwitchAdvance;

    @ApiModelProperty(value = "商户默认语种")
    private String defaultLanguage;

    @ApiModelProperty(value = "商户行情开关,0关闭，1为开")
    private Integer tagMarketStatus;

    @ApiModelProperty(value = "行情等级")
    private Integer tagMarketLevel;

    @ApiModelProperty(value = "0 现金网，1 信用网")
    private Integer merchantTag;

    @ApiModelProperty(value = "过滤赛种")
    private String filterSport;

    @ApiModelProperty(value = "过滤联赛")
    private String filterLeague;

    @ApiModelProperty(value = "视频API域名")
    private String videoDomain;

    @ApiModelProperty(value = "最小串关数,默认值为2")
    @Range(min = 2, message = "最小串关数最小值为2！")
    private Integer minSeriesNum;

    @ApiModelProperty(value = "最大串关数,默认值为10")
    @Range(max = 10, message = "最大串关数最大值为10！")
    private Integer maxSeriesNum;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "日志标识")
    private Integer logTag;

    @ApiModelProperty(value = "赔率分组动态风控开关 0关 1开")
    private Integer tagOddsGroupingDynamicRiskControlStatus;

    @ApiModelProperty(value = "预约投注 0关 1开")
    private Integer bookBet;

    @ApiModelProperty(value = "足球预约盘口开关 0关 1开")
    private Integer bookMarketSwitch;

    @ApiModelProperty(value = "蓝球预约盘口开关 0关 1开")
    private Integer bookMarketSwitchBasketball;

    @ApiModelProperty(value = "视频流量管控开关 0关 1开")
    private Integer videoSwitch;

    @ApiModelProperty(value = "用户名前缀位数")
    private Integer userProfixDigits;

    @ApiModelProperty(value = "商户等级 0 直营 1 渠道 2 二级")
    private String agentLevel;

    /**
     * 盘口开关
     */
    private String coilSwitch;
}
