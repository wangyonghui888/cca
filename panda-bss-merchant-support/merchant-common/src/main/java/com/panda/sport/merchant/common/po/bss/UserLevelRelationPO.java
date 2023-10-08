package com.panda.sport.merchant.common.po.bss;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
public class UserLevelRelationPO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * uid
     */
    private Long uid;

    /**
     * level_id
     */
    private Integer levelId;

    /**
     * levelName
     */
    private String levelName;

    /**
     * create_time
     */
    private BigInteger createTime;

    /**
     * create_user
     */
    private String createUser;

    /**
     * modify_time
     */
    private BigInteger modifyTime;

    /**
     * modify_user
     */
    private String modifyUser;

    /**
     * remark
     */
    private String remark;

    private String sportJson;
    private String tournamentJson;
    private String orderTypeJson;
    private String playJson;
    private String orderStageJson;
    private String secondaryLabelJson;

    private String sportIdsJson;
    private String tournamentIdsJson;
    private String orderTypeIdsJson;
    private String playIdsJson;
    private String orderStageIdsJson;
    private String secondaryLabelIdsJson;


    private Long vipUpdateTime;

    /**
     * 日志类型(1 标签 2 行情)
     */
    private Integer type;

    @ApiModelProperty(value = "商户编码", example = "123123")
    private String merchantCode;

    @ApiModelProperty(value = "提前结算投注笔数", example = "123123")
    private Integer preOrderCount;

    @ApiModelProperty(value = "提前结算胜率", example = "12.33")
    private BigDecimal preWinRate;

    @ApiModelProperty(value = "提前结算盈利率", example = "12.33")
    private BigDecimal preProfitRate;

    @ApiModelProperty(value = "总投注笔数", example = "123123")
    private Integer totalOrderCount;

    @ApiModelProperty(value = "总胜率", example = "12.33")
    private BigDecimal totalWinRate;

    @ApiModelProperty(value = "总盈利率", example = "12.33")
    private BigDecimal totalProfitRate;

    @ApiModelProperty(value = "总盈利金额", example = "12.33")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "extra_margin结果")
    private BigDecimal extraMarginResult;

    @ApiModelProperty(value = "组别结果")
    private Integer groupNumResult;

    @ApiModelProperty(value = "用户自行风控开关 0关 1开")
    private Integer tagUserRiskControlStatus;



    private Integer dynamicMarketLevelSwitch;

    private Integer dynamicMarketLevel;

    private Integer dynamicMargin;

    private Integer tagMarketLevelId;

}
